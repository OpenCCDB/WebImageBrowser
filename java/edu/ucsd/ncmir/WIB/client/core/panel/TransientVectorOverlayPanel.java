package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.canvas.dom.client.Context2d;
import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.ClearTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ParameterUpdateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderTransientVectorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetTransientVectorOverlayLineWidthMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.UpdateOriginMessage;

/**
 * @author spl
 */
public class TransientVectorOverlayPanel
    extends AbstractCanvasPanel

{

    private final Context2d _c2d;

    public TransientVectorOverlayPanel()

    {

	super( ClearTransientVectorOverlayMessage.class,
	       ParameterUpdateMessage.class,
	       RenderTransientVectorOverlayMessage.class,
	       SetTransientVectorOverlayLineWidthMessage.class,
	       UpdateOriginMessage.class );

	this._c2d = this.getContext2d();

        this.setVisible( false );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof ClearTransientVectorOverlayMessage )
	    this.clearVectorOverlay();
	else if ( m instanceof ParameterUpdateMessage )
	    this.update( ( ParameterUpdateMessage ) m );
	else if ( m instanceof RenderTransientVectorOverlayMessage )
	    this.renderVectorOverlay( ( Drawable[] ) o );
	else if ( m instanceof UpdateOriginMessage )
	    this.updateOrigin( ( UpdateOriginMessage ) m );
	else if ( m instanceof SetTransientVectorOverlayLineWidthMessage )
	    this._line_width = ( ( Integer ) o ).intValue();

    }

    private Drawable[] _display_list = new Drawable[0];

    private void clearVectorOverlay()

    {

	this._display_list = new Drawable[0];
        this.setVisible( false );

    }

    private void updateOrigin( UpdateOriginMessage uom )

    {

	this._xorg = uom.getXOrg();
	this._yorg = uom.getYOrg();
	this._zoom = uom.getZoom();

	this.renderDisplayList();

    }

    private ScaleFactor _zoom;
    private double _xorg;
    private double _yorg;

    private void update( ParameterUpdateMessage pum )

    {

	this._zoom = pum.getZoom();
	this._xorg = pum.getXOrg();
	this._yorg = pum.getYOrg();

	this.renderDisplayList();

    }

    private void renderVectorOverlay( Drawable[] display_list )

    {

	this._display_list = display_list;
	this.renderDisplayList();

    }

    private int _line_width = 5;

    private void renderDisplayList()

    {

	if ( this._zoom != null ) {

	    this.setVisible( true );

	    double scale = this._zoom.getScale();

	    int ww = this.getOffsetWidth();
	    int wh = this.getOffsetHeight();

	    this._c2d.save();
	    this._c2d.setLineCap( Context2d.LineCap.ROUND );
	    this._c2d.setLineJoin( Context2d.LineJoin.ROUND );
	    this._c2d.clearRect( 0, 0, ww, wh );

	    this._c2d.scale( scale, scale );
	    this._c2d.translate( -this._xorg, -this._yorg );

	    double increment = 1 / scale;

	    double line_width = this._line_width / scale;
	    double standard_line_width = 1.5 / scale;

	    for ( Drawable p : this._display_list ) {

		p.render( this._c2d, increment, line_width, 0.25, true );
		p.render( this._c2d, increment, standard_line_width, 1, true );

	    }
	    this._c2d.restore();

	}

    }

    @Override
    protected void redraw()

    {

        this.renderDisplayList();

    }

}
