package edu.ucsd.ncmir.WIB.client.core.render3d.renderable.triangle_mesh;

import gwt.g3d.client.gl2.GL2;
import gwt.g3d.client.gl2.enums.BeginMode;

/**
 *
 * @author spl
 */
public class LineSegmentMesh
    extends TriangleMesh

{

    /**
     *
     * @param positions
     * @param normals
     * @param vertex_colors
     */
    public LineSegmentMesh( float[] positions,
	                    float[] normals,
                            float[] vertex_colors )

    {

	super( positions, normals, vertex_colors );

    }

    @Override
    protected void drawImpl( GL2 gl )

    {

	super.drawOperation( gl, BeginMode.LINES );

    }

}
