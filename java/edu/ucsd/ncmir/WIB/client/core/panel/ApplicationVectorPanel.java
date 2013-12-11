package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.canvas.dom.client.Context2d;
import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.GetDisplayListMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ParameterUpdateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.RenderRequestMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.UpdateOriginMessage;
import java.util.ArrayList;

/**
 * Displays vector graphics.
 * @author spl
 */
public class ApplicationVectorPanel
    extends AbstractCanvasPanel

{

    public ApplicationVectorPanel()

    {

	super( RenderRequestMessage.class,
	       ParameterUpdateMessage.class,
	       UpdateOriginMessage.class );

    }

    @Override
    public void action( Message m, Object o )

    {


	if ( m instanceof ParameterUpdateMessage )
	    this.update( ( ParameterUpdateMessage ) m );
	else if ( m instanceof RenderRequestMessage )
	    this.render();
	else if ( m instanceof UpdateOriginMessage )
	    this.updateOrigin( ( UpdateOriginMessage ) m );

    }

    private ScaleFactor _zoom = null;
    private double _xorg;
    private double _yorg;

    private void update( ParameterUpdateMessage pum )

    {

	this._zoom = pum.getZoom();
	this._xorg = pum.getXOrg();
	this._yorg = pum.getYOrg();

	this.render();

    }

    private void updateOrigin( UpdateOriginMessage uom )

    {

	this._xorg = uom.getXOrg();
	this._yorg = uom.getYOrg();
	this._zoom = uom.getZoom();

	this.renderDisplayList();

    }

    private void render()

    {

	this.updateDisplayList();
	this.renderDisplayList();

    }

    private ArrayList<Drawable> _display_list = new ArrayList<Drawable>();

    private void updateDisplayList()

    {

	if ( this._zoom != null ) {

	    double scale = this._zoom.getScale();

	    int ww = this.getOffsetWidth();
	    int wh = this.getOffsetHeight();

	    double width = ww / scale;
	    double height = wh / scale;

	    GetDisplayListMessage gdlm =
		new GetDisplayListMessage( this._xorg,
					   this._yorg,
					   this._xorg + width,
					   this._yorg + height );

	    gdlm.send();

	    this._display_list = gdlm.getDisplayList();

	}

    }

    private void renderDisplayList()

    {

	if ( this._zoom != null ) {

	    Context2d c2d = this.getContext2d();

	    this.setVisible( true );
	    double scale = this._zoom.getScale();

	    int ww = this.getOffsetWidth();
	    int wh = this.getOffsetHeight();

	    c2d.save();
	    c2d.clearRect( 0, 0, ww, wh );

	    c2d.scale( scale, scale );
	    c2d.translate( -this._xorg, -this._yorg );

	    double increment = 1 / scale;

	    double linewidth = 1.5 / scale;
	    for ( Drawable p : this._display_list )
		p.render( c2d, increment, linewidth, 1.0, false );

	    c2d.restore();

	}

    }

    @Override
    protected void redraw()
    {

        this.renderDisplayList();

    }

}
