package edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin;

import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import java.util.HashMap;

/**
 *
 * @author spl
 */
class Mark
    extends Drawable

{

    private double _x;
    private double _y;
    private int _id;

    Mark( double x, double y, int id )

    {

	this.setLocation( x, y );
        this._id = id;
	super.setIgnoreInterval( true );

    }

    int getID()

    {

        return this._id;

    }

    final void setLocation( double x, double y )

    {

        this._x = x;
        this._y = y;

	super.clear();
	super.add( new double[] { x - 5, y } );
	super.add( new double[] { x + 5, y } );

	super.setBreak();
	super.add( new double[] { x, y - 5 } );
	super.add( new double[] { x, y + 5 } );

    }


    double getX()

    {

        return this._x;

    }

    double getY()

    {

        return this._y;

    }

    private HashMap<String,Mark> _siblings = new HashMap<String,Mark>();

    void addSibling( Mark sibling )

    {

	this._siblings.put( sibling.getObjectName(), sibling );

    }

    Mark getSibling( String view )

    {

	return this._siblings.get( view );

    }

    Mark[] getSiblings()

    {

	return this._siblings.values().toArray( new Mark[0] );

    }

    void clearSiblings()

    {

	this._siblings.clear();

    }

    private String _view;

    @Override
    public void setObjectName( String view )

    {

	this._view = view;

    }

    @Override
    public String getObjectName()

    {

	return this._view;

    }

    private int _red = 0xff;
    private int _green = 0x00;
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

    double distance( double x, double y )

    {

	double dx = this._x - x;
	double dy = this._y - y;

	return Math.sqrt( ( dx * dx ) + ( dy * dy ) );

    }

    double distance( Mark m )

    {

	return this.distance( m.getX(), m.getY() );

    }

    @Override
    public boolean equals( Object o )

    {

	boolean equals = false;

	if ( o instanceof Mark ) {

	    Mark m = ( Mark ) o;

	    equals = ( m._x == this._x ) && ( m._y == this._y );

	}

	return equals;

    }

    @Override
    public int hashCode()

    {

        return this._id;

    }

}
