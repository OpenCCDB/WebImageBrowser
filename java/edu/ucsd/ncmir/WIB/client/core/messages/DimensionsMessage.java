package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class DimensionsMessage
    extends Message

{

    private final int _width;
    private final int _height;
    private final int _tilesize;

    public DimensionsMessage( int width, int height, int tilesize )

    {

        this._width = width;
        this._height = height;
        this._tilesize = tilesize;

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
     * @return the tilesize
     */
    public int getTilesize()
    {
        return this._tilesize;
    }

}
