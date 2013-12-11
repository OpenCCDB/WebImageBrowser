package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class RGBChannelMapMessage
    extends Message

{

    private final int _red;
    private final int _green;
    private final int _blue;

    public RGBChannelMapMessage( int red, int green, int blue )

    {

	this._red = red;
	this._green = green;
	this._blue = blue;

    }

    /**
     * @return The red channel mapping.
     */

    public int getRedMapping()

    {

        return this._red;

    }

    /**
     * @return The green channel mapping.
     */

    public int getGreenMapping()

    {

        return this._green;

    }

    /**
     * @return The blue channel mapping.
     */

    public int getBlueMapping()

    {

        return this._blue;

    }

}
