package edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin;

import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;

class Star
    extends Drawable

{

    static double[] _x = new double[5];
    static double[] _y = new double[5];

    static {

	for ( int i = 0; i < 5; i++ ) {

	    double ang = ( i * 360 / 5 ) * Math.PI / 180;

	    Star._x[i] = Math.sin( ang );
	    Star._y[i] = -Math.cos( ang );
	    
	}
            
    }

    Star( double x0, double y0, double radius )

    {

	this.add( new double[] {
		( Star._x[0] * radius ) + x0, ( Star._y[0] * radius ) + y0
	    } );
	this.add( new double[] {
		( Star._x[2] * radius ) + x0, ( Star._y[2] * radius ) + y0
	    } );
	this.add( new double[] {
		( Star._x[4] * radius ) + x0, ( Star._y[4] * radius ) + y0
	    } );
	this.add( new double[] {
		( Star._x[1] * radius ) + x0, ( Star._y[1] * radius ) + y0
	    } );
	this.add( new double[] {
		( Star._x[3] * radius ) + x0, ( Star._y[3] * radius ) + y0
	    } );
	this.add( new double[] {
		( Star._x[0] * radius ) + x0, ( Star._y[0] * radius ) + y0
	    } );
	
    }

    @Override
    public void setObjectName( String object_name )
    {
	
	// Does nothing

    }

    @Override
    public String getObjectName()

    {

	return "Star";

    }

    private int _red = 0xff;
    private int _green = 0xff;
    private int _blue = 0x00;

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

}
