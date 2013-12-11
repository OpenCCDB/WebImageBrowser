package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;

/**
 * This message encapsulates all the sundry parameters necessary to
 * display the image tiles and/or traces.
 * @author spl
 */
public class ParameterUpdateMessage
    extends Message

{

    private double _xorg = 0;
    private double _yorg = 0;
    private ScaleFactor _scale_factor = null;
    private int _plane = 0;
    private int _timestep = 0;
    private Quaternion _quaternion = new Quaternion( 1, 0, 0, 0 );

    @Override
    public void send()

    {

        if ( this._scale_factor != null )
            super.send();

    }

    /**
     * @return the xorg
     */

    public double getXOrg()

    {

        return this._xorg;

    }

    /**
     * @return the yOrg
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

        return this._scale_factor;

    }

    /**
     * @return the plane
     */

    public int getPlane()

    {

	return this._plane;

    }

    /**
     * @return the timestep
     */

    public int getTimestep()

    {

	return this._timestep;

    }

    /**
     * @return the quaternion
     */

    public Quaternion getQuaternion()

    {

        return this._quaternion;

    }

    /**
     * Sets the origin.
     * @param xorg the xorg to set
     * @param yorg the yorg to set
     */

    public void setOrigin( double xorg, double yorg )

    {

        this._xorg = xorg;
        this._yorg = yorg;

    }

    /**
     * @param zoom the zoom to set
     */

    public void setZoom( ScaleFactor zoom )

    {

        this._scale_factor = zoom;

    }

    /**
     * @param plane the plane to set
     */

    public void setPlane( int plane )

    {

        this._plane = plane;

    }

    /**
     * @param timestep the timestep to set
     */

    public void setTimestep( int timestep )

    {

        this._timestep = timestep;

    }

    /**
     * @param quaternion the quaternion to set
     */

    public void setQuaternion( Quaternion quaternion )

    {

        this._quaternion = quaternion;

    }

    @Override
    public String toString()

    {

	String s;

	if ( this._scale_factor != null )
	    s = "" +
		this._xorg + " " +
		this._yorg + " " +
		this._scale_factor.getExponent()+ " " +
		this._plane + " " +
		this._timestep + " " +
		this._quaternion;
	else
	    s = "NOT SET";

	return s;

    }

}
