package edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin;

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
public class DefaultImageFactory
    implements ImageFactoryInterface

{

    private class TimestepThumbnail extends HashMap<Integer,DefaultThumbnail> {}
    private class OffsetTimestep extends HashMap<Integer,TimestepThumbnail> {}
    private class QuaternionOffset extends HashMap<Quaternion,OffsetTimestep> {}

    private QuaternionOffset _directory = new QuaternionOffset();

    @Override
    public AbstractThumbnail getThumbnail( Quaternion q,
					   int offset,
					   int timestep )

    {

	OffsetTimestep ot = this._directory.get( q );

	if ( ot == null )
	    this._directory.put( q, ot = new OffsetTimestep() );

	TimestepThumbnail tt = ot.get( offset );

	if ( tt == null )
	    ot.put( offset, tt = new TimestepThumbnail() );

	DefaultThumbnail t = tt.get( timestep );

	if ( t == null )
	    tt.put( timestep,
		    t = new DefaultThumbnail( q, offset, timestep ) );

	return t;

    }

    private HashMap<String,DefaultTile> _tiles =
	new HashMap<String,DefaultTile>();

    @Override
    public AbstractTile getTile( int i, int j, int depth,
				 int timestep,
				 ScaleFactor zoom, Quaternion quaternion )

    {

	String index = depth + "." + zoom + "." + i + "." + j + "." +
	    timestep + "." + quaternion;

	DefaultTile tile = this._tiles.get( index );

	if ( tile == null )
	    this._tiles.put( index,
			     tile = new DefaultTile( i, j, depth, timestep,
						     zoom, quaternion ) );

        return tile;

    }

}
