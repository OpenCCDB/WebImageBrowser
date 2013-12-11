package edu.ucsd.ncmir.WIB.client.core.drawable;

/**
 * An <i>x</i>, <i>y</i> point representation.
 * @author spl
 */
public final class Point

{

    private final int _pointer_x;
    private final int _pointer_y;
    private final double _image_x;
    private final double _image_y;
    private final double _scale;

    Point( double scale,
           double image_x, double image_y, int pointer_x, int pointer_y )

    {

        this._scale = scale;

	this._image_x = image_x;
	this._image_y = image_y;

	this._pointer_x = pointer_x;
	this._pointer_y = pointer_y;

    }

    /**
     * Returns absolute the <i>x</i> coordinate of the pointer
     * (cursor) at which this <code>Point</code> was recorded.
     * @return The absolute <i>x</i> coordinate of the pointer.
     */

    public int getPointerX()

    {

	return this._pointer_x;

    }

    /**
     * Returns the absolute <i>y</i> coordinate of the pointer
     * (cursor) at which this <code>Point</code> was recorded.
     * @return The absolute <i>y</i> coordinate of the pointer.
     */

    public int getPointerY()

    {

	return this._pointer_y;

    }

    /**
     * Returns the scaled <i>x</i> coordinate of the pointer relative
     * to the origin of the image.
     * @return The scaled <i>x</i> coordinate of the pointer relative
     * to the origin of the image.
     */

    public double getX()

    {

        return this._image_x;

    }

    /**
     * Returns the scaled <i>y</i> coordinate of the pointer relative
     * to the origin of the image.
     * @return The scaled <i>y</i> coordinate of the pointer relative
     * to the origin of the image.
     */

    public double getY()

    {

        return this._image_y;

    }

    /**
     * The scale factor in effect at the instant the coordinate was
     * generated.
     * @return The scale factor.
     */
    public double getScale()

    {

        return this._scale;

    }

    @Override
    public String toString()

    {

        return "Point: " + this._image_x + " " + this._image_y + " " +
            this._pointer_x + " " + this._pointer_y;

    }

}
