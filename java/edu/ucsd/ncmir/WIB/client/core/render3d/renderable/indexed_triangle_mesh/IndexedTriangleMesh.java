package edu.ucsd.ncmir.WIB.client.core.render3d.renderable.indexed_triangle_mesh;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import edu.ucsd.ncmir.WIB.client.core.render3d.renderable.AbstractRenderable;
import edu.ucsd.ncmir.WIB.client.debug.Debug;
import gwt.g3d.client.gl2.GL2;
import gwt.g3d.client.gl2.WebGLBuffer;
import gwt.g3d.client.gl2.array.Float32Array;
import gwt.g3d.client.gl2.array.Uint16Array;
import gwt.g3d.client.gl2.enums.BeginMode;
import gwt.g3d.client.gl2.enums.BufferTarget;
import gwt.g3d.client.gl2.enums.BufferUsage;
import gwt.g3d.client.gl2.enums.DataType;
import gwt.g3d.client.gl2.enums.DrawElementsType;
import gwt.g3d.client.shader.AbstractShader;
import gwt.g3d.client.shader.ShaderException;
import gwt.g3d.resources.client.ShaderResource;
import javax.vecmath.Vector4f;

/**
 *
 * @author spl
 */
public class IndexedTriangleMesh
    extends AbstractRenderable

{

    private final Float32Array _vertices;
    private final Float32Array _normals;
    private final Uint16Array _indices;
    private final float _red;
    private final float _green;
    private final float _blue;

    public IndexedTriangleMesh( float[] vertices,
				float[] normals,
				int[] indices,
				float red, float green, float blue )

    {

	this( Float32Array.create( vertices ),
	      Float32Array.create( normals ),
	      Uint16Array.create( indices ),
	      red, green, blue );

    }

    public IndexedTriangleMesh( Float32Array vertices, 
				Float32Array normals, 
				Uint16Array indices, 
				float red, float green, float blue )

    {

	this._vertices = vertices;
	this._normals = normals;
	this._indices = indices;
	this._red = red;
	this._green = green;
	this._blue = blue;

    }

    private WebGLBuffer _vertex_buffer;
    private WebGLBuffer _normals_buffer;
    private WebGLBuffer _indices_buffer;
    private AbstractShader _shader;

    @Override
    protected AbstractShader getShader( GL2 gl )

    {

	AbstractShader shader = null;
	try {

	    ClientBundleWithLookup client_bundle = Resources.INSTANCE;

            ShaderResource shaderResource =
                ( ShaderResource ) client_bundle.getResource( "shader" );

            this._shader = shader = shaderResource.createShader( gl );
	    shader.init( gl );

	    this._vertex_buffer = gl.createBuffer();

	    gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this._vertex_buffer );
	    gl.bufferData( BufferTarget.ARRAY_BUFFER,
			   this._vertices,
			   BufferUsage.STATIC_DRAW );

	    this._normals_buffer = gl.createBuffer();

	    gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this._normals_buffer );
	    gl.bufferData( BufferTarget.ARRAY_BUFFER,
			   this._normals,
			   BufferUsage.STATIC_DRAW );

	    this._indices_buffer = gl.createBuffer();

	    gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER,
			   this._indices_buffer );
	    gl.bufferData( BufferTarget.ELEMENT_ARRAY_BUFFER,
			   this._indices,
			   BufferUsage.STATIC_DRAW );

	} catch ( ShaderException e ) {

	    Debug.traceback( e );

	}
	return shader;

    }

    @Override
    public void dispose()
    {

    }

    /** Resource files. */
    interface Resources
	extends ClientBundleWithLookup

    {

	Resources INSTANCE = GWT.create( Resources.class );

	@Source( { "shaders/vertex_shader.vp", "shaders/fragment_shader.fp" } )
	ShaderResource shader();

    }

    @Override
    protected void attach()
    {

    }

    @Override
    protected void drawImpl( GL2 gl )

    {

        gl.uniform( this._shader.getUniformLocation( "uColor" ),
		    new Vector4f( this._red,
				  this._green,
				  this._blue,
				  1 ) );

	gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this._vertex_buffer );

	int pos = this._shader.getAttributeLocation( "aVertexPosition" );

	gl.enableVertexAttribArray( pos );

	gl.vertexAttribPointer( pos,
				3,
				DataType.FLOAT,
				false,
				0, 0 );

	gl.bindBuffer( BufferTarget.ARRAY_BUFFER, this._normals_buffer );

	int norm = this._shader.getAttributeLocation( "aVertexNormal" );

	gl.enableVertexAttribArray( norm );

	gl.vertexAttribPointer( norm,
				3,
				DataType.FLOAT,
				true,
				0, 0 );

	gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER,
		       this._indices_buffer );

	gl.drawElements( BeginMode.TRIANGLES,
			 this._indices.getLength(),
			 DrawElementsType.UNSIGNED_SHORT,
			 0);

    }

}
