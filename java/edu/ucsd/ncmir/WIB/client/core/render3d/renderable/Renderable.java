package edu.ucsd.ncmir.WIB.client.core.render3d.renderable;

import gwt.g3d.client.gl2.GL2;
import gwt.g3d.client.gl2.GLDisposable;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;

/**
 *
 * @author spl
 */

public interface Renderable
    extends GLDisposable

{

    public void init( GL2 gl );
    public void setLightPosition( float x, float y, float z );
    public void setEyePosition( float x, float y, float z );
    public void draw( Matrix4f projection_matrix,
		      Matrix4f modelview_matrix,
                      Matrix3f normal_matrix );
    
}