package edu.ucsd.ncmir.WIB.client.core.render3d;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import edu.ucsd.ncmir.WIB.client.core.KeyData;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.PopInteractionFactoryMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.PushInteractionFactoryMessage;
import edu.ucsd.ncmir.WIB.client.core.panel.AbstractWIBPanel;
import edu.ucsd.ncmir.WIB.client.core.render3d.renderable.Renderable;
import edu.ucsd.ncmir.WIB.client.core.render3d.renderer.Renderer;
import edu.ucsd.ncmir.WIB.client.core.render3d.trackball.Trackball;
import edu.ucsd.ncmir.WIB.client.core.render3d.trackball.TrackballCallbackInterface;
import edu.ucsd.ncmir.spl.LinearAlgebra.AbstractDoubleVector;
import edu.ucsd.ncmir.spl.LinearAlgebra.Double3Vector;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;
import gwt.g3d.client.gl2.GL2;
import gwt.g3d.client.gl2.GL2ContextHelper;
import gwt.g3d.client.gl2.enums.ClearBufferMask;
import gwt.g3d.client.gl2.enums.DepthFunction;
import gwt.g3d.client.gl2.enums.EnableCap;

/**
 *
 * @author spl
 */
public class ThreeDRenderingPanel
    extends AbstractWIBPanel
    implements MouseDownHandler,
	       MouseMoveHandler,
	       MouseUpHandler,
	       MouseWheelHandler,
	       MouseOverHandler,
	       MouseOutHandler,
	       TouchStartHandler,
	       TouchMoveHandler,
	       TouchEndHandler,
               TrackballCallbackInterface

