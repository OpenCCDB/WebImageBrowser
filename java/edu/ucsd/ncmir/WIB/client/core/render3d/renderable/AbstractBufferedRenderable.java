package edu.ucsd.ncmir.WIB.client.core.render3d.renderable;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import gwt.g3d.client.gl2.GL2;
import gwt.g3d.client.gl2.array.Float32Array;
import gwt.g3d.client.gl2.array.Int32Array;
import gwt.g3d.client.gl2.array.TypeArray;
import gwt.g3d.client.gl2.enums.BufferTarget;
import gwt.g3d.client.gl2.enums.BufferUsage;
import gwt.g3d.client.gl2.enums.DataType;
import gwt.g3d.client.mesh.VboConfig;

/**
 *
 * @author spl
 */
public abstract class AbstractBufferedRenderable
    extends AbstractRenderable

{

    /**
     * An abstract class for representing a Renderable.
     *
     */

    private VboConfig[] _vbos;

    protected AbstractBufferedRenderable( int n_vbos )

    {

	this._vbos = new VboConfig[n_vbos];

	for ( int i = 0; i < n_vbos; i++ )
	    this._vbos[i] = new VboConfig();

    }

    protected AbstractBufferedRenderable( VboConfig... vbos )

    {

	this._vbos = vbos;

    }


    @Override
    public final void dispose()

    {

	GL2 gl = super.getGL();

	for ( VboConfig vbo : this._vbos ) {

	    if ( vbo.getVertexBuffer() != null )
		gl.deleteBuffer( vbo.getVertexBuffer() );
	    vbo.reset();

	}
	this.disposeImpl();

    };

    protected void attachIndex( int index,
				String shader_variable )

    {

	int location =
	    super.getShader().getAttributeLocation( shader_variable );

	this._vbos[index].setVertexIndex( location );

    }

   protected void setVbo( float[] data, int index )

   {

	VboConfig vbo;

	Float32Array array = Float32Array.create( data );

	vbo = this._vbos[index];
	vbo.setDataType( DataType.FLOAT );
	vbo.setDataSize( 3 );
	this.createAndBufferArrayData( vbo, array, BufferUsage.STATIC_DRAW );

   }

    protected void setVbo( int[] data, int index )

    {

	VboConfig vboconfig;

	JsArrayInteger jai = JavaScriptObject.createArray().cast();

	for ( int d : data )
	    jai.push( d );

	Int32Array array = Int32Array.create( jai );

	vboconfig = this._vbos[index];
	vboconfig.setDataType( DataType.INT );
	vboconfig.setDataSize( 3 );
	this.createAndBufferArrayData( vboconfig,
				       array,
				       BufferUsage.STATIC_DRAW );

    }

    /**
     * Binds the VBOs.
     */

    protected void bind()

    {

	for ( VboConfig vbo : this._vbos )
	    this.bind( vbo );

	this.bindImpl();

    }

    /**
     * Overrides this method to perform extra binding.
     */

    protected void bindImpl()

    {

	// By default, does nothing.

    }

    /**
     * Unbinds the VBOs.
     */

    protected void unbind()

    {

	super.getGL().bindBuffer( BufferTarget.ARRAY_BUFFER, null );
	for ( VboConfig vbo : this._vbos )
	    unbind( vbo );
	this.unbindImpl();

    }

    /**
     * Overrides this method to perform extra un binding.
     */
    protected void unbindImpl()

    {

	// By default, does nothing.

    }

    /**
     * Creates a new buffer in the VBO and passes the data array to the VBO'
     * s buffer as ARRAY_BUFFER target using the given usage.
     *
     * @param vbo
     * @param dataArray
     * @param usage
     */
    protected void createAndBufferArrayData( VboConfig vbo,
					     TypeArray dataArray,
					     BufferUsage usage )

    {

	GL2 gl = super.getGL();

	if ( vbo.getVertexBuffer() != null )
	    gl.deleteBuffer( vbo.getVertexBuffer() );

	vbo.setVertexBuffer( gl.createBuffer() );
	this.bufferArrayData( vbo, dataArray, usage );

    }

    /**
     * Passes the data array to the VBO's buffer as ARRAY_BUFFER target using
     * the given usage.
     *
     * @param vbo
     * @param dataArray
     * @param usage
     */
    protected void bufferArrayData( VboConfig vbo,
				    TypeArray dataArray,
				    BufferUsage usage )

    {

	GL2 gl = super.getGL();

	gl.bindBuffer( BufferTarget.ARRAY_BUFFER, vbo.getVertexBuffer() );
	gl.bufferData( BufferTarget.ARRAY_BUFFER, dataArray, usage );

    }

     /**
     * Enables and binds the VBO.
     * If vertexIndex is negative, this method will do nothing.
     */

    protected void bind( VboConfig vbo )

    {

	int vertexIndex = vbo.getVertexIndex();

	if ( vertexIndex >= 0 ) {

	    GL2 gl = super.getGL();

	    gl.enableVertexAttribArray( vertexIndex );
	    if ( vbo.getDataType().equals( DataType.UNSIGNED_SHORT ) )
		gl.bindBuffer( BufferTarget.ELEMENT_ARRAY_BUFFER,
			       vbo.getVertexBuffer() );
	    else
		gl.bindBuffer( BufferTarget.ARRAY_BUFFER,
			       vbo.getVertexBuffer() );
	    gl.vertexAttribPointer( vertexIndex,
				    vbo.getDataSize(),
				    vbo.getDataType(),
				    vbo.isNormalize(),
				    0,
				    0 );

	}

    }

    /**
     * Unbinds a VBO.
     * If vertexIndex is negative, this method will do nothing.
     *
     * @param vbo
     */
    protected void unbind( VboConfig vbo )

    {

	if ( vbo.getVertexIndex() >= 0 )
	    super.getGL().disableVertexAttribArray( vbo.getVertexIndex() );

    }

}
