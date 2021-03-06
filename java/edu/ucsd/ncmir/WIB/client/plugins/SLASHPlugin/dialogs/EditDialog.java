package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import edu.ucsd.ncmir.WIB.client.core.components.CenteredGrid;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.SLASHPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.BumpNudgeRadiusMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ClearEditSelectionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.EditActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.EditModeSelectionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetEditModeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetEditNudgeModeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetEditObjectLineTypeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetExtendModeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetLineTypeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetNudgeRadiusMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ValidateEditSelectionMessage;

/**
 *
 * @author spl
 */
public class EditDialog
    extends AbstractOperationDialog
    implements MessageListener,
	       MouseDownHandler,
	       MouseMoveHandler,
	       MouseUpHandler,
	       TouchStartHandler,
	       TouchMoveHandler,
	       TouchEndHandler,
	       ValueChangeHandler<Boolean>

{

    private final Canvas _canvas;
    private int _edit_radius;

    private final Label _label = new Label( "Nudge Radius" );

    private final RadioButton _nudge =
	new RadioButton( "edit_mode", "Nudge" );
    private final RadioButton _drag =
	new RadioButton( "edit_mode", "Attractor" );
    private final RadioButton _extend =
	new RadioButton( "edit_mode", "Extend" );

    private final RadioButton _select_object =
	new RadioButton( "edit_mode_select", "Select Object" );
    private final RadioButton _edit_object =
	new RadioButton( "edit_mode_select", "Edit Object" );

    private final ToggleButton _polyline_polygon_choice_toggle =
	new ToggleButton( "Polygon", "Polyline" );
    private final CenteredGrid _polyline_polygon_choice =
	new CenteredGrid( 1, 2 );

    public EditDialog( int edit_radius )

    {

	super( EditActivationMessage.class );

	this._edit_radius = edit_radius;

	super.addTitle( "Edit Controls" );

	CenteredGrid grid = new CenteredGrid( 5, 1 );

	CenteredGrid modes = new CenteredGrid( 1, 3 );

	this._nudge.setValue( true );
        this._nudge.addValueChangeHandler( this );
	modes.setWidget( 0, 0, this._nudge );

        this._drag.addValueChangeHandler( this );
	modes.setWidget( 0, 1, this._drag );

        this._extend.addValueChangeHandler( this );
	modes.setWidget( 0, 2, this._extend );

	grid.setWidget( 0, 0, modes );

	grid.setWidget( 1, 0, this._label );
	this._canvas = Canvas.createIfSupported();
	this._canvas.setCoordinateSpaceWidth( 211 );
	this._canvas.setCoordinateSpaceHeight( 211 );

	grid.setWidget( 2, 0, this._canvas );
	this.drawEditRadius();

	if ( MGWT.getOsDetection().isDesktop() ) {

	    this._canvas.addMouseDownHandler( this );
	    this._canvas.addMouseUpHandler( this );

	} else
	    super.addDomHandler( this, TouchStartEvent.getType() );

	CenteredGrid selectors = new CenteredGrid( 1, 2 );

	this._select_object.setValue( true );
        this._select_object.addValueChangeHandler( this );
	selectors.setWidget( 0, 0, this._select_object );

        this._edit_object.addValueChangeHandler( this );
	selectors.setWidget( 0, 1, this._edit_object );

	grid.setWidget( 3, 0, selectors );

	this._polyline_polygon_choice_toggle.addValueChangeHandler( this );
	this._polyline_polygon_choice.setWidget( 0, 0,
                                                 new Label( "Convert to" ) );
	this._polyline_polygon_choice.
	    setWidget( 0, 1,
		       this._polyline_polygon_choice_toggle );
	this._polyline_polygon_choice.setVisible( false );
	grid.setWidget( 4, 0, this._polyline_polygon_choice );

	super.setWidget( grid );

	new ClearEditSelectionMessage().send();
	new SetEditNudgeModeMessage().send();

    }

    // @Override
    // protected void onAttach()

    // {

    // 	MessageManager.registerAsListener( this,
    // 					   BumpNudgeRadiusMessage.class,
    // 					   SetEditObjectLineTypeMessage.class,
    // 					   SetNudgeRadiusMessage.class,
    // 					   EditModeSelectionMessage.class );
    // 	super.onAttach();

    // }

    // @Override
    // protected void onDetach()

    // {

    // 	MessageManager.deregisterAsListener( this,
    // 					     BumpNudgeRadiusMessage.class,
    // 					     SetEditObjectLineTypeMessage.class,
    // 					     SetNudgeRadiusMessage.class,
    // 					     EditModeSelectionMessage.class );
    //     super.onDetach();

    // }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof BumpNudgeRadiusMessage )
	    this.updateNudgeRadius( this._edit_radius +
				    ( ( Integer ) o ).intValue() );
	else if ( m instanceof SetNudgeRadiusMessage ) {

	    this._edit_radius = ( ( Double ) o ).intValue();
	    this.drawEditRadius();

	} else if ( m instanceof EditModeSelectionMessage ) {

	    boolean select_mode = ( ( Boolean ) o ).booleanValue();

	    this._select_object.setValue( select_mode );
	    this._edit_object.setValue( !select_mode );
            this._polyline_polygon_choice.setVisible( !select_mode );

	} else if ( m instanceof SetEditObjectLineTypeMessage )
	    this._polyline_polygon_choice_toggle.setValue( ( Boolean ) o );

    }

    private void drawEditRadius()

    {

	Context2d c2d = this._canvas.getContext2d();

	int cw = this._canvas.getCoordinateSpaceWidth();
	int ch = this._canvas.getCoordinateSpaceHeight();

	c2d.save();

	c2d.setFillStyle( CssColor.make( 110, 215, 93 ) );
	c2d.setStrokeStyle( CssColor.make( 0, 0, 0 ) );
	c2d.fillRect( 0, 0, cw, ch );

	c2d.translate( ( cw - 1 ) / 2.0, ( ch - 1 ) / 2.0 );
	c2d.scale( this._edit_radius, this._edit_radius );
	c2d.setLineWidth( 1.5 / this._edit_radius );

	c2d.beginPath();
	c2d.moveTo( 0, 1 );
	for ( int a = 1; a <= 360; a++ ) {

	    double ang = a * Math.PI / 180;

	    c2d.lineTo( Math.sin( ang ), Math.cos( ang ) );

	}
	c2d.stroke();

	c2d.restore();

    }

    private void radiusHandler( TouchEvent event )

    {

	LightArray<Touch> touches = event.getChangedTouches();
        Touch t = ( Touch ) touches.get( touches.length() - 1 );

        int x = t.getPageX() - this._canvas.getAbsoluteLeft();
	int y = t.getPageY() - this._canvas.getAbsoluteTop();

	if ( ( 0 <= x ) && ( x < this._canvas.getCoordinateSpaceWidth() ) &&
	     ( 0 <= y ) && ( y < this._canvas.getCoordinateSpaceHeight() ) ) {

	    this.radiusHandler( t.getPageX() - this._canvas.getAbsoluteLeft(),
				t.getPageY() - this._canvas.getAbsoluteTop() );
	    event.preventDefault();

	}

    }

    private void radiusHandler( int x, int y )

    {

	int cw = this._canvas.getCoordinateSpaceWidth();
	int ch = this._canvas.getCoordinateSpaceHeight();

	double dx = x - ( ( cw - 1 ) / 2.0 );
	double dy = y - ( ( ch - 1 ) / 2.0 );

	double l = Math.sqrt( ( dx * dx ) + ( dy * dy ) );

	this.updateNudgeRadius( l );

    }

    private void radiusHandler( MouseEvent event )

    {

	this.radiusHandler( event.getX(), event.getY() );

    }

    private static final int MIN_RADIUS = 5;
    private static final int MAX_RADIUS = 100;

    private void updateNudgeRadius( double nudge_radius )

    {

	if ( this._canvas.isEnabled() ) {

	    if ( nudge_radius < EditDialog.MIN_RADIUS )
		nudge_radius = EditDialog.MIN_RADIUS;
	    else if ( nudge_radius > EditDialog.MAX_RADIUS )
		nudge_radius = EditDialog.MAX_RADIUS;
	    new SetNudgeRadiusMessage().send( nudge_radius );

	}

    }

    private HandlerRegistration _touch_move_handler_reg;
    private HandlerRegistration _touch_end_handler_reg;

    @Override
    public void onTouchStart( TouchStartEvent event )
    {

	this.radiusHandler( event );
        this._touch_move_handler_reg =
            super.addDomHandler( this, TouchMoveEvent.getType() );
        this._touch_end_handler_reg =
            super.addDomHandler( this, TouchEndEvent.getType() );

    }

    @Override
    public void onTouchMove( TouchMoveEvent event )
    {

	this.radiusHandler( event );

    }

    @Override
    public void onTouchEnd( TouchEndEvent event )
    {

        this._touch_move_handler_reg.removeHandler();
        this._touch_end_handler_reg.removeHandler();

    }

    private HandlerRegistration _move_registration;

    @Override
    public void onMouseDown( MouseDownEvent event )

    {

	this.radiusHandler( event );
	this._move_registration = this._canvas.addMouseMoveHandler( this );

    }

    @Override
    public void onMouseMove( MouseMoveEvent event )

    {

	this.radiusHandler( event );

    }

    @Override
    public void onMouseUp( MouseUpEvent event )

    {

	this.radiusHandler( event );

	this._move_registration.removeHandler();

    }

    @Override
    public void onValueChange( ValueChangeEvent event )

    {

	Object s = event.getSource();
	boolean v = ( ( Boolean ) event.getValue() ).booleanValue();

	if ( s instanceof ToggleButton )
	    new SetLineTypeMessage().send( v );
	else if ( ( s instanceof RadioButton ) && v ) {

	    RadioButton rb = ( RadioButton ) s;

	    if ( rb == this._select_object )
		new ClearEditSelectionMessage().send();
            else if ( rb == this._edit_object )
		new ValidateEditSelectionMessage().send();
	    else if ( rb == this._nudge )
		this.setRadiusMode( "Nudge", SLASHPlugin.EditMode.NUDGE );
	    else if ( rb == this._drag )
		this.setRadiusMode( "Attractor", SLASHPlugin.EditMode.PULL );
	    else if ( rb == this._extend ) {

		new SetEditModeMessage().send( SLASHPlugin.EditMode.EXTEND );
		this._label.setText( "Extend Mode" );
		this._canvas.setVisible( false );

	    }

	}

    }

    private void setRadiusMode( String label, SLASHPlugin.EditMode mode )

    {

	new SetEditModeMessage().send( mode );
	this._label.setText( label + " Radius" );
	this._canvas.setVisible( true );
	new SetExtendModeMessage().send( false );

    }

    @Override
    protected void init()

    {

	MessageManager.registerAsListener( this,
					   BumpNudgeRadiusMessage.class,
					   SetEditObjectLineTypeMessage.class,
					   SetNudgeRadiusMessage.class,
					   EditModeSelectionMessage.class );

    }

    @Override
    protected void cleanup()

    {

	MessageManager.deregisterAsListener( this,
					     BumpNudgeRadiusMessage.class,
					     SetEditObjectLineTypeMessage.class,
					     SetNudgeRadiusMessage.class,
					     EditModeSelectionMessage.class );
	
    }

}

