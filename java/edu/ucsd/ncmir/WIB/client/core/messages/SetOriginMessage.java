package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class SetOriginMessage
    extends Message

{

    private final double _xorg;
    private final double _yorg;

    public SetOriginMessage( double xorg, double yorg )

    {

        this._xorg = xorg;
        this._yorg = yorg;

    }

    /**
     * @return the _xorg
     */
    public double getXOrg()

    {

        return this._xorg;

    }

    /**
     * @return the _yorg
     */
    public double getYOrg()

    {

        return this._yorg;

    }

}
