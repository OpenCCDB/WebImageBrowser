package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractDialogBox;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractDoubleSpinner;
import edu.ucsd.ncmir.WIB.client.core.components.FilteredDataCallback;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.DisplayInitMessage;
import edu.ucsd.ncmir.WIB.client.core.render3d.ThreeDRenderingPanel;
import edu.ucsd.ncmir.WIB.client.core.render3d.renderable.Renderable;
import edu.ucsd.ncmir.WIB.client.core.render3d.renderable.triangle_mesh.LineSegmentMesh;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.SLASHAnnotationSpinnerInfo;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DimensionRequestMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ThreeDGeometryDeleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ThreeDGeometryUpdateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ToggleView3DStateMessage;
import java.util.HashSet;

/**
 *
 * @author spl
 */
public final class ThreeDDisplayDialog
    extends AbstractDialogBox
    implements FilteredDataCallback,
	       MessageListener

{

    private ThreeDRenderingPanel _render_panel = null;
    private final ZMagSpinner _z_mag_spinner;
    private final Grid _hp = new Grid( 2, 1 );

    public ThreeDDisplayDialog()
        throws Exception

    {

	super( false, false, true, true, true );

	super.addTitle( "3D View" );

	this.add( this._hp );

	int box_x = 250;
	int box_y = 250;
	int panel_width = 512;
	int panel_height = 512;

	double zmag = 1.0;

	if ( Cookies.isCookieEnabled() ) {

            String cookie_prefix = Configuration.getCookiePrefix();

	    String position =
		Cookies.getCookie( cookie_prefix + ".3d_display_position" );

	    if ( position != null ) {

		String[] p = position.split( " " );

		box_x = Integer.parseInt( p[0] );
		box_y = Integer.parseInt( p[1] );
		panel_width = Integer.parseInt( p[2] );
		panel_height = Integer.parseInt( p[3] );

	    }
	    String zmagstr =
		Cookies.getCookie( cookie_prefix + ".z_exaggeration" );
	    if ( zmagstr != null )
		zmag = Double.parseDouble( zmagstr );

	}

	Grid controls = new Grid( 1, 2 );

	this._hp.setWidget( 0, 0, controls );
	this._render_panel =
	    new ThreeDRenderingPanel( panel_width, panel_height );
	this._hp.setWidget( 1, 0, this._render_panel );

	this._z_mag_spinner = new ZMagSpinner( zmag, this );

	controls.setWidget( 0, 0, new Label( "Z exaggeration:" ) );
	controls.setWidget( 0, 1, this._z_mag_spinner );

	super.setPopupPosition( box_x, box_y );

	MessageManager.registerAsListener( this,
					   DimensionRequestMessage.class,
					   DisplayInitMessage.class,
					   ThreeDGeometryDeleteMessage.class,
					   ThreeDGeometryUpdateMessage.class );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof DisplayInitMessage )
	    this.setDimensions( ( DisplayInitMessage ) m );
	else if ( m instanceof ThreeDGeometryUpdateMessage )
	    this.updateGeometry( ( SLASHAnnotationSpinnerInfo ) o );
	else if ( m instanceof ThreeDGeometryDeleteMessage )
	    this.deleteGeometry( ( SLASHAnnotationSpinnerInfo ) o );
	else if ( m instanceof DimensionRequestMessage )
	    this.getDimensions( ( DimensionRequestMessage ) m );

    }

    @Override
    public void onLoad()

    {

	super.onLoad();
	this._initialized = true;
	this.updateRenderer();

    }

    private int _dragmode = -1;

    @Override
    protected void updateCursor( int dragmode )

    {

	this._dragmode = dragmode;

	super.updateCursor( dragmode );

    }

    @Override
    protected void dragResizeWidget( PopupPanel panel, int dx, int dy )

    {

	super.dragResizeWidget( panel, dx, dy );

	int dw = 0;
	int dh = 0;

	switch ( this._dragmode ) {

	case 0: {

	    dw = -dx;
	    dh = -dy;
	    break;

	}
	case 1: {

	    dh = -dy;
	    break;

	}
	case 2: {

	    dw = dx;
	    dh = -dy;
	    break;

	}
	case 3: {

	    dw = -dx;
	    break;

	}
	case 5: {

	    dw = dx;
	    break;

	}
	case 6: {

	    dw = -dx;
	    dh = dy;
	    break;

	}
	case 7: {

	    dh = dy;
	    break;

	}
	case 8: {

	    dw = dx;
	    dh = dy;
	    break;

	}

	}

	int width = this._render_panel.getOffsetWidth() + dw;
	int height = this._render_panel.getOffsetHeight() + dh;

	this._render_panel.setSize( width, height );

	this._render_panel.update();

    }

    @Override
    protected void endDragging( MouseUpEvent event )

    {

	super.endDragging( event );

	if ( Cookies.isCookieEnabled() ) {

            String cookie_prefix = Configuration.getCookiePrefix();

            Cookies.setCookie( cookie_prefix + ".3d_display_position",
			       this.getAbsoluteLeft() + " " +
			       this.getAbsoluteTop() + " " +
			       this._render_panel.getOffsetWidth() + " " +
			       this._render_panel.getOffsetHeight() );

	}

    }

    @Override
    protected void onCloseClick( ClickEvent event )

    {

	new ToggleView3DStateMessage().send();
	super.onCloseClick( event );

    }

    private void getDimensions( DimensionRequestMessage drm )

    {

	drm.setWidth( this._width );
	drm.setHeight( this._height );
	drm.setDepth( this._depth );

    }

    private double _zmag = 1;
    private boolean _initialized = false;

    @Override
    public boolean setValue( double s )

    {

	boolean ok = ( 1 <= s );

	if ( ok ) {

	    this._zmag = s;

	    if ( Cookies.isCookieEnabled() ) {

		String cookie_prefix = Configuration.getCookiePrefix();
		Cookies.setCookie( cookie_prefix + ".z_exaggeration",
				   this._zmag + "" );

	    }

	}

	if ( this._initialized )
	    this.updateRenderer();
	return ok;

    }

    @Override
    public double getValue()
    {

	return this._zmag;

    }

    private static class ZMagSpinner
	extends AbstractDoubleSpinner

    {

	ZMagSpinner( double zmag, ThreeDDisplayDialog tddb )

	{

	    super( zmag, 1, tddb );

	}

    }

    private final HashSet<SLASHAnnotationSpinnerInfo> _geometry_table =
	new HashSet<SLASHAnnotationSpinnerInfo>();

    private void updateGeometry( SLASHAnnotationSpinnerInfo sasi )

    {

	this._geometry_table.add( sasi );
	this.updateRenderer();

    }

    private void deleteGeometry( SLASHAnnotationSpinnerInfo sasi )

    {

	this._geometry_table.remove( sasi );
	this.updateRenderer();

    }

    private int _width;
    private int _height;
    private int _depth;

    private static int parse( String size )

    {

	size = size.replaceAll( "[^0-9]", "" );

	return !size.equals( "" ) ? Integer.parseInt( size ) : 0;

    }

    private void updateRenderer()

    {

        this._render_panel.setScale( 1, 1, this._zmag );
	this._render_panel.clearAll();
	this._render_panel.addRenderable( this.cube() );
	this._render_panel.addRenderable( this.origin() );

	for ( SLASHAnnotationSpinnerInfo sasi : this._geometry_table )
	    if ( sasi.is3DEnabled() ) {

		if ( sasi.isSurfaceEnabled() ) {

		    Renderable[] meshes = sasi.getMeshes();

		    if ( meshes != null )
                        for ( Renderable m : meshes )
                            this._render_panel.addRenderable( m );

		} else
		    for ( Renderable m : sasi.getGeometry() )
			this._render_panel.addRenderable( m );

	    }

        this._render_panel.update();

    }

    private void setDimensions( DisplayInitMessage dim )

    {

	this._width = dim.getWidth();
	this._height = dim.getHeight();
	this._depth = dim.getDepth();

	double size =
	    Math.sqrt( ( this._width * this._width ) +
		       ( this._height * this._height ) +
		       ( this._depth * this._depth ) );

	float xc = ( float ) ( ( this._width - 1 ) / 2.0 );
	float yc = ( float ) ( ( this._height - 1 ) / 2.0 );
	float zc = ( float ) ( ( this._depth - 1 ) / 2.0 );

	this._render_panel.lookAt( xc, yc, zc - ( size * 1.5 ),
				   xc, yc, zc,
				   0, 1, 0 );

    }

    private LineSegmentMesh cube()

    {

	float[] x = { 0, this._width - 1 };
	float[] y = { 0, this._height - 1 };
	float[] z = { 0, this._depth - 1 };

	Triplets t = new Triplets( 24 );

	t.add( x[0], y[0], z[0] );
	t.add( x[0], y[0], z[1] );
	t.add( x[0], y[0], z[0] );
	t.add( x[0], y[1], z[0] );
	t.add( x[0], y[0], z[0] );
	t.add( x[1], y[0], z[0] );
	t.add( x[0], y[0], z[1] );
	t.add( x[0], y[1], z[1] );
	t.add( x[0], y[0], z[1] );
	t.add( x[1], y[0], z[1] );
	t.add( x[0], y[1], z[0] );
	t.add( x[0], y[1], z[1] );
	t.add( x[0], y[1], z[0] );
	t.add( x[1], y[1], z[0] );
	t.add( x[0], y[1], z[1] );
	t.add( x[1], y[1], z[1] );
	t.add( x[1], y[0], z[0] );
	t.add( x[1], y[0], z[1] );
	t.add( x[1], y[0], z[0] );
	t.add( x[1], y[1], z[0] );
	t.add( x[1], y[0], z[1] );
	t.add( x[1], y[1], z[1] );
	t.add( x[1], y[1], z[0] );
	t.add( x[1], y[1], z[1] );

	Triplets n = new Triplets( 24 );

	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );

	Triplets c = new Triplets( 24 );

	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );
	c.add( 1, 0, 0 );

	return new LineSegmentMesh( t.array(), n.array(), c.array() );

    }

    private static class Triplets

    {

	private int _i;
	private final float[] _array;

	Triplets( int n )

	{

	    this._array = new float[n * 3];
	    this._i = 0;

	}

	void add( float x, float y, float z )

	{

	    this._array[this._i++] = x;
	    this._array[this._i++] = y;
	    this._array[this._i++] = z;

	}

	float[] array()

	{

	    return this._array;

	}

    }

    private LineSegmentMesh origin()

    {

	float xc = ( float ) ( ( this._width - 1 ) / 2.0 );
	float yc = ( float ) ( ( this._height - 1 ) / 2.0 );
	float zc = ( float ) ( ( this._depth - 1 ) / 2.0 );

	float[] x = { xc, this._width - 1 };
	float[] y = { yc, this._height - 1 };
	float[] z = { zc, this._depth - 1 };

	Triplets t = new Triplets( 6 );

	t.add( x[0], y[0], z[0] );
	t.add( x[1], y[0], z[0] );
	t.add( x[0], y[0], z[0] );
	t.add( x[0], y[1], z[0] );
	t.add( x[0], y[0], z[0] );
	t.add( x[0], y[0], z[1] );

	Triplets n = new Triplets( 6 );

	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );
	n.add( 0, 0, 0 );

	Triplets c = new Triplets( 6 );

	c.add( 1, 1, 1 );
	c.add( 1, 0, 0 );
	c.add( 1, 1, 1 );
	c.add( 0, 1, 0 );
	c.add( 1, 1, 1 );
	c.add( 0, 0, 1 );

	return new LineSegmentMesh( t.array(), n.array(), c.array() );

    }

}