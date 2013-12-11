package edu.ucsd.ncmir.WIB.client.core.render3d.renderer;

import com.google.gwt.dom.client.CanvasElement;
import edu.ucsd.ncmir.WIB.client.core.render3d.renderable.Renderable;
import gwt.g3d.client.gl2.GL2;
import gwt.g3d.client.gl2.GLDisposable;
import gwt.g3d.client.gl2.enums.ClearBufferMask;
import gwt.g3d.client.math.MatrixStack;
import gwt.g3d.client.shader.AbstractShader;
import java.util.ArrayList;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;

public class Renderer
    implements GLDisposable

{

    private AbstractShader _shader;
    private final GL2 _gl;

    public Renderer( GL2 gl )
    {

        this._gl = gl;

    }

    public void init()

    {


    }

    @Override
    public void dispose()

    {

    }

    private float _angle = 0;
    private float _about_x = 0;
    private float _about_y = 0;
    private float _about_z = 1;

    private float _trans_x = 0;
    private float _trans_y = 0;
    private float _trans_z = 0;

    private float _mag_x = 1;
    private float _mag_y = 1;
    private float _mag_z = 1;

    private float _eye_x;
    private float _eye_y;
    private float _eye_z;

    private float _center_x;
    private float _center_y;
    private float _center_z;

    private float _up_x;
    private float _up_y;
    private float _up_z;

    public final void setScale( float mag_x, float mag_y, float mag_z )

    {

	this._mag_x = mag_x;
	this._mag_y = mag_y;
	this._mag_z = mag_z;

    }

    public final void setScale( double mag_x, double mag_y, double mag_z )

    {

	this.setScale( ( float ) mag_x, ( float ) mag_y, ( float ) mag_z );

    }

    public final void translate( float trans_x, float trans_y, float trans_z )

    {

	this._trans_x = trans_x;
	this._trans_y = trans_y;
	this._trans_z = trans_z;

    }

    public final void translate( double trans_x,
				 double trans_y,
				 double trans_z )

    {

	this.translate( ( float ) trans_x,
			( float ) trans_y,
			( float ) trans_z );

    }

    public final void lookAt( float eye_x, float eye_y, float eye_z,
			      float center_x, float center_y, float center_z,
			      float up_x, float up_y, float up_z )

    {

	this._eye_x = eye_x;
	this._eye_y = eye_y;
	this._eye_z = eye_z;

	this._center_x = center_x;
	this._center_y = center_y;
	this._center_z = center_z;

	this._up_x = up_x;
	this._up_y = up_y;
	this._up_z = up_z;

    }

    public final void rotate( double angle, double x, double y, double z )

    {

	this.rotate( ( float ) angle,
		     ( float ) x, ( float ) y, ( float ) z );

    }

    public final void rotate( float angle, float x, float y, float z )

    {

	this._angle = angle;
	this._about_x = x;
	this._about_y = y;
	this._about_z = z;

    }

    public final void update()

    {

	CanvasElement c = this._gl.getCanvas();

	float height = c.getHeight();
	float width = c.getWidth();

	this._gl.clear( ClearBufferMask.COLOR_BUFFER_BIT,
			ClearBufferMask.DEPTH_BUFFER_BIT );

	// PROJECTION matrix

	MatrixStack.PROJECTION.pushIdentity();
	MatrixStack.PROJECTION.perspective( 45,
					    width / height,
					    .1f,
					    ( float ) 1.0e10 );
	Matrix4f projection_matrix = MatrixStack.PROJECTION.get();
	MatrixStack.PROJECTION.pop();

	// MODELVIEW matrix

	MatrixStack.MODELVIEW.pushIdentity();
	MatrixStack.MODELVIEW.lookAt( this._eye_x,
				      this._eye_y,
				      this._eye_z,
				      this._center_x,
				      this._center_y,
				      this._center_z,
				      this._up_x,
				      this._up_y,
				      this._up_z );
	MatrixStack.MODELVIEW.translate( this._center_x,
					 this._center_y,
					 this._center_z );
	MatrixStack.MODELVIEW.translate( this._trans_x,
					 this._trans_y,
					 this._trans_z );
	MatrixStack.MODELVIEW.rotate( this._angle,
				      this._about_x,
				      this._about_y,
				      this._about_z );
	MatrixStack.MODELVIEW.scale( this._mag_x, this._mag_y, this._mag_z );
	MatrixStack.MODELVIEW.translate( -this._center_x,
					 -this._center_y,
					 -this._center_z );
	Matrix4f modelview_matrix = MatrixStack.MODELVIEW.get();
	Matrix3f normal_matrix = new Matrix3f();
	MatrixStack.MODELVIEW.getInvertTranspose( normal_matrix );

	MatrixStack.MODELVIEW.pop();

	// Render.

	for ( Renderable m : this._renderables ) {

	    m.setEyePosition( this._eye_x, this._eye_y, this._eye_z );
	    m.setLightPosition( this._eye_x - 50, this._eye_y - 50, this._eye_z );
	    m.draw( projection_matrix, modelview_matrix, normal_matrix );

	}

    }

    private final ArrayList<Renderable> _renderables =
	new ArrayList<Renderable>();

    public final void clearAll()

    {

        this._renderables.clear();

    }

    public final void addRenderable( Renderable mesh )

    {

	this._renderables.add( mesh );
        mesh.init( this._gl );

    }

    public final void dropRenderable( Renderable mesh )

    {

	this._renderables.remove( mesh );

    }

}
