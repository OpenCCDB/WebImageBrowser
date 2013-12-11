package edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Image;
import edu.ucsd.ncmir.WIB.DefaultWorker.client.CanvasData;
import edu.ucsd.ncmir.WIB.client.core.components.Transform;
import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.image.AbstractTile;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.GetRGBMapMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ImpededModeMessage;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.messages.DefaultTileWorkerMessage;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;

/**
 *
 * @author spl
 */
public class DefaultTile
    extends AbstractTile
    implements LoadHandler,
	       AttachEvent.Handler

{

    static {

	MessageManager.registerListener( DefaultTileWorkerMessage.class,
					 new DefaultTileWorkerListener() );

    }
    private final int _i;
    private final int _j;
    private final int _depth;
    private final Canvas _canvas;
    private final Image _image;
    private double _mag;
    private boolean _loaded;
    private ImageData _id;
    private static boolean _impeded_mode = false;

    public DefaultTile( int i, int j, int depth, int timestep,
			ScaleFactor zoom, Quaternion quaternion )

    {

        super( "tile" +
               "&i=" + i +
               "&j=" + j +
               "&zoom=" + zoom.getZoomExponent() +
               "&depth=" + depth +
               "&timestep=" + timestep +
               quaternion.getFormatted( "&qr=%g&qi=%g&qj=%g&qk=%g" ) );

        this._i = i;
        this._j = j;
        this._depth = depth;

	this._canvas = Canvas.createIfSupported();

        this.add( this._canvas, 0, 0 );
	this.realize();
	this._image = this.getImage();

 	this._image.addLoadHandler( this );

	this._image.setVisible( false );

	this.addAttachHandler( this );

    }

    /**
     * @return the i position.
     */
    @Override
    public int getI()
    {
        return this._i;
    }

    /**
     * @return the j position
     */
    @Override
    public int getJ()
    {
        return this._j;
    }

    @Override
    public String toString()

    {

	return "Tile [i: " + this._i +
	    ", j: " + this._j +
	    ", depth: " + this._depth +
	    "]";

    }

    private int _red_map = 0;
    private int _green_map = 1;
    private int _blue_map = 2;

    private double _contrast = 0;
    private double _brightness = 0;
    private boolean _red_on = true;
    private boolean _green_on = true;
    private boolean _blue_on = true;
    private int _flip_m = 1;
    private int _flip_b = 0;

    private Context2d _scratch_context;
    private CanvasElement _scratch_canvas_element;

    private double _old_contrast = 0;
    private double _old_brightness = 0;
    private boolean _old_red_on = true;
    private boolean _old_green_on = true;
    private boolean _old_blue_on = true;
    private int _old_flip_m = 1;
    private int _old_flip_b = 0;
    private double _old_mag = -1;

    /**
     * Sets the image characteristics.
     * @param contrast The contrast value.  Varies between -1 and 1.
     * @param brightness The brightness value.  Varies between -1 and
     * @param red If <code>true</code> the channel is turned on.
     * @param green If <code>true</code> the channel is turned on.
     * @param blue If <code>true</code> the channel is turned on.
     * @param flip If <code>true</code> the contrast is flipped.
     * Otherwise, contrast is normal.
     * @param mag Image magnification.
     */

    @Override
    public void setImageCharacteristics( double contrast, double brightness,
					 boolean red,
					 boolean green,
					 boolean blue,
					 boolean flip,
					 double mag )

    {

        this._mag = mag;

	this.setImageCharacteristics( contrast, brightness,
				      red, green, blue, flip );

    }

    @Override
    public void setImageCharacteristics( double contrast, double brightness,
					 boolean red,
					 boolean green,
					 boolean blue,
					 boolean flip )

    {

	if ( !DefaultTile._impeded_mode ) {

	    this._contrast = contrast;
	    this._brightness = brightness;

	    this._red_on = red;
	    this._green_on = green;
	    this._blue_on = blue;

	    if ( flip ) {

		this._flip_m = -1;
		this._flip_b = 255;

	    } else {

		this._flip_m = 1;
		this._flip_b = 0;

	    }

	    boolean param_change =
		( this._old_contrast != this._contrast ) ||
		( this._old_brightness != this._brightness ) ||
		!( this._old_red_on && this._red_on ) ||
		!( this._old_green_on && this._green_on ) ||
		!( this._old_blue_on && this._blue_on ) ||
		( this._old_flip_m != this._flip_m ) ||
		( this._old_flip_b != this._flip_b );

	    if ( param_change || ( this._old_mag != this._mag ) ) {

		if ( this._loaded ) {

		    this.updateImageBuffer();
		    this.renderImage();

		}

		this._old_contrast = this._contrast;
		this._old_brightness = this._brightness;
		this._old_red_on = this._red_on;
		this._old_green_on = this._green_on;
		this._old_blue_on = this._blue_on;
		this._old_flip_m = this._flip_m;
		this._old_flip_b = this._flip_b;
		this._old_mag = this._mag;

	    }

	}

    }

    private int[] buildLUT( boolean on, double A, double B )

    {

	int[] lut = new int[256];

	if ( on )
	    for ( int i = 0; i < 256; i++ ) {

		int value = ( i * this._flip_m ) + this._flip_b;

		value = ( int ) ( ( value * A ) + B );
		if ( value < 0 )
		    value = 0;
		else if ( value > 255 )
		    value = 255;

		lut[i] = value;

	    }

	return lut;

    }

    private void renderImage()

    {

	if ( !DefaultTile._impeded_mode )
	    this.render();

    }

    private void updateImageBuffer()

    {

	if ( this._loaded && !DefaultTile._impeded_mode ) {

	    int w = this._image.getWidth();
	    int h = this._image.getHeight();

	    if ( ( this._contrast != 0 ) ||
		 ( this._brightness != 0 ) ||
		 ( this._flip_m != 1 ) ||
		 ( this._flip_b != 0 ) ||
		 !this._red_on ||
		 !this._green_on ||
		 !this._blue_on ) {

		double C = ( ( this._contrast * 255 ) - 1 ) / 256;
		double c = ( C + 1 ) * ( Math.PI / 4 );
		double s = Math.tan( c );
		double b = this._brightness;

		double A;
		double B;

		if ( b > 0 ) {

		    A = s * ( 1 - b );
		    B = 255 * ( ( b * s ) - ( s / 2 ) + 0.5 );

		} else {

		    A = s * ( 1 + b );
		    B = 255 * ( ( -s / 2 ) + 0.5 );

		}

		ImageData id = this._scratch_context.getImageData( 0, 0, w, h );

		CanvasData data = CanvasData.create( this._red_on,
						     this._green_on,
						     this._blue_on,
						     A,
						     B,
						     this._flip_m,
						     this._flip_b,
						     this._red_map,
						     this._green_map,
						     this._blue_map,
						     this._image_pixels,
						     id );

		new DefaultTileWorkerMessage( this, data ).send();

	    } else {

		this._scratch_context.clearRect( 0, 0, w, h );

		this._scratch_context.save();

		this._scratch_context.putImageData( this._id, 0, 0 );
		this._scratch_context.restore();

	    }

	}

    }

    void updateImageData( ImageData image_data )

    {

	int w = this._image.getWidth();
	int h = this._image.getHeight();

	this._scratch_context.clearRect( 0, 0, w, h );

	this._scratch_context.save();

	this._scratch_context.putImageData( image_data, 0, 0 );

	this._scratch_context.restore();
	this.render();

    }

    void render()

    {

	this.clearCanvas();

	Context2d c2d = this._canvas.getContext2d();

	c2d.save();
	c2d.setGlobalAlpha( 255.0 );

	c2d.scale( this._mag, this._mag );

	c2d.drawImage( this._scratch_canvas_element, 0, 0 );
	c2d.restore();

    }


    private void clearCanvas()

    {

	int w = ( int ) ( this._image.getWidth() * this._mag );
	int h = ( int ) ( this._image.getHeight() * this._mag );

	this.setCoordinateSpaceWidth( w );
	this.setCoordinateSpaceHeight( h );

	Context2d c2d = this._canvas.getContext2d();

	c2d.save();

	new Transform().setContextTransform( c2d );

	c2d.clearRect( 0, 0, w, h );

	c2d.restore();

    }

    public void setCoordinateSpaceWidth( int width )

    {

	this.setWidth( width + "px" );
	this._canvas.setCoordinateSpaceWidth( width );

    }

    public void setCoordinateSpaceHeight( int height )

    {

	this.setHeight( height + "px" );
	this._canvas.setCoordinateSpaceHeight( height );

    }

    private CanvasPixelArray _image_pixels;

    @Override
    public void handleLoad()

    {

	this._loaded = true;
	if ( !DefaultTile._impeded_mode ) {

	    int w = this._image.getWidth();
	    int h = this._image.getHeight();

	    Canvas scratch = Canvas.createIfSupported();

	    scratch.setCoordinateSpaceWidth( w );
	    scratch.setCoordinateSpaceHeight( h );

	    Context2d tc2d = scratch.getContext2d();

	    tc2d.drawImage( ImageElement.as( this._image.getElement() ), 0, 0 );

	    try {

		this._id = tc2d.getImageData( 0, 0, w, h );

		this._image_pixels = this._id.getData();

		GetRGBMapMessage grgbmm = new GetRGBMapMessage();

		grgbmm.send();

		this._red_map = grgbmm.getRed();
		this._green_map = grgbmm.getGreen();
		this._blue_map = grgbmm.getBlue();

		Canvas canvas = Canvas.createIfSupported();
		canvas.setCoordinateSpaceWidth( w );
		canvas.setCoordinateSpaceHeight( h );
		this._scratch_context = canvas.getContext2d();

		this._scratch_context.createImageData( w, h );

		this._scratch_canvas_element =
		    ( CanvasElement ) CanvasElement.as( canvas.getElement() );

		if ( this.isVisible() ) {

		    this.updateImageBuffer();
		    this.renderImage();

		}

	    } catch ( Throwable t ) {

		DefaultTile._impeded_mode = true;
		this._image.setVisible( true );
		new ImpededModeMessage().send();

	    }

	} else
	    this._image.setVisible( true );

    }

    @Override
    public void onAttachOrDetach( AttachEvent event )

    {

	if ( this._loaded ) {

	    this.updateImageBuffer();
	    this.renderImage();

	}

    }

}
