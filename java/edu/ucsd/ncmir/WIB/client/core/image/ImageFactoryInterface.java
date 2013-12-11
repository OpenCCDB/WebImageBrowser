package edu.ucsd.ncmir.WIB.client.core.image;

import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;

/**
 *
 * @author spl
 */
public interface ImageFactoryInterface

{

    /**
     * Returns a thumbnail image.
     * @param q Quaternion expressing the rotation of the underlying volume.
     * @param offset The Z offset
     * @param timestep The timestep of the image or volume.
     * @return An <code>AbstractThumbnail</code> object.
     */

    public AbstractThumbnail getThumbnail( Quaternion q,
					   int offset,
					   int timestep );

    /**
     * Returns a <code>AbstractTile</code> object.
     *
     * @param i The i location in the tile grid.
     * @param j The j location in the tile grid.
     * @param depth The depth of the image in the volume.
     * @param timestep The timestep of the image or volume.
     * @param zoom The zoom level of time image.
     * @param quaternion A <code>Quaternion</code> describing the
     * orientation of the plane.
     * @return An <code>AbstractTile</code> object to be rendered.
     */

    public AbstractTile getTile( int i, int j, int depth,
				 int timestep,
				 ScaleFactor zoom, Quaternion quaternion );

}
