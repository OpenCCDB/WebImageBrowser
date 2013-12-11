package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class GetRGBMapMessage
    extends Message

{

    private int _red;
    private int _green;
    private int _blue;

    /**
     * @return the red value
     */

    public int getRed()

    {

        return this._red;

    }

    /**
     * @param red the red value to set
     */

    public void setRed( int red )

    {

        this._red = red;

    }

    /**
     * @return the green value
     */

    public int getGreen()

    {

        return this._green;

    }

    /**
     * @param green the green value to set
     */

    public void setGreen( int green )

    {

        this._green = green;

    }

    /**
     * @return the blue value
     */

    public int getBlue()

    {

        return this._blue;

    }

    /**
     * @param blue the blue value to set
     */

    public void setBlue( int blue )

    {

        this._blue = blue;

    }

}
