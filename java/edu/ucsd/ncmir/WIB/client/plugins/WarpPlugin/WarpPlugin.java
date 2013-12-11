package edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.core.KeyData;
import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.image.ImageFactoryInterface;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.ClearTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.GetDisplayListMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.InformationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.KeyPressMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.MousePositionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderRequestMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetTransientVectorOverlayLineWidthMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.StatusMessage;
import edu.ucsd.ncmir.WIB.client.core.request.AbstractXMLRequestCallback;
import edu.ucsd.ncmir.WIB.client.core.request.HTTPRequest;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.DefaultPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.CompleteAddCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.CompleteAddMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.CompleteEditMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.CompleteSelectCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DeleteMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DragAddMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DragEditMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DrawAddCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DrawSelectCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.EnableAddMarkOptionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.EnableCandidateOptionsMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.EnableMarkDependentMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.GenerateWarpMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.ImageNameSelectionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.ImageSelectionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.SelectEditMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.StartAddCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.StartAddMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.StartSelectCandidateMessage;
import edu.ucsd.ncmir.spl.XMLUtil.XML;
import java.util.HashMap;

/**
 *
 * @author spl
 */
public class WarpPlugin
    extends DefaultPlugin

{


    private String _view = "template";
    private final HashMap<String,HashMap<Mark,Mark>> _marks =
	new HashMap<String,HashMap<Mark,Mark>>();


    public WarpPlugin()

    {

	super( CompleteAddCandidateMessage.class,
	       CompleteAddMarkMessage.class,
	       CompleteEditMarkMessage.class,
	       CompleteSelectCandidateMessage.class,
	       DeleteMarkMessage.class,
	       DragAddMarkMessage.class,
	       DragEditMarkMessage.class,
	       DrawAddCandidateMessage.class,
	       DrawSelectCandidateMessage.class,
	       GenerateWarpMessage.class,
	       GetDisplayListMessage.class,
	       ImageSelectionMessage.class,
	       KeyPressMessage.class,
               MousePositionMessage.class,
	       SelectEditMarkMessage.class,
	       StartAddCandidateMessage.class,
	       StartAddMarkMessage.class,
	       StartSelectCandidateMessage.class );

	this.clearMarks();

    }

    private void clearMarks()

    {

	this._nmark = 0;
	this._marks.put( "template", new HashMap<Mark,Mark>() );
	this._marks.put( "i1", new HashMap<Mark,Mark>() );
	this._marks.put( "i2", new HashMap<Mark,Mark>() );

    }

    @Override
    public void setupPlugin()

    {

	HTTPRequest.get( "cgi-bin/warp.pl?action=initialize" +
			 "&config=" + Configuration.parameter( "config" ),
			 new GetKey( this ) );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof MousePositionMessage )
	    this.mousePosition( ( Point ) o );
	else if ( m instanceof ImageSelectionMessage )
	    this.selectImage( ( String ) o );
	else if ( m instanceof DrawSelectCandidateMessage )
	    this.drawSelectFiducial( ( Point ) o );
	else if ( m instanceof CompleteSelectCandidateMessage )
	    this.completeSelectFiducial( ( Point ) o );
	else if ( m instanceof StartSelectCandidateMessage )
	    this.startSelectFiducial( ( Point ) o );
	else if ( m instanceof DrawAddCandidateMessage )
	    this.drawSelectFiducial( ( Point ) o );
	else if ( m instanceof CompleteAddCandidateMessage )
	    this.completeAddFiducial( ( Point ) o );
	else if ( m instanceof StartAddCandidateMessage )
	    this.startSelectFiducial( ( Point ) o );
	else if ( m instanceof GetDisplayListMessage )
	    this.getDisplayList( ( GetDisplayListMessage ) m );
	else if ( m instanceof DeleteMarkMessage )
	    this.deleteCandidate( ( Point ) o );
	else if ( m instanceof SelectEditMarkMessage )
	    this.selectEditMark( ( Point ) o );
	else if ( m instanceof DragEditMarkMessage )
	    this.dragEditMark( ( Point ) o );
	else if ( m instanceof CompleteEditMarkMessage )
	    this.completeEditMark( ( Point ) o );
	else if ( m instanceof StartAddMarkMessage )
	    this.startAddMark( ( Point ) o );
	else if ( m instanceof DragAddMarkMessage )
	    this.dragAddMark( ( Point ) o );
	else if ( m instanceof CompleteAddMarkMessage )
	    this.completeAddMark( ( Point ) o );
	else if ( m instanceof KeyPressMessage )
	    this.changeView( ( KeyData ) o );
	else if ( m instanceof GenerateWarpMessage )
	    this.generateWarp();

    }

    private HashMap<String,String> _view_table = new HashMap<String,String>();

    @Override
    public String getTransactionURL()

    {

	return "cgi-bin/Loader.pl?" +
	    "type=flat&" +
	    "uri=" + this._view_table.get( this._view ) + "&" +
	    "transaction=";

    }

    /**
     * Called by the
     * <code>WIB</code> module upon loading.
     *
     * @param menu_bar The top-level <code>MenuBar</code> object to be added to.
     */
    @Override
    protected void configureMenuBar( MenuBar menu_bar )
    {

        super.configureMenuBar( menu_bar );

        menu_bar.addItem( "Actions", new WarpActionsMenu() );
        menu_bar.addItem( "View", new WarpViewMenu() );

    }

    private WarpImageFactory _image_factory = null;

    @Override
    public ImageFactoryInterface getImageFactory()

    {

	if ( this._image_factory == null )
	    this._image_factory = new WarpImageFactory( this._view );

        return this._image_factory;

    }

    private void mousePosition( Point point )

    {

	Mark m = this.findNearestMark( point, 10 );

	if ( m != null )
	    new StatusMessage( "Mark:", m.getID(),
			       "Position:", m.getX(), m.getY() ).send();
	else
	    new StatusMessage( "" ).send();

    }

    private Mark findNearestMark( Point point, int distance )

    {

	double x = point.getX();
	double y = point.getY();

	double mindist = Double.MAX_VALUE;
	Mark mark = null;

	for ( Mark m : this._marks.get( this._view ).values() ) {

	    double d = m.distance( x, y );

	    if ( ( d < distance ) && ( mindist > d ) ) {

		mindist = d;
		mark = m;

	    }

	}

	return mark;

    }

    private String _key = null;

    private void setKey( Element root )

    {

	this._key = XML.find( root, "key" ).getAttribute( "id" );
	this._view_table.put( "template",
			      XML.find( root,
					"template" ).getAttribute( "path" ) );
	this._view_table.put( "i1",
			      XML.find( root,
					"i1" ).getAttribute( "path" ) );
	this._view_table.put( "i2",
			      XML.find( root,
					"i2" ).getAttribute( "path" ) );

	new TransactionTimer( this._key );
	super.setupPlugin();
	this.selectImage( "template" );

    }

    private void selectImage( String view )

    {

	this._view = view;

	this.menuUpdate();

        new InformationMessage().send( view + ": " +
                                       this._view_table.get( view ) );
	new RenderRequestMessage().send();

    }

    private void menuUpdate()

    {

	boolean there_are_marks = this._marks.get( "template" ).size() > 0;
	new EnableCandidateOptionsMessage().send( this._view.equals( "template" ) );
	new EnableAddMarkOptionMessage().send( this._view.equals( "template" ) &&
					       there_are_marks );
	new EnableMarkDependentMessage().send( there_are_marks );

    }

    private void changeView( KeyData key_data )

    {

	String view = null;

	switch ( key_data.getKeyCode() ) {

        case KeyCodes.KEY_PAGEDOWN: {

            view = this.bumpImage( -1, key_data.isShiftKeyDown() );
            break;

        }
        case KeyCodes.KEY_PAGEUP: {

            view = this.bumpImage( 1, key_data.isShiftKeyDown() );
            break;

        }
	case '1': {

	    view = "template";
	    break;

	}
	case '2': {

	    view = "i1";
	    break;

	}
	case '3': {

	    view = "i2";
	    break;

	}

        }
	if ( view != null ) {

	    new ImageSelectionMessage().send( view );
	    new ImageNameSelectionMessage().send( view );

	}

    }

    private String bumpImage( int direction, boolean shifted )

    {

	String[] v = { "template", "i1", "i2" };

	int i;

	if ( direction == -1 && shifted )
	    i = 0;
	else if ( direction == 1 && shifted )
	    i = v.length - 1;
	else {

	    for ( i = 0; i < v.length; i++ )
		if ( v[i].equals( this._view ) )
		    break;

	    i = ( i + ( direction + v.length ) ) % v.length;

	}
	return v[i];

    }

    private void getDisplayList( GetDisplayListMessage gdlm )

    {

	HashMap<Mark,Mark> marks = this._marks.get( this._view );

	if ( marks.size() > 0 )
	    gdlm.addPoints( marks.values().toArray( new Mark[0] ) );

    }

    private void deleteCandidate( Point point )

    {

	Mark delmark = this.findNearestMark( point, 5 );

	if ( delmark != null ) {

	    this._marks.get( this._view ).remove( delmark );
	    for ( Mark m : delmark.getSiblings() )
		this._marks.get( m.getObjectName() ).remove( m );

	}
	new RenderRequestMessage().send();

    }

    private Square _fiducial = null;

    private void startSelectFiducial( Point point )

    {

	if ( this._view.equals( "template" ) ) {

	    this._fiducial = new Square( point );
	    new SetTransientVectorOverlayLineWidthMessage().send( 1 );

	} else
	    Window.alert( "Sorry.\n" +
			  "You must be on the template\n" +
			  "image to use this option." );

    }

    private void drawSelectFiducial( Point point )

    {

	if ( this._fiducial != null ) {

	    this._fiducial.updateCorner( point );
	    new RenderTransientVectorOverlayMessage().send( new Drawable[] {
		    this._fiducial
		} );

	}

    }

    private Mark _mark = null;

    private void selectEditMark( Point point )

    {

	this._mark = this.findNearestMark( point, 10 );

	if ( this._mark != null ) {

	    this._mark.setVisible( false );
	    new RenderRequestMessage().send();
	    this.renderTransientMark( point.getX(), point.getY() );

	}

    }

    private void renderTransientMark( double x, double y )

    {

	this._mark.setLocation( x, y );
	new RenderTransientVectorOverlayMessage().send( new Drawable[] {
		new Mark( x, y, -1 )
	    } );

    }

    private void dragEditMark( Point point )

    {

	if ( this._mark != null )
	    this.renderTransientMark( point.getX(), point.getY() );

    }

    private void completeEditMark( Point point )

    {

	if ( this._mark != null ) {

	    this.renderTransientMark( point.getX(), point.getY() );

	    this._mark.setVisible( true );

	    // Reload the hash table.

	    // This is necessary because the object lookup is
	    // accomplished by using the X,Y coordinates as the hash
	    // code and if we don't, the mark can get lost.

	    HashMap<Mark,Mark> temp = new HashMap<Mark,Mark>();

	    for ( Mark m :
		      this._marks.get( this._mark.getObjectName() ).values() )
		temp.put( m, m );

	    this._marks.put( this._mark.getObjectName(), temp );

	    new ClearTransientVectorOverlayMessage().send();
	    new RenderRequestMessage().send();

	    if ( !this._mark.getObjectName().equals( "template" ) ) {

		Mark[] siblings = this._mark.getSiblings();

		HashMap<String,Mark> marks = new HashMap<String,Mark>();

		for ( Mark sibling : siblings )
		    marks.put( sibling.getObjectName(), sibling );

		Mark template = marks.get( "template" );

		HTTPRequest.get( "cgi-bin/warp.pl?action=correct" +
				 "&key=" + this._key +
				 "&template=" + template.getObjectName() +
				 "&template_x=" + ( int ) template.getX() +
				 "&template_y=" + ( int ) template.getY() +
				 "&target=" + this._mark.getObjectName() +
				 "&target_x=" + ( int ) this._mark.getX() +
				 "&target_y=" + ( int ) this._mark.getY() +
				 "&size=" + this._selection_size,
				 new MarkCorrectionCallback( this._mark ) );

	    }
	    this._mark = null;

	}

    }

    private void startAddMark( Point point )

    {

	this._mark = new Mark( point.getX(), point.getY(), -1 );

	this._mark.setVisible( false );
	new RenderRequestMessage().send();
	this.renderTransientMark( point.getX(), point.getY() );

    }

    private void dragAddMark( Point point )

    {

	this.renderTransientMark( point.getX(), point.getY() );

    }

    private void completeAddMark( Point point )

    {

	new ClearTransientVectorOverlayMessage().send();
	new RenderRequestMessage().send();

	int n = 0;
	double dx1 = 0;
	double dy1 = 0;
	double dx2 = 0;
	double dy2 = 0;

	Mark[] marks =
	    this._marks.get( "template" ).values().toArray( new Mark[0] );
	for ( Mark t : marks ) {

	    int x = ( int ) t.getX();
	    int y = ( int ) t.getY();

	    Mark i1 = t.getSibling( "i1" );
	    dx1 += i1.getX() - x;
	    dy1 += i1.getY() - y;

	    Mark i2 = t.getSibling( "i2" );
	    dx2 += i2.getX() - x;
	    dy2 += i2.getY() - y;

	    n++;

	}

	dx1 /= n;
	dy1 /= n;

	dx2 /= n;
	dy2 /= n;

	int x = ( int ) point.getX();
	int y = ( int ) point.getY();

	HTTPRequest.get( "cgi-bin/warp.pl?action=add_mark" +
			 "&key=" + this._key +
			 "&x=" + x +
			 "&y=" + y +
			 "&p1=" + ( int ) ( x + dx1 ) +
			 "&q1=" + ( int ) ( y + dy1 ) +
			 "&p2=" + ( int ) ( x + dx2 ) +
			 "&q2=" + ( int ) ( y + dy2 ) +
			 "&size=" + this._selection_size,
			 new AddMarkCallback( this ) );

	this._mark = null;

    }

    private void addMark( Element root )

    {

	Element mark = XML.find( root, "add_mark" );

	this.insertNewMark( Double.parseDouble( mark.getAttribute( "x" ) ),
			    Double.parseDouble( mark.getAttribute( "y" ) ),
			    Double.parseDouble( mark.getAttribute( "p1" ) ),
			    Double.parseDouble( mark.getAttribute( "q1" ) ),
			    Double.parseDouble( mark.getAttribute( "p2" ) ),
			    Double.parseDouble( mark.getAttribute( "q2" ) ) );

	new RenderRequestMessage().send();

    }

    private void generateWarp()

    {

	String data = "";

	for ( Mark m : this._marks.get( "template" ).values() ) {

	    data += m.getX() + " " + m.getY();

	    for ( Mark sibling : m.getSiblings() )
		data += " " + sibling.getX() + " " + sibling.getY();

	    data += "\n";

	}
	HTTPRequest.post( "cgi-bin/warp.pl?action=warp&key=" + this._key,
			  data,
			  new WarpCompletionCallback() );

    }

    private void completeAddFiducial( Point point )

    {

	this.performSelection( point, new AddFiducialCallback( this ) );

    }


    private void completeSelectFiducial( Point point )

    {

	this.performSelection( point, new FiducialCallback( this ) );

    }

    private double _selection_size = 0;

    private void performSelection( Point point,
                                   AbstractXMLRequestCallback callback )

    {

	if ( this._fiducial != null ) {

	    this._fiducial.updateCorner( point );
	    double[][] list = this._fiducial.toArray( new double[0][] );

	    this._selection_size = Math.ceil( list[2][0] - list[0][0] );

	    HTTPRequest.get( "cgi-bin/warp.pl?action=fiducial" +
			     "&key=" + this._key +
			     "&view=" + this._view +
			     "&x=" + list[0][0] +
			     "&y=" + list[0][1] +
			     "&size=" + this._selection_size,
			     callback );

	    this._fiducial = null;
	    new ClearTransientVectorOverlayMessage().send();

	}

    }

    private void handleFiducials( Element root )

    {

	this.clearMarks();
	this.handleAddFiducials( root );

    }

    private int _nmark = 0;

    private void handleAddFiducials( Element root )

    {

	NodeList nl = root.getElementsByTagName( "fiducial" );

	for ( int m = 0; m < nl.getLength(); m++ ) {

	    Element fiducial = ( Element ) nl.item( m );

	    String[] template_xy =
		fiducial.getAttribute( "template" ).trim().split( " " );
	    String[] i1_xy =
		fiducial.getAttribute( "i1" ).trim().split( " " );
	    String[] i2_xy =
		fiducial.getAttribute( "i2" ).trim().split( " " );

	    this.insertNewMark( Double.parseDouble( template_xy[0] ),
				Double.parseDouble( template_xy[1] ),
				Double.parseDouble( i1_xy[0] ),
				Double.parseDouble( i1_xy[1] ),
				Double.parseDouble( i2_xy[0] ),
				Double.parseDouble( i2_xy[1] ) );

	}

	this.menuUpdate();

	new RenderRequestMessage().send();

    }

    private void insertNewMark( double x, double y,
				double p1, double q1,
				double p2, double q2 )

    {

	HashMap<Mark,Mark> template_marks = this._marks.get( "template" );
	HashMap<Mark,Mark> i1_marks = this._marks.get( "i1" );
	HashMap<Mark,Mark> i2_marks = this._marks.get( "i2" );

	Mark templatemark = new Mark( x, y, this._nmark );

	boolean ok = true;

	for ( Mark m2 : template_marks.values() )
	    if ( !( ok = ( templatemark.distance( m2 ) > 10 ) ) )
		break;

	if ( ok ) {

	    Mark i1mark = new Mark( p1, q1, this._nmark );
	    Mark i2mark = new Mark( p2, q2, this._nmark );

	    this._nmark++;

	    templatemark.setObjectName( "template" );
	    i1mark.setObjectName( "i1" );
	    i2mark.setObjectName( "i2" );

	    templatemark.addSibling( i1mark );
	    templatemark.addSibling( i2mark );

	    i1mark.addSibling( templatemark );
	    i1mark.addSibling( i2mark );

	    i2mark.addSibling( templatemark );
	    i2mark.addSibling( i1mark );

	    template_marks.put( templatemark, templatemark );
	    i1_marks.put( i1mark, i1mark );
	    i2_marks.put( i2mark, i2mark );

	}

    }

    private static class TransactionTimer
	extends Timer

    {

        private final String _key;

	TransactionTimer( String key )

	{

	    this._key = key;
	    this.scheduleRepeating( 290000 );

	}

        @Override
	public void run()

	{

	    HTTPRequest.get( "cgi-bin/warp.pl?action=noop" +
			     "&key=" + this._key );

	}

    }

    private static class GetKey
	extends AbstractXMLRequestCallback

    {

	private WarpPlugin _plugin;

	GetKey( WarpPlugin plugin )

	{

	    this._plugin = plugin;

	}

        @Override
	protected void handleXMLResponse( Element root )

	{

	    this._plugin.setKey( root );

	}

    }

    private static class FiducialCallback
	extends AbstractXMLRequestCallback

    {

	private WarpPlugin _wp;

	public FiducialCallback( WarpPlugin wp )

        {

	    this._wp = wp;

        }

	@Override
        protected void handleXMLResponse( Element root )

        {

	    this._wp.handleFiducials( root );

        }

    }

    private static class AddFiducialCallback
	extends AbstractXMLRequestCallback

    {

	private WarpPlugin _wp;

	public AddFiducialCallback( WarpPlugin wp )

        {

	    this._wp = wp;

        }

	@Override
        protected void handleXMLResponse( Element root )

        {

	    this._wp.handleAddFiducials( root );

        }

    }

    private static class AddMarkCallback
	extends AbstractXMLRequestCallback

    {

	private WarpPlugin _wp;

	public AddMarkCallback( WarpPlugin wp )

        {

	    this._wp = wp;

        }

	@Override
        protected void handleXMLResponse( Element root )

        {

	    this._wp.addMark( root );

        }

    }

    private static class MarkCorrectionCallback
	extends AbstractXMLRequestCallback

    {

	private Mark _mark;

	public MarkCorrectionCallback( Mark mark )

        {

	    this._mark = mark;

        }

	@Override
        protected void handleXMLResponse( Element root )

        {

	    String[] xy = XML.extractSubstringsFromElement( root, " " );

	    this._mark.setLocation( Double.parseDouble( xy[0] ),
				    Double.parseDouble( xy[1] ) );
	    new RenderRequestMessage().send();

        }

    }

    private static class WarpCompletionCallback
	extends AbstractXMLRequestCallback

    {

        @Override
        protected void handleXMLResponse( Element root )

        {

	    Element warp = XML.find( root, "warp" );

	    String path = warp.getAttribute( "path" ).trim();

	    Window.open( GWT.getHostPageBaseURL() +
			 "index.html?volumeID=" + path,
			 "_blank",
			 null );

        }

    }

}