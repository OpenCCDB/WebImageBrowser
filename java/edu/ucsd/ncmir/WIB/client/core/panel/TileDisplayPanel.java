package edu.ucsd.ncmir.WIB.client.core.panel;

import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.image.AbstractTile;
import edu.ucsd.ncmir.WIB.client.core.image.ImageFactoryInterface;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.BrightnessMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ContrastMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DimensionsMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ImageLoadCompleteMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ImageLoadStartMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ParameterUpdateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ResetMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetQuaternionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleBlueChannelMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleContrastMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleGreenChannelMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleRedChannelMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.UpdateOriginMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.UpdateViewMessage;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author spl
 */

public class TileDisplayPanel
    extends AbstractWIBPanel

{

    /**
     * The main display panel.
     */
    public TileDisplayPanel()

    {

	super( BrightnessMessage.class,
               ContrastMessage.class,
               DimensionsMessage.class,
	       ImageLoadCompleteMessage.class,
	       ImageLoadStartMessage.class,
               ParameterUpdateMessage.class,
               ResetMessage.class,
               SetQuaternionMessage.class,
               ToggleBlueChannelMessage.class,
               ToggleContrastMessage.class,
               ToggleGreenChannelMessage.class,
               ToggleRedChannelMessage.class,
               UpdateOriginMessage.class );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof ParameterUpdateMessage )
	    this.updateDisplay( ( ParameterUpdateMessage ) m );
	else if ( m instanceof UpdateOriginMessage ) {

	    UpdateOriginMessage uom = ( UpdateOriginMessage ) m;

	    this.positionTiles( uom.getXOrg(), uom.getYOrg(), uom.getZoom() );

	} else if ( m instanceof ContrastMessage )
	    this.setContrast( ( ( Double ) o ).doubleValue() );
	else if ( m instanceof BrightnessMessage )
	    this.setBrightness( ( ( Double ) o ).doubleValue() );
        else if ( m instanceof ToggleRedChannelMessage )
	    this.redChannel( ( ( ToggleRedChannelMessage ) m ).selected() );
	else if ( m instanceof ToggleGreenChannelMessage )
	    this.greenChannel( ( ( ToggleGreenChannelMessage ) m ).selected() );
	else if ( m instanceof ToggleBlueChannelMessage )
	    this.blueChannel( ( ( ToggleBlueChannelMessage ) m ).selected() );
	else if ( m instanceof ToggleContrastMessage )
	    this.flipContrast( ( ( ToggleContrastMessage ) m ).selected() );
	else if ( m instanceof ResetMessage )
	    this.reset();
	else if ( m instanceof DimensionsMessage )
	    this.dimensions( ( DimensionsMessage ) m );
	else if ( m instanceof ImageLoadCompleteMessage )
	    this.imageLoadComplete();
	else if ( m instanceof ImageLoadStartMessage )
	    this.imageLoadStart();

    }

    private int _tilesize;
    private int _width;
    private int _height;

    private void dimensions( DimensionsMessage dm )

    {

	this._tilesize = dm.getTilesize();
	this._width = dm.getWidth();
	this._height = dm.getHeight();

    }

    private double _contrast = 0.0;
    private double _brightness = 0.0;

    private boolean _red_channel = true;
    private boolean _green_channel = true;
    private boolean _blue_channel = true;

    private boolean _flip_contrast = false;

    private void reset()

    {

	this._flip_contrast = false;
	this._red_channel = this._green_channel = this._blue_channel = true;
	this.updateAll();

    }

    private int _in_flight = 0;

    private Stack<AbstractTile> _tile_queue = new Stack<AbstractTile>();

    private void imageLoadComplete()

    {

	this._in_flight--;
	this.realizeTiles();

    }

    private void realizeTiles()

    {

	while ( !this._tile_queue.empty() && ( this._in_flight < 4 ) )
	    this._tile_queue.pop().realize();

    }

    private void imageLoadStart()

    {

	this._in_flight++;

    }

    private void updateDisplay( ParameterUpdateMessage pum )

    {

	ScaleFactor zoom = pum.getZoom();

	double scale = zoom.getScale();
	double tile_scale_factor = zoom.getTileScaleFactor();

	double hunk = zoom.getScaledTilesize( this._tilesize );

	int wwidth = this.getOffsetWidth();
	int wheight = this.getOffsetHeight();

	double swwidth = wwidth / scale;
	double swheight = wheight / scale;

	int n_xtiles = ( int ) Math.ceil( this._width / hunk );
	int n_ytiles = ( int ) Math.ceil( this._height / hunk );

	double slop = hunk * 2;

	// This chicanery is mostly because of Opera, which seems to
	// execute a clear operation instantly, rather than queue the
	// operations, causing the display to flash in an annoying
	// manner.

	// We first make a list of the existing Tiles on the display,
	// then add any new tiles required, removing from the list any
	// tiles which stay displayed.  Once we've done that, we
	// remove any tiles which are no longer visible, those being
	// the tiles left in the ArrayList after all the tiles are
	// arranged.

	ArrayList<AbstractTile> tiles = new ArrayList<AbstractTile>();

	for ( int i = 0; i < this.getWidgetCount(); i++ )
	    tiles.add( ( AbstractTile ) this.getWidget( i ) );

	double xorg = pum.getXOrg();
	double yorg = pum.getYOrg();
	int plane = pum.getPlane();
	int timestep = pum.getTimestep();

	double x0 = xorg - slop;
	double x1 = xorg + swwidth + slop;

	double y0 = yorg - slop;
	double y1 = yorg + swheight + slop;

	int i0 = ( int ) Math.floor( x0 / hunk );
	int i1 = ( int ) Math.floor( x1 / hunk );

	if ( i0 < 0 )
	    i0 = 0;
	else if ( i0 >= n_xtiles )
	    i0 = n_xtiles - 1;
	if ( i1 < 1 )
	    i1 = 1;
	else if ( i1 >= n_xtiles )
	    i1 = n_xtiles - 1;

	int j0 = ( int ) Math.floor( y0 / hunk );
	int j1 = ( int ) Math.floor( y1 / hunk );

	if ( j0 < 0 )
	    j0 = 0;
	else if ( j0 >= n_ytiles )
	    j0 = n_ytiles - 1;
	if ( j1 < 1 )
	    j1 = 1;
	else if ( j1 >= n_ytiles )
	    j1 = n_ytiles - 1;

        ImageFactoryInterface ifact = Configuration.getPlugin().getImageFactory();
        Quaternion q = pum.getQuaternion();

	this._tile_queue = new Stack<AbstractTile>();

	for ( int i = i0; i <= i1; i++ )
	    for ( int j = j0; j <= j1; j++ ) {

		AbstractTile t =
                    ifact.getTile( i, j, plane, timestep, zoom, q );

		t.setImageCharacteristics( this._contrast,
					   this._brightness,
					   this._red_channel,
					   this._green_channel,
					   this._blue_channel,
					   this._flip_contrast,
					   tile_scale_factor );

		this._tile_queue.push( t );
		if ( !t.isAttached() )
		    this.add( t );
		else
		    tiles.remove( t );

	    }

	// Realize a flight's worth of tiles.

	this.realizeTiles();

	// // Clean up any tiles no longer visible.

	for ( AbstractTile t : tiles )
	    t.setVisible( false );

	// Now position the tiles per the origin, magnification, and
	// relative location.

	this.positionTiles( xorg, yorg, zoom );

    }

    private void updateAll()

    {

	for ( int i = 0; i < this.getWidgetCount(); i++ ) {

	    AbstractTile t = ( AbstractTile ) this.getWidget( i );

	    t.setImageCharacteristics( this._contrast,
				       this._brightness,
				       this._red_channel,
				       this._green_channel,
				       this._blue_channel,
				       this._flip_contrast );

	}

    }


    private void positionTiles( double xorg, double yorg, ScaleFactor zoom )

    {

        new UpdateViewMessage().send();
	int z = zoom.getZoomExponent();

	double scale = zoom.getScale();
	double mag = zoom.getTileScaleFactor();

	int tile_offset = ( int ) Math.floor( this._tilesize * mag );

	int x0 = ( int ) Math.floor( xorg * scale );
	int y0 = ( int ) Math.floor( yorg * scale );

	for ( int t = 0; t < this.getWidgetCount(); t++ ) {

	    AbstractTile tile = ( AbstractTile ) this.getWidget( t );

	    int i = tile.getI();
	    int j = tile.getJ();

	    this.setWidgetPosition( tile,
				    ( i * tile_offset ) - x0,
				    ( j * tile_offset ) - y0 );

	}

    }

    private void setContrast( double contrast )

    {

	this._contrast = contrast;
	this.updateAll();

    }

    private void setBrightness( double brightness )

    {

	this._brightness = brightness;
	this.updateAll();

    }

    private void redChannel( boolean selected )

    {

	this._red_channel = selected;
	this.updateAll();

    }

    private void greenChannel( boolean selected )

    {

	this._green_channel = selected;
	this.updateAll();

    }

    private void blueChannel( boolean selected )

    {

	this._blue_channel = selected;
	this.updateAll();

    }

    private void flipContrast( boolean selected )
    {

        this._flip_contrast = selected;
	this.updateAll();

    }

}
