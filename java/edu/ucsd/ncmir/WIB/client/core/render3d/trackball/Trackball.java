package edu.ucsd.ncmir.WIB.client.core.render3d.trackball;

import edu.ucsd.ncmir.spl.LinearAlgebra.AbstractDoubleVector;
import edu.ucsd.ncmir.spl.LinearAlgebra.Double3Vector;
import edu.ucsd.ncmir.spl.LinearAlgebra.Quaternion;

/**
 *
 * @author spl
 */
public class Trackball

{

    private final TrackballCallbackInterface _callback;

    /**
     *
     * @param window_width
     * @param window_height
     * @param callback
     */
    public Trackball( int window_width, int window_height,
                      TrackballCallbackInterface callback )

    {

	this._callback = callback;

	this.setWindowSize( window_width, window_height );

    }

    private int _center_x;
    private int _center_y;
    private float _radius;

    public final void setWindowSize( int window_width, int window_height )

    {

	this._center_x = ( int ) Math.floor( ( window_width - 1 ) / 2.0 );
	this._center_y = ( int ) Math.floor( ( window_height - 1 ) / 2.0 );

	this._radius =
	    ( float ) Math.sqrt( ( this._center_x * this._center_x ) +
				 ( this._center_y * this._center_y ) );

    }

    private int _trackball_x;
    private int _trackball_y;

    public void initialize( int x, int y )

    {

	this._trackball_x = x - ( int ) this._center_x;
	this._trackball_y = y - ( int ) this._center_y;

    }

    public void move( int x, int y, boolean x_only, boolean y_only )

    {

	int current_x = y_only ? 0 : ( x - this._center_x );
	int current_y = x_only ? 0 : ( y - this._center_y );

	this._trackball_x = y_only ? 0 : this._trackball_x;
	this._trackball_y = x_only ? 0 : this._trackball_y;

	if ( ( current_x != this._trackball_x ) ||
	     ( current_y != this._trackball_y ) ) {

	    /*
	     * Find projection of previous and current point onto unit sphere.
	     *
	     * Note: The Y axis is flipped top-for-bottom.  Ain't graphics
	     * wunnerful?
	     */

	    double pt0_x = this._trackball_x / this._radius;
	    double pt0_y = this._trackball_y / this._radius;
	    double pt0_z = 1.0 - ( ( pt0_x * pt0_x ) + ( pt0_y * pt0_y ) );
	    double pt1_x = current_x / this._radius;
	    double pt1_y = current_y / this._radius;
	    double pt1_z = 1.0 - ( ( pt1_x * pt1_x ) + ( pt1_y * pt1_y ) );

	    if ( ( pt0_z >= 0.0 ) && ( pt1_z >= 0.0 ) ) {

		pt0_z = Math.sqrt( pt0_z );
		pt1_z = Math.sqrt( pt1_z );

		Double3Vector pt0 = new Double3Vector( pt0_x, pt0_y, pt0_z );
		Double3Vector pt1 = new Double3Vector( pt1_x, pt1_y, pt1_z );

		/*
		 * Find cross product to get the axis of rotation.
		 */

		AbstractDoubleVector axis = pt0.multiply( pt1 ).normalize();

		double alpha = Math.acos( pt0.scalarProduct( pt1 ) );

		Quaternion q = Quaternion.createRotationRadians( axis, alpha );

		this._callback.callback( q );

	    }
	    this._trackball_x = current_x;
	    this._trackball_y = current_y;

	}

    }

    public void initialize( int[] xy )

    {

        this.initialize( xy[0], xy[1] );

    }

    public void move( int[] xy )

    {

        this.move( xy[0], xy[1], false, false );

    }

}
