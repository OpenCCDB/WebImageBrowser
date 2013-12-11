package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.image.AbstractThumbnail;
import edu.ucsd.ncmir.WIB.client.core.image.AbstractTile;
import edu.ucsd.ncmir.WIB.client.core.image.ImageFactoryInterface;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;
import java.util.HashMap;

/**
 *
 * @author spl
 */
class SLASHImageFactory
    implements ImageFactoryInterface

{

    private HashMap<String,SLASHTile> _tiles = new HashMap<String,SLASHTile>();

    @Override
    public AbstractTile getTile( int i, int j, int depth,
				 int timestep,
				 ScaleFactor zoom, Quaternion quaternion )

    {

        int level = zoom.getLevel();

	String index = depth + "." + level + "." + i + "." + j;

	SLASHTile tile = this._tiles.get( index );

	if ( tile == null )
	    this._tiles.put( index,
			     tile = new SLASHTile( level, i, j, depth ) );

        return tile;

    }

    private HashMap<Integer,SLASHThumbnail> _thumbnails =
	new HashMap<Integer,SLASHThumbnail>();

    @Override
    public AbstractThumbnail getThumbnail( Quaternion q,
					   int offset,
					   int timestep )
    {

	SLASHThumbnail thumbnail = this._thumbnails.get( offset );

	if ( thumbnail == null )
	    this._thumbnails.put( offset,
				  thumbnail = new SLASHThumbnail( offset ) );

	return thumbnail;

    }

}
