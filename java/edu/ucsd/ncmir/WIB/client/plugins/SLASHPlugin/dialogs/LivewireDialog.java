package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireDrawPolylineMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireDrawPolygonMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireAddPolygonMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireAddPolylineMessage;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DisableLivewireMessage;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.xml.client.Element;
import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ClearTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ParameterUpdateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetTransientVectorOverlayLineWidthMessage;
import edu.ucsd.ncmir.WIB.client.core.request.AbstractXMLRequestCallback;
import edu.ucsd.ncmir.WIB.client.core.request.HTTPRequest;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.Annotation;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolygonActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolylineActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DrawPolygonActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DrawPolylineActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireSelectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SLASHdbLinkMessage;
import edu.ucsd.ncmir.spl.XMLUtil.XML;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author spl
 */
public class LivewireDialog
    extends AbstractOperationDialog
    implements MessageListener

{

    private final HTML _status = new HTML( "" );
    private final OptionButtons _buttons = new OptionButtons( this );

    public LivewireDialog()

    {

	super( LivewireActivationMessage.class );

	super.addTitle( "Livewire" );

	Grid g = new Grid( 2, 1 );

	g.setWidget( 0, 0, this._status );
	g.setWidget( 1, 0, this._buttons );

	this.add( g );

	MessageManager.registerAsListener( this,
					   AbstractActivationMessage.class,
					   LivewireSelectMessage.class,
					   ParameterUpdateMessage.class,
					   SLASHdbLinkMessage.class );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof LivewireSelectMessage )
	    this._current_state.addPoint( ( Point ) o );
	else if ( m instanceof AbstractActivationMessage )
	    this.setActivation( ( AbstractActivationMessage ) m );
	else if ( m instanceof ParameterUpdateMessage )
	    this.setParameters( ( ParameterUpdateMessage ) m );
	else if ( m instanceof SLASHdbLinkMessage )
	    this.setSLASHLink( ( String ) o );

    }

    private static class NoOpMessage extends Message {}

    private enum Mode {
	NONE( new NoOpMessage() ),
	DRAW_POLYGON( new LivewireDrawPolygonMessage() ),
	DRAW_POLYLINE( new LivewireDrawPolylineMessage() ),
	ADD_POLYGON( new LivewireAddPolygonMessage() ),
	ADD_POLYLINE( new LivewireAddPolylineMessage() );

	private final Message _message;

	Mode( Message message )

	{

	    this._message = message;

	}

	Message getMessage()

	{

	    return this._message;

	}

    }

    private Mode _mode;

    private void setActivation( AbstractActivationMessage aam )

    {

	if ( aam instanceof DrawPolygonActivationMessage )
	    this._mode = Mode.DRAW_POLYGON;
	else if ( aam instanceof DrawPolylineActivationMessage )
	    this._mode = Mode.DRAW_POLYLINE;
	else if ( aam instanceof AddPolygonActivationMessage )
	    this._mode = Mode.ADD_POLYGON;
	else if ( aam instanceof AddPolylineActivationMessage )
	    this._mode = Mode.ADD_POLYLINE;
	else if ( !( aam instanceof LivewireActivationMessage ) )
	    this._mode = Mode.NONE;

    }

    private String _path = null;

    private void setSLASHLink( String uri )

    {

	String scheme = UriUtils.extractScheme( uri );

	if ( !scheme.equals( "file" ) ) {

	    Window.alert( "The URI\n" +
			  uri + "\n" +
			  "is incompatible with Livewire.\n" +
			  "This option will be disabled." );
	    new DisableLivewireMessage().send();

	} else
	    this._path = uri.split( ":" )[1].trim();

    }

    private void setStatus( String html )

    {

	this._status.setHTML( "<b>" + html + "</b>" );

    }

    private Stack<AbstractState> _undo_stack = new Stack<AbstractState>();
    private AbstractState _current_state = null;
    private Stack<AbstractState> _redo_stack = new Stack<AbstractState>();

    private void updateState( AbstractState state )

    {

	if ( this._current_state != null )
	    this._undo_stack.push( this._current_state );
	this._redo_stack.clear();
	this._current_state = state;
	state.activate( this );

    }

    private void reset()

    {

	this._redo_stack.clear();
	this._undo_stack.clear();
	this._current_state = null;
	this.updateState( new InitialState( this ) );

    }

    private void finish()

    {

	Annotation annotation = new Annotation();

	for ( AbstractState as : this._undo_stack ) {
	    
	    double[][] points = as.getData();

	    if ( points != null )
		annotation.addAll( Arrays.asList( points ) );

	}
	this._mode.getMessage().send( annotation );
	new ClearTransientVectorOverlayMessage().send();
	this.reset();

    }

    private void undo()

    {

	this._redo_stack.push( this._current_state );
	this._current_state = this._undo_stack.pop();
	this._current_state.activate( this );
	this.render();

    }

    private void redo()

    {

	this._undo_stack.push( this._current_state );
	this._current_state = this._redo_stack.pop();
	this._current_state.activate( this );
	this.render();

    }

    @Override
    protected void init()

    {

	this.reset();

    }

    @Override
    protected void cleanup()

    {

    }

    private int _z;

    private void setParameters( ParameterUpdateMessage pum )

    {

	this._z = pum.getPlane();

    }

    private void computeSegment( AbstractState next_state )

    {

	Point from_state_point = this._undo_stack.peek().getPoint();
	Point to_state_point = this._current_state.getPoint();

	this._current_state.clearPoints();

	HTTPRequest.get( "cgi-bin/livewire.pl?" +
			 "smooth=true&" +
			 "file=" + this._path + "&" +
			 "plane=" + this._z + "&" +
			 "from_x=" + ( int ) from_state_point.getX() + "&" +
			 "from_y=" + ( int ) from_state_point.getY() + "&" +
			 "to_x=" + ( int ) to_state_point.getX() + "&" +
			 "to_y=" + ( int ) to_state_point.getY(),
			 this._current_state );
	this.updateState( next_state );
        this.render();

    }

    private void render()

    {

	DisplayList dl = new DisplayList();

	ArrayList<Drawable> drawable_list = new ArrayList<Drawable>();

	for ( AbstractState as : this._undo_stack ) {

	    double[][] data = as.getData();

	    if ( data != null )
		dl.addData( data );
	    drawable_list.add( new Mark( as.getPoint() ) );

	}

	if ( dl.size() > 0 ) 
	    drawable_list.add( dl );
	new ClearTransientVectorOverlayMessage().send();
	new SetTransientVectorOverlayLineWidthMessage().send( 3 );
	Drawable[] dwl = drawable_list.toArray( new Drawable[0] );
	new RenderTransientVectorOverlayMessage().send( dwl );

    }

    private static class DisplayList
	extends Drawable

    {

        @Override
        public void setObjectName( String object_name )

        {

        }

        @Override
        public String getObjectName()

        {

	    return null;

        }

	private int _red;
	private int _green;
	private int _blue;

        @Override
        public void setRGB( int red, int green, int blue )

        {

        }

        @Override
        public int getRed()

        {

	    return 0xff;

        }

        @Override
        public int getGreen()

        {

	    return 0xff;

        }

        @Override
        public int getBlue()

        {

	    return 0x00;

        }

        private void addData( double[][] data )

        {
            super.addAll( Arrays.asList( data ) );

        }

    }

    private void enableButtons( boolean undo_state, boolean finish_state )

    {

	this._buttons.buttonEnable( undo_state,
				    this._redo_stack.size() > 0,
				    finish_state );

    }

    private static class OptionButtons
        extends Grid

    {

        private final AbstractStateButton _undo_button;
        private final AbstractStateButton _redo_button;
        private final AbstractStateButton _finish_button;

        private OptionButtons( LivewireDialog lwd )

        {

	    super( 1, 3 );

	    this._undo_button = new UndoButton( lwd );
            this.setWidget( 0, 0, this._undo_button );

	    this._redo_button = new RedoButton( lwd );
            this.setWidget( 0, 1, this._redo_button );

	    this._finish_button = new FinishButton( lwd );
            this.setWidget( 0, 2, this._finish_button );

        }

        private void buttonEnable( boolean undo_state,
				   boolean redo_state,
				   boolean finish_state )

        {

	    this._undo_button.setEnabled( undo_state );
	    this._redo_button.setEnabled( redo_state );
	    this._finish_button.setEnabled( finish_state );

        }

	private abstract static class AbstractStateButton
	    extends Button
	    implements ClickHandler
	
	{

	    
	    private final LivewireDialog _lwd;

	    AbstractStateButton( String label, LivewireDialog lwd )

	    {

		super( label );
		this._lwd = lwd;
		this.addClickHandler( this );

	    }

            @Override
            public void onClick( ClickEvent event )

            {

		this.handler( this._lwd );

	    }

	    abstract protected void handler( LivewireDialog lwd );

	}

	private static class UndoButton
	    extends AbstractStateButton

	{

	    UndoButton( LivewireDialog lwd )

	    {

		super( "Undo", lwd );

	    }

            @Override
	    protected void handler( LivewireDialog lwd )

	    {
	    
		lwd.undo();

            }

	}

	private static class RedoButton
	    extends AbstractStateButton

	{

	    RedoButton( LivewireDialog lwd )

	    {

		super( "Redo", lwd );

	    }

            @Override
	    protected void handler( LivewireDialog lwd )

	    {
	    
		lwd.redo();

            }

	}

	private static class FinishButton
	    extends AbstractStateButton

	{

	    FinishButton( LivewireDialog lwd )

	    {

		super( "Finish", lwd );

	    }

            @Override
	    protected void handler( LivewireDialog lwd )

	    {
	    
		lwd.finish();

            }

	}

    }

    private abstract static class AbstractState
	extends AbstractXMLRequestCallback

    {

	private final LivewireDialog _lwd;
	AbstractState( LivewireDialog lwd, String status_message )

	{

	    this._lwd = lwd;
	    lwd.setStatus( status_message );

	}

	private Point _p;

	void addPoint( Point p )

	{

	    this._p = p;
	    this.updateState( this._lwd );

	}

	Point getPoint()

	{

	    return this._p;

	}

	private double[][] _data = null;

	double[][] getData()

	{

	    return this._data;

	}

        @Override
	protected final void handleXMLResponse( Element root )

        {

	    String status = root.getAttribute( "status" );

	    if ( status.equals( "success" ) ) {

		Element trace = XML.find( root, "trace" );
		String[] t = XML.extractSubstringsFromElement( trace, "\n" );

		this._data = new double[t.length][2];

		for ( int i = 0; i < t.length; i++ ) {

		    String[] s = t[i].trim().split( " " );

		    this._data[i][0] = Double.parseDouble( s[0].trim() );
		    this._data[i][1] = Double.parseDouble( s[1].trim() );

		}
		this._lwd.render();

	    }

        }

        private void clearPoints()

        {

	    this._data = null;

        }

	protected abstract void updateState( LivewireDialog lwd );
        protected abstract void activate( LivewireDialog lwd );

    }

    private static class InitialState
	extends AbstractState

    {

	InitialState( LivewireDialog lwd )

	{

	    super( lwd, "Click to set initial contour point." );

	}

        @Override
	protected void updateState( LivewireDialog lwd )

        {

	    lwd.updateState( new FirstSegmentState( lwd ) );
	    lwd.render();

        }

        @Override
        protected void activate( LivewireDialog lwd )
        {

	    lwd.enableButtons( false, false );

        }

    }

    private static class FirstSegmentState
        extends AbstractState
    {

        public FirstSegmentState( LivewireDialog lwd )

        {

            super( lwd, "Add initial contour segment" );

        }

        @Override
        protected void updateState( LivewireDialog lwd )

        {

            lwd.computeSegment( new AddCompletionPointState( lwd ) );

        }

        @Override
        protected void activate( LivewireDialog lwd )
        {

	    lwd.enableButtons( true, false );

        }

    }

    private static class AddCompletionPointState
        extends AbstractState
    {

        public AddCompletionPointState( LivewireDialog lwd )
        {

            super( lwd, "Add contour segment" );

        }

        @Override
        protected void updateState( LivewireDialog lwd )

        {

            lwd.computeSegment( new AddCompletionPointState( lwd ) );

        }

        @Override
        protected void activate( LivewireDialog lwd )
        {

	    lwd.enableButtons( true, true );

        }

    }

    private static class Mark
        extends Drawable
    {

        public Mark( Point point )

        {

	    double x = point.getX();
	    double y = point.getY();
	    
	    this.add( new double[] { x - 5, y } );
	    this.add( new double[] { x + 5, y } );
	    this.setBreak();
	    this.add( new double[] { x, y - 5 } );
	    this.add( new double[] { x, y + 5 } );

        }

        @Override
        public void setObjectName( String object_name )

        {
            
            // Does nothing;
            
        }

        @Override
        public String getObjectName()

        {
            
            return "Mark";
            
        }

        @Override
        public void setRGB( int red, int green, int blue )

        {

	    // Does nothing;

        }

        @Override
        public int getRed()

        {

	    return 0xff;

        }

        @Override
        public int getGreen()

        {

	    return 0xff;

        }

        @Override
        public int getBlue()

        {

	    return 0x00;

        }

    }

}
