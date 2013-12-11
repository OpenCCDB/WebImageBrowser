package edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin;

import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;

/**
 *
 * @author spl
 */
public class Overlay
    extends Drawable

{

    private int _red;
    private int _green;
    private int _blue;

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

    private String _object_name = "";
    
    @Override
    public void setObjectName( String object_name )
        
    {
        
        this._object_name = object_name;
        
    }

    @Override
    public String getObjectName()
        
    {
        
        return this._object_name;
        
    }

}