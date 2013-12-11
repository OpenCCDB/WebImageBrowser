package edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin;

import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;

/**
 *
 * @author spl
 */
class Square
    extends Drawable

{

    private final Point _p0;

    /**
     *
     * @param point
     */
    Square( Point point )

    {

	this._p0 = point;

    }

    @Override
    public void setObjectName( String object_name )

    {

	// Does nothing.

    }

    @Override
    public String getObjectName()

    {

	return "Square";

    }

    private int _red = 255;
    private int _green = 0;
    private int _blue = 0;

    @Override
    public void setRGB( int red, int green, int blue )

    {

	this._red = red;
	this._green = green;
	this._blue = blue;

    }

    @Override
    public int getRed()

    {

	return this._red;

    }

    @Override
    public int getGreen()

    {

	return this._green;

    }

    @Override
    public int getBlue()

    {

	return this._blue;

    }

    void updateCorner( Point p1 )

    {

	double x0 = this._p0.getX();
	double y0 = this._p0.getY();

	double x1 = p1.getX();
	double y1 = p1.getY();

	double dx = Math.abs( x1 - x0 );
	double dy = Math.abs( y1 - y0 );

	double d = ( dx > dy ) ? dx : dy;

	double X0 = x0 - d;
	double Y0 = y0 - d;

	double X1 = x0 + d;
	double Y1 = y0 + d;

	this.clear();

	this.add( new double[] { X0, Y0 } );
	this.add( new double[] { X0, Y1 } );
	this.add( new double[] { X1, Y1 } );
	this.add( new double[] { X1, Y0 } );
	this.add( new double[] { X0, Y0 } );

    }

}
