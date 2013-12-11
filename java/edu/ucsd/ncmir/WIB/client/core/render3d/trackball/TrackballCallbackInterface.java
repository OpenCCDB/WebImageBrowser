package edu.ucsd.ncmir.WIB.client.core.render3d.trackball;

import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;

/**
 *
 * @author spl
 */
public interface TrackballCallbackInterface

{

    void callback( Quaternion q );

}
