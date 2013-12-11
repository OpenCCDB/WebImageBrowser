package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.core.image.AbstractTile;

/**
 *
 * @author spl
 */
class SLASHTile
    extends AbstractTile

{

    private final int _i;
    private final int _j;

    SLASHTile( int level, int i, int j, int depth )

    {

        super( SLASHTile.format( level, i, j, depth ) );

        this._i = i;
        this._j = j;

    }

    private static String format( int level, int i, int j, int depth )

    {

	String d = SLASHPlugin.format( depth );

	return d + "/TileGroup0/" + level + "-" + i + "-" + j + ".jpg";

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
    public void setImageCharacteristics( double contrast,
					 double brightness,
					 boolean red_channel,
					 boolean green_channel,
					 boolean blue_channel,
					 boolean flip_contrast,
					 double tile_scale_factor )
    {

	// does nothing.

    }

    @Override
    public void setImageCharacteristics( double contrast,
					 double brightness,
					 boolean red_channel,
					 boolean green_channel,
					 boolean blue_channel,
					 boolean flip_contrast )
    {

	// does nothing.

    }

    @Override
    public void handleLoad()
    {

        // Does nothing.
        
    }

}
