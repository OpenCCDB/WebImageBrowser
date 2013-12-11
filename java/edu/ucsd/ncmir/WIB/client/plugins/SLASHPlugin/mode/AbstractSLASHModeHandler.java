package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mode;

import edu.ucsd.ncmir.WIB.client.plugins.ModeHandlerInterface;

/**
 *
 * @author spl
 */
public abstract class AbstractSLASHModeHandler
    implements ModeHandlerInterface

{

    public abstract void getContours( double x0, double y0, double z0,
			              double x1, double y1, double z1 );

    public abstract String[] getObjectList();

}
