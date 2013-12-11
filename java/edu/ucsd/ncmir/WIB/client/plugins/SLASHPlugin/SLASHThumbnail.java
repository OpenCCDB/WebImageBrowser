package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import edu.ucsd.ncmir.WIB.client.core.image.AbstractThumbnail;

/**
 *
 * @author spl
 */
class SLASHThumbnail
    extends AbstractThumbnail
{


    SLASHThumbnail( int depth )

    {

	super( SLASHPlugin.format( depth ) + "/TileGroup0/0-0-0.jpg" );

	Image image = this.getImage();

	image.addLoadHandler( new Resizer( image ) );

    }

    private final class Resizer
	implements LoadHandler

    {

	private final Image _image;

	Resizer( Image image )

	{

	    this._image = image;

	}

	@Override
        public void onLoad( LoadEvent event )
	    
	{
	    
	    int width = this._image.getWidth();
	    int height = this._image.getHeight();
	    
	    double scale = 128.0 / ( ( width > height ) ? width : height );
	    
	    this._image.setWidth( ( int ) Math.ceil( scale * width ) + "px" );
	    this._image.setHeight( ( int ) Math.ceil( scale * height ) + "px" );

	}

    }

}
