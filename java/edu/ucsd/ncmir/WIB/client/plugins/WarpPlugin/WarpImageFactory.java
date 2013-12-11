package edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin;

import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.image.AbstractThumbnail;
import edu.ucsd.ncmir.WIB.client.core.image.AbstractTile;
import edu.ucsd.ncmir.WIB.client.core.image.ImageFactoryInterface;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.RedrawMessage;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.DefaultThumbnail;
import edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin.DefaultTile;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.ImageSelectionMessage;
import java.util.HashMap;

/**
 *
 * @author spl
 */
class WarpImageFactory
    implements ImageFactoryInterface,
	       MessageListener

{

    WarpImageFactory( String path )

    {

	this.setImagePath( path );
	MessageManager.registerListener( ImageSelectionMessage.class, this );

    }

    private ImageMap _data_map = new ImageMap();

    @Override
    public void action( Message m, Object o )

    {

	this.setImagePath( ( String ) o );
        new RedrawMessage().send();

    }

    private ImageMapper _maps = null;

    private void setImagePath( String path )

    {

	if ( !this._data_map.containsKey( path ) )
	    this._data_map.put( path, new ImageMapper() );

	this._maps = this._data_map.get( path );

    }

    @Override
    public AbstractThumbnail getThumbnail( Quaternion q,
					   int offset,
                                           int timestep )
    {

	return this._maps.getThumbnail( q, offset, timestep );

    }

    @Override
    public AbstractTile getTile( int i, int j,
				 int depth, int timestep,
                                 ScaleFactor zoom, Quaternion quaternion )
    {

	TileMap tiles = this._maps.getTileMap();

	String index = depth + "." + zoom + "." + i + "." + j + "." +
	    timestep + "." + quaternion;

	DefaultTile tile = tiles.get( index );

	if ( tile == null )
	    tiles.put( index,
		       tile = new DefaultTile( i, j, depth, timestep,
					       zoom, quaternion ) );

        return tile;

    }

    private static class ImageMap extends HashMap<String,ImageMapper> {}

    private static class ImageMapper

    {

	private final TileMap _tile_map = new TileMap();
	private DefaultThumbnail _thumbnail = null;

	TileMap getTileMap()

	{

	    return this._tile_map;

	}

	DefaultThumbnail getThumbnail( Quaternion q, int offset, int timestep )

	{

	    if ( this._thumbnail == null )
		this._thumbnail =
		    new DefaultThumbnail( q, offset, timestep );

	    return this._thumbnail;

	}

    }

    private static class TileMap extends HashMap<String,DefaultTile> {}

}
