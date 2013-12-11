package edu.ucsd.ncmir.WIB.client.core.render3d.renderable.triangle_mesh;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import edu.ucsd.ncmir.WIB.client.core.render3d.renderable.AbstractBufferedRenderable;
import edu.ucsd.ncmir.WIB.client.debug.Debug;
import gwt.g3d.client.gl2.GL2;
import gwt.g3d.client.gl2.enums.BeginMode;
import gwt.g3d.client.shader.AbstractShader;
import gwt.g3d.client.shader.ShaderException;
import gwt.g3d.resources.client.ShaderResource;

/**
 *
 * @author spl
 */
public class TriangleMesh
    extends AbstractBufferedRenderable

{

    private static final int POSITIONS = 0;
    private static final int NORMALS = 1;
    private static final int COLORS = 2;
    private final float[] _positions;
    private final float[] _normals;
    private final float[] _vertex_colors;

    /**
     *
     * @param positions
     * @param normals
     * @param vertex_colors
     */
    public TriangleMesh( float[] positions,
			 float[] normals,
			 float[] vertex_colors )

    {

	super( TriangleMesh.COLORS + 1 );

	this._positions = positions;
	this._normals = normals;
	this._vertex_colors = vertex_colors;

    }

    public TriangleMesh( float[] positions,
			 float[] normals,
			 float r, float g, float b )

    {

	super( TriangleMesh.COLORS + 1 );

	this._positions = positions;
	this._normals = normals;
	this._vertex_colors =
	    this.createVertexColors( r, g, b, positions.length / 3 );

    }

    public TriangleMesh( float[] vertices,
			 float[] vertex_normals,
			 int[] indices,
			 float r, float g, float b )

    {

	super( TriangleMesh.COLORS + 1 );

	this._positions = new float[indices.length * 3];
	this._normals = new float[indices.length * 3];

	this._vertex_colors =
	    this.createVertexColors( r, g, b, indices.length );

	for ( int i = 0, p = 0; i < indices.length; i++, p += 3 ) {

	    int index = indices[i] * 3;

	    this._positions[p + 0] = vertices[index + 0];
	    this._positions[p + 1] = vertices[index + 1];
	    this._positions[p + 2] = vertices[index + 2];

	    this._normals[p + 0] = vertex_normals[index + 0];
	    this._normals[p + 1] = vertex_normals[index + 1];
	    this._normals[p + 2] = vertex_normals[index + 2];

	}

    }

    private float[] createVertexColors( float r, float g, float b, int l )

    {

	float[] vertex_colors = new float[l * 3];

	for ( int i = 0, p = 0; i < l; i++, p += 3 ) {

	    vertex_colors[p + 0] = r;
	    vertex_colors[p + 1] = g;
	    vertex_colors[p + 2] = b;

	}
	return vertex_colors;

    }

    @Override
    protected void attach()

    {

	this.setVbo( this._positions, TriangleMesh.POSITIONS );
	this.setVbo( this._normals, TriangleMesh.NORMALS );
	this.setVbo( this._vertex_colors, TriangleMesh.COLORS );

	this.attachIndex( TriangleMesh.POSITIONS, "aVertexPosition" );
	this.attachIndex( TriangleMesh.NORMALS, "aVertexNormal" );
	this.attachIndex( TriangleMesh.COLORS, "aVertexColor" );

    }

    @Override
    protected AbstractShader getShader( GL2 gl )

    {

	AbstractShader shader = null;
	try {

	    ClientBundleWithLookup client_bundle = Resources.INSTANCE;

            ShaderResource shaderResource =
                ( ShaderResource ) client_bundle.getResource( "shader" );

            shader = shaderResource.createShader( gl );
	    shader.init( gl );

	} catch ( ShaderException e ) {

	    Debug.traceback( e );

	}
	return shader;

    }

    @Override
    protected void drawImpl( GL2 gl )

    {

	this.drawOperation( gl, BeginMode.TRIANGLES );

    }

    protected void drawOperation( GL2 gl, BeginMode begin_mode )

    {

        super.bind();
        gl.drawArrays( begin_mode, 0, this._positions.length / 3 );
        super.unbind();

    }

    /** Resource files. */
    interface Resources
	extends ClientBundleWithLookup

    {

	Resources INSTANCE = GWT.create( Resources.class );

	@Source( { "shaders/vertex_shader.vp", "shaders/fragment_shader.fp" } )
	ShaderResource shader();

    }

}