{

    private final Canvas _surface = Canvas.createIfSupported();
    private final GL2 _gl;
    private final Renderer _renderer;
    private final Trackball _trackball;

    public ThreeDRenderingPanel( int width, int height )
        throws Exception

    {

	super( ThreeDKeyPressMessage.class );

	this._gl = GL2ContextHelper.getGL2( this._surface );

	if ( this._gl == null )
	    throw new Exception( "WebGL unsupported." );
	else {

	    this.add( this._surface, 0, 0 );

	    this._trackball = new Trackball( width, height, this );

	    this.addMouseDownHandler( this );
	    this.addMouseWheelHandler( this );
	    this.addTouchStartHandler( this );
	    this.addMouseOverHandler( this );
	    this.addMouseOutHandler( this );

	    this._gl.enable( EnableCap.DEPTH_TEST );
	    this._gl.depthFunc( DepthFunction.LEQUAL );
	    this._gl.clear( ClearBufferMask.COLOR_BUFFER_BIT,
			    ClearBufferMask.DEPTH_BUFFER_BIT );
	    this._gl.clearColor( 0f, 0f, 0f, 1f );
	    this._gl.clearDepth( 1 );
	    this._renderer = new Renderer( this._gl );

            this.setSize( width, height );
	    this.initializeView();

	}

    }

    public final void update()

    {

	this._renderer.translate( this._trans_x, this._trans_y, this._trans_z );

	double halfa = Math.acos( this._quaternion.getR() );
	double a = halfa * 2;
	double sinha = Math.sin( halfa );
	AbstractDoubleVector about =
	    this._quaternion.imag().scalarDivide( sinha );

	this._renderer.rotate( a,
			       about.getComponent( 0 ),
			       about.getComponent( 1 ),
			       about.getComponent( 2 ) );

	this._renderer.update();

    }

    public final void clearAll()

    {

        this._renderer.clearAll();

    }

    public final void addRenderable( Renderable renderable )

    {

	this._renderer.addRenderable( renderable );

    }

    public final void dropRenderable( Renderable renderable )

    {

	this._renderer.dropRenderable( renderable );

    }

    public void setScale( double xscale, double yscale, double zscale )

    {

	this._renderer.setScale( xscale, yscale, zscale );

    }

    public void lookAt( double eyex, double eyey, double eyez,
			double centerx, double centery, double centerz,
			double upx, double upy, double upz )

    {

	this.lookAt( ( float ) eyex, ( float ) eyey, ( float ) eyez,
		     ( float ) centerx, ( float ) centery, ( float ) centerz,
		     ( float ) upx, ( float ) upy, ( float ) upz );

    }

    public void lookAt( float eyex, float eyey, float eyez,
			float centerx, float centery, float centerz,
			float upx, float upy, float upz )

    {

	this._renderer.lookAt( eyex, eyey, eyez,
			       centerx, centery, centerz,
			       upx, upy, upz );

	double dx = centerx - eyex;
	double dy = centery - eyey;
	double dz = centerz - eyez;
	this._delta =
	    Math.sqrt( ( dx * dx ) + ( dy * dy ) + ( dz * dz ) ) / 200;

	this._renderer.init();

    }

    private Quaternion _quaternion =
	Quaternion.createRotationDegrees( new Double3Vector( 0, 1, 0 ), 180 );

    private HandlerRegistration _mouse_move_handler_reg;
    private HandlerRegistration _mouse_up_handler_reg;
    private boolean _shift;
    private boolean _control;

    private int _mouse_x;
    private int _mouse_y;

    @Override
    public void onMouseDown( MouseDownEvent event )

    {

        event.preventDefault();

        this._mouse_move_handler_reg = this.addMouseMoveHandler( this );
        this._mouse_up_handler_reg = this.addMouseUpHandler( this );
        this._shift = event.isShiftKeyDown();
        this._control = event.isControlKeyDown();

        if ( !this._shift || !this._control )
	    this._trackball.initialize( event.getX(), event.getY() );
	else {

	    this._mouse_x = event.getX();
	    this._mouse_y = event.getY();

	}

    }

    @Override
    public void onMouseMove( MouseMoveEvent event )

    {

        event.preventDefault();
        if ( !this._shift || !this._control )
	    this._trackball.move( event.getX(), event.getY(),
				  this._shift, this._control );
	else
	    this.move( event );

    }

    private void move( MouseEvent event )

    {

	this._trans_x -= event.getX() - this._mouse_x;
	this._trans_y -= event.getY() - this._mouse_y;

	this._mouse_x = event.getX();
	this._mouse_y = event.getY();

	this.update();

    }

    @Override
    public void onMouseUp( MouseUpEvent event )

    {

        event.preventDefault();
        if ( !this._shift || !this._control )
	    this._trackball.move( event.getX(), event.getY(),
				  this._shift, this._control );
	else
	    this.move( event );

        this._mouse_move_handler_reg.removeHandler();
        this._mouse_up_handler_reg.removeHandler();

    }

    private double _delta;

    private float _trans_x = 0;
    private float _trans_y = 0;
    private float _trans_z = 0;

    @Override
    public void onMouseWheel( MouseWheelEvent event )

    {

        event.preventDefault();
	this.zBump( event.isNorth() ? -1 : 1, event.isShiftKeyDown() );

    }

    private void zBump( int direction, boolean accelerate )

    {

	this._trans_z +=
	    ( float ) this._delta * direction * ( accelerate ? 5 : 1 );
	this.update();

    }

    private int[] getTouchXY( TouchEvent event )

    {

        LightArray<Touch> touches = event.getChangedTouches();
        Touch t = ( Touch ) touches.get( touches.length() - 1 );

	return new int[] { t.getPageX(), t.getPageY() };

    }

    private HandlerRegistration _touch_move_handler_reg;
    private HandlerRegistration _touch_end_handler_reg;

    @Override
    public void onTouchStart( TouchStartEvent event )

    {

        event.preventDefault();
        this._touch_move_handler_reg = this.addTouchMoveHandler( this );
        this._touch_end_handler_reg = this.addTouchEndHandler( this );

	this._trackball.initialize( this.getTouchXY( event ) );

    }

    @Override
    public void onTouchMove( TouchMoveEvent event )

    {

	event.preventDefault();

        this._trackball.move( this.getTouchXY( event ) );

    }

    @Override
    public void onTouchEnd( TouchEndEvent event )

    {

        event.preventDefault();

        this._trackball.move( this.getTouchXY( event ) );

        this._touch_move_handler_reg.removeHandler();
        this._touch_end_handler_reg.removeHandler();


    }

    private void initializeView()

    {

	this._trans_x = this._trans_y = this._trans_z = 0;
	this._quaternion =
	    Quaternion.createRotationDegrees( new Double3Vector( 0, 1, 0 ),
					      180 );

    }

    public void keyHandler( KeyData kd )

    {

	int nkc = kd.getKeyCode();

	switch ( nkc ) {

	case KeyCodes.KEY_HOME: {

	    this.initializeView();
	    this.update();
	    break;

	}
	case KeyCodes.KEY_RIGHT: {

	    if ( !kd.isControlKeyDown() )
		this.rotate( 0, 1, 0, kd.isShiftKeyDown() ? 5 : 1 );
	    else if ( kd.isControlKeyDown() )
		this.bumpTranslate( -this._delta, 0, kd.isShiftKeyDown() );
	    break;

	}
	case KeyCodes.KEY_LEFT: {

	    if ( !kd.isControlKeyDown() )
		this.rotate( 0, 1, 0, kd.isShiftKeyDown() ? -5 : -1 );
	    else if ( kd.isControlKeyDown() )
		this.bumpTranslate( this._delta, 0, kd.isShiftKeyDown() );
	    break;

	}
	case KeyCodes.KEY_UP: {

	    if ( !kd.isControlKeyDown() )
		this.rotate( 1, 0, 0, kd.isShiftKeyDown() ? 5 : 1 );
	    else if ( kd.isControlKeyDown() )
		this.bumpTranslate( 0, this._delta, kd.isShiftKeyDown() );
	    break;

	}
	case KeyCodes.KEY_DOWN: {

	    if ( !kd.isControlKeyDown() )
		this.rotate( 1, 0, 0, kd.isShiftKeyDown() ? -5 : -1 );
	    else if ( kd.isControlKeyDown() )
		this.bumpTranslate( 0, -this._delta, kd.isShiftKeyDown() );
	    break;

	}
	case KeyCodes.KEY_PAGEDOWN: {

	    this.zBump( 1, kd.isShiftKeyDown() );
	    break;

	}
	case KeyCodes.KEY_PAGEUP: {

	    this.zBump( -1, kd.isShiftKeyDown() );
	    break;

	}

	}

    }

    private void bumpTranslate( double dx, double dy, boolean accelerate )

    {

	this._trans_x += dx * ( accelerate ? 5 : 1 );
	this._trans_y += dy * ( accelerate ? 5 : 1 );

	this.update();

    }

    private void rotate( double x, double y, double z, double r )

    {

	Quaternion q =
	    Quaternion.createRotationDegrees( new Double3Vector( x, y, z ), r );

	this.updateQuaternion( q );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof ThreeDKeyPressMessage )
	    this.keyHandler( ( KeyData ) o );

    }

    /**
     *
     * @param q
     */
    @Override
    public void callback( Quaternion q )
    {

	this.updateQuaternion( q );

    }

    private void updateQuaternion( Quaternion q )

    {

        this._quaternion = q.multiply( this._quaternion ).normalize();
	this.update();

    }

    public final void setSize( int width, int height )

    {

        this._surface.setCoordinateSpaceWidth( width );
        this._surface.setCoordinateSpaceHeight( height );
        this.setWidth( width + "px" );
        this.setHeight( height + "px" );
        this._trackball.setWindowSize( width, height );
	this._gl.viewport( 0, 0, width, height );

    }

    private static class ThreeDMessageFactory
	extends AbstractInteractionMessageFactory

    {

        @Override
        public AbstractActivationMessage activateMessage()
        {

	    return new NoOpActivationMessage();

        }

        @Override
        public Message getMouseDownMessage()
        {

	    return new NoOpMessage();

        }

        @Override
        public Message getMouseUpMessage()
        {

	    return new NoOpMessage();

        }

        @Override
        public Message getMouseMoveMessage()
        {

	    return new NoOpMessage();

        }
	private static class NoOpMessage extends Message {}
	private static class NoOpActivationMessage
	    extends AbstractActivationMessage {}

        @Override
	public Message getKeyPressMessage()

	{

	    return new ThreeDKeyPressMessage();

	}

    }

    private static class ThreeDKeyPressMessage extends Message {}

    @Override
    public void onMouseOver( MouseOverEvent event )

    {

	new PushInteractionFactoryMessage().send( new ThreeDMessageFactory() );

    }

    @Override
    public void onMouseOut( MouseOutEvent event )

    {

	new PopInteractionFactoryMessage().send();

    }

}