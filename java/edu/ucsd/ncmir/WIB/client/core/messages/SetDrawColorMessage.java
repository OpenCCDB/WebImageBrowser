package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class SetDrawColorMessage
    extends Message

{

    private final int _red;
    private final int _green;
    private final int _blue;

    public SetDrawColorMessage( int red, int green, int blue )

    {

        this._red = red;
        this._green = green;
        this._blue = blue;

    }

    /**
     * @return the red component
     */

    public int getRed()

    {

        return this._red;

    }

    /**
     * @return the green component
     */

    public int getGreen()

    {

        return this._green;

    }

    /**
     * @return the blue component
     */

    public int getBlue()

    {

        return this._blue;

    }

}
