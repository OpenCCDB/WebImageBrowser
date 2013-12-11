package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class UpdateOriginMessage
    extends Message

{

    private final double _xorg;
    private final double _yorg;
    private final ScaleFactor _zoom;

    public UpdateOriginMessage( double xorg, double yorg, ScaleFactor zoom )

    {

	this._xorg = xorg;
	this._yorg = yorg;
	this._zoom = zoom;

    }

    /**
     * @return the xorg
     */

    public double getXOrg()

    {

        return this._xorg;

    }

    /**
     * @return the yorg
     */

    public double getYOrg()

    {

        return this._yorg;

    }

    /**
     * @return the zoom
     */

    public ScaleFactor getZoom()

    {

        return this._zoom;

    }

}
