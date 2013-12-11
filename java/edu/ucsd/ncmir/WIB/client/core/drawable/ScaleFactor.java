package edu.ucsd.ncmir.WIB.client.core.drawable;

/**
 * This class encapsulates all of the scaling calculations.
 * @author spl
 */
public class ScaleFactor

{

    private static int _max_mag;

    public static void setMaxMag( int max_mag )

    {

        ScaleFactor._max_mag = max_mag;

    }

    private final double _exponent;
    private final double _scale;
    private final int _integer_part;
    private final double _tile_scale_factor;
    private final int _integer_part_multiplier;

    /**
     * Create a <code>ScaleFactor</code> object.
     * @param exponent The ScaleFactor exponent.
     */
    public ScaleFactor( double exponent )

    {

	this._exponent = exponent;
	this._scale = Math.pow( 2, exponent );
	int integer_part = ( int ) Math.floor( exponent );
	if ( integer_part > 0 )
	    integer_part = 0;
	this._integer_part = integer_part;
	this._integer_part_multiplier = 1 << -integer_part;
	this._tile_scale_factor = Math.pow( 2, exponent - integer_part );
    }

    public ScaleFactor( String string )

    {

        this( Double.parseDouble( string ) );

    }

    public int getLevel()

    {

        return ( int ) -( ScaleFactor._max_mag - this._exponent );

    }

    @Override
    public String toString()

    {

	return "Zoom: 2^" + this._exponent + " = " + this._scale;

    }

    /**
     * The zoom exponent is the basic value used by the ZoomBar slider
     * to determine overall image magnification.  It will vary between
     * some negative value expressing the maximum minification of the
     * image and 1 (giving an overall magnification of 2).
     * @return The zoom exponent.
     */

    public double getExponent()

    {

        return this._exponent;

    }

    /**
     * The scale factor is 2 raised to the value of the exponent.
     * @return The overall scale factor.
     */

    public double getScale()

    {

        return this._scale;

    }

    /**
     * Returns the integer part of the exponent.
     * @return The integer part of the exponent.
     */

    public int getZoomExponent()

    {

        return this._integer_part;

    }

    /**
     * Returns a tile scale factor.
     * @return The tile scale factor.
     */
    public double getTileScaleFactor()

    {

        return this._tile_scale_factor;

    }

    /**
     * Scales the tilesize by 2 to the power of the integer part of
     * the exponent.
     * @param tilesize The raw tilesize;
     * @return The scaled tilesize.
     */

    public int getScaledTilesize( int tilesize )

    {

	return this._integer_part_multiplier * tilesize;

    }

}
