package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class DimensionRequestMessage
    extends Message
{
    
    private int _width;
    private int _height;
    private int _depth;

    public void setWidth( int width )

    {

	this._width = width;

    }

    public void setHeight( int height )

    {

	this._height = height;

    }

    public void setDepth( int depth )

    {

	this._depth = depth;

    }

    public int getWidth()

    {

	return this._width;

    }

    public int getHeight()

    {

	return this._height;

    }

    public int getDepth()

    {

	return this._depth;

    }

}
