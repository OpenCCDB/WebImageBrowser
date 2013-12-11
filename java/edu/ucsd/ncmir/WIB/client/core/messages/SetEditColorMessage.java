package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class SetEditColorMessage
    extends Message

{
    private final int _red;
    private final int _green;
    private final int _blue;

    /**
     *
     * @param red
     * @param green
     * @param blue
     */
    public SetEditColorMessage( int red, int green, int blue )

    {

        this._red = red;
        this._green = green;
        this._blue = blue;

    }

    /**
     * @return the _red
     */
    public int getRed()
    {
        return _red;
    }

    /**
     * @return the _green
     */
    public int getGreen()
    {
        return _green;
    }

    /**
     * @return the _blue
     */
    public int getBlue()
    {
        return _blue;
    }

}