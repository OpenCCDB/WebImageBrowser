package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.spl.LinearAlgebra.Matrix2D;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class DisplayInitMessage
    extends Message

{

    private final int _width;
    private final int _height;
    private final int _depth;
    private final int _tilesize;
    private final int _timesteps;
    private final String _atlas_name;
    private final int _page;
    private final Matrix2D _matrix;
    private final int _red_map;
    private final int _green_map;
    private final int _blue_map;

    public DisplayInitMessage( int width, int height, int depth,
			       int tilesize,
			       int timesteps,
			       String atlas_name, int page,
			       Matrix2D matrix,
			       int red_map, int green_map, int blue_map )

    {

	this._width = width;
	this._height = height;
	this._depth = depth;
	this._tilesize = tilesize;
	this._timesteps = timesteps;
	this._atlas_name = atlas_name;
	this._page = page;
	this._matrix = matrix;
	this._red_map = red_map;
	this._green_map = green_map;
	this._blue_map = blue_map;

    }

    /**
     * @return the width
     */

    public int getWidth()

    {

        return this._width;

    }

    /**
     * @return the height
     */

    public int getHeight()

    {

        return this._height;

    }

    /**
     * @return the depth
     */

    public int getDepth()

    {

        return this._depth;

    }

    /**
     * @return the tilesize
     */

    public int getTilesize()

    {

        return this._tilesize;

    }

    /**
     * @return the timesteps
     */

    public int getTimesteps()

    {

        return this._timesteps;

    }

    /**
     * @return the atlas name
     */

    public String getAtlasName()

    {

        return this._atlas_name;

    }

    /**
     * @return the atlas page
     */

    public int getAtlasPage()
    {


	return this._page;

    }

    /**
     * @return the matrix
     */

    public Matrix2D getMatrix()

    {

        return this._matrix;

    }

    /**
     * @return the red map
     */

    public int getRedMap()

    {

        return this._red_map;

    }

    /**
     * @return the green map
     */

    public int getGreenMap()

    {

        return this._green_map;

    }

    /**
     * @return the blue map
     */

    public int getBlueMap()

    {

        return this._blue_map;

    }

}
