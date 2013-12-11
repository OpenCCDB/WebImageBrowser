package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import java.util.ArrayList;

/**
 *
 * @author spl
 */
public class GetDisplayListMessage
    extends Message

{

    private final double _x0;
    private final double _y0;
    private final double _x1;
    private final double _y1;
    private final ArrayList<Drawable> _drawables = new ArrayList<Drawable>();

    /**
     * A message sent to acquire a display list of points which
     * overlap the bounding box.
     *
     * @param x0 Minimum x coordinate.
     * @param y0 Minimum y coordinate.
     * @param x1 Maximum x coordinate.
     * @param y1 Maximum y coordinate.
     */

    public GetDisplayListMessage( double x0, double y0, double x1, double y1 )

    {

	this._x0 = x0;
	this._y0 = y0;
	this._x1 = x1;
	this._y1 = y1;

    }

    /**
     * Returns the display list
     * @return the display list
     */

    public ArrayList<Drawable> getDisplayList()

    {

        return this._drawables;

    }

    /**
     * Adds a collection of <code>Drawable</code>s to the display list.
     * @param drawables The <code>Drawable</code>s to be added.
     */

    public void addPoints( Drawable[] drawables )

    {

        for ( int i = 0; i < drawables.length; i++ )
            this.addPoints( drawables[i] );

    }

    /**
     * Adds a contour to the display list.
     * @param contour The contour to be added.
     */
    public void addPoints( Drawable contour )

    {

        this._drawables.add( contour );

    }

    /**
     * @return the x0
     */

    public double getX0()

    {

        return this._x0;

    }

    /**
     * @return the y0
     */

    public double getY0()

    {

        return this._y0;

    }

    /**
     * @return the x1
     */

    public double getX1()

    {

        return this._x1;

    }

    /**
     * @return the y1
     */

    public double getY1()

    {

        return this._y1;

    }

}
