package edu.ucsd.ncmir.WIB.client.core.image;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import edu.ucsd.ncmir.WIB.client.core.messages.ImageLoadCompleteMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ImageLoadStartMessage;

/**
 *
 * @author spl
 */
public abstract class AbstractTile
    extends AbstractImage
    implements LoadHandler

{

    /**
     * Instantiates an <code>AbstractTile</code>.
     * @param args
     */
    public AbstractTile( String args )

    {

        super( args, false );

    }

    /**
     *
     * @return The I tile coordinate.
     */
    public abstract int getI();
    /**
     *
     * @return The J tile coordinate.
     */
    public abstract int getJ();

    public abstract void setImageCharacteristics( double contrast,
						  double brightness,
						  boolean red_channel,
						  boolean green_channel,
						  boolean blue_channel,
						  boolean flip_contrast,
						  double tile_scale_factor );

    public abstract void setImageCharacteristics( double contrast,
						  double brightness,
						  boolean red_channel,
						  boolean green_channel,
						  boolean blue_channel,
						  boolean flip_contrast );

    public abstract void handleLoad();

    private boolean _in_flight = false;

    public final boolean realize()

    {

	Image image = this.getImage();
        boolean here = !image.getUrl().equals( "" );
	if ( !here && this.isAttached() ) {

            new ImageLoadStartMessage().send();

	    this._in_flight = true;
	    image.addLoadHandler( this );
	    this.load();

	}
        this.setVisible( true );
	return here;

    }

    @Override
    public final void onLoad( LoadEvent event )

    {

	this.handleLoad();
	if ( this._in_flight ) {

	    new ImageLoadCompleteMessage().send();
	    if ( !this.isVisible() )
		this.removeFromParent();

	}
	this._in_flight = false;

    }

}
