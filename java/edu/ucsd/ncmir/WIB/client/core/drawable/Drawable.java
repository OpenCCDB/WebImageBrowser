package edu.ucsd.ncmir.WIB.client.core.drawable;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.xml.client.Element;
import edu.ucsd.ncmir.spl.Interpolator.LaGrangianInterpolator;
import edu.ucsd.ncmir.spl.XMLUtil.XML;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 *
 * @author spl
 */
public abstract class Drawable
    implements Collection<double[]>

{

    /**
     * Sets the name of the object.
     *
     * @param object_name The name of the object.
     */
    abstract public void setObjectName( String object_name );

    /**
     * Returns the object's name
     *
     * @return The object's name.
     */
    abstract public String getObjectName();

    /**
     * Sets the color of the object. It is the responsibility of the extending
     * class to ascertain that the color components are clamped to the range of
     * 0 to 255.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     */
    abstract public void setRGB( int red, int green, int blue );

    /**
     * Returns the red component of the color.
     *
     * @return The red component of the color.
     */
    abstract public int getRed();

    /**
     * Returns the green component of the color.
     *
     * @return The green component of the color.
     */
    abstract public int getGreen();

    /**
     * Returns the blue component of the color.
     *
     * @return The blue component of the color.
     */
    abstract public int getBlue();

    /**
     * Adds a list of points from an XML
     * <code>TEXT</code>
     * <code>Element</code>. The points are assumed to be newline delimited with
     * <b><i>x y</i></b> pairs further delimited by one or more space
     * characters.
     *
     * @param element The <code>TEXT</code> XML <code>Element</code>.
     */

    public final void addPointsFromXML( Element element )

    {

        this.addPointsFromString( XML.extractTextFromElement( element ) );

    }

    /**
     * Adds a list of points from a
     * <code>String</code> delimited by newlines.
     *
     * @param string The newline delimited <code>String</code>.
     */

    public final void addPointsFromString( String string )

    {

        this.addPointsFromStringArray( string.trim().split( "\n" ) );

    }

    /**
     * Adds a list of points from an array of space delimited strings.
     *
     * @param string_array The array of <code>String</code>s.
     */

    public final void addPointsFromStringArray( String[] string_array )

    {

        for ( int p = 0; p < string_array.length; p++ ) {

            String[] t = string_array[p].trim().split( " " );

            double x = Double.parseDouble( t[0] );
            double y = Double.parseDouble( t[1] );

            if ( this._xmin > x )
                this._xmin = x;
            if ( this._ymin > y )
                this._ymin = y;
            if ( this._xmax < x )
                this._xmax = x;
            if ( this._ymax < y )
                this._ymax = y;

            double[] d = new double[]{ x, y };
            this.add( d );

        }

    }

    private boolean _ignore_interval = false;

    /**
     * Force rendering of a line with no sampling.
     * @param ignore_interval Set to <code>true</code> to force the
     * line to be rendered with no interruptions.
     */
    public final void setIgnoreInterval( boolean ignore_interval )

    {

	this._ignore_interval = ignore_interval;

    }

    private HashSet<Integer> _breaks = new HashSet<Integer>();

    /**
     * Sets break points (moveTo) in a rendered object.
     * @param breakpoint The index of the point at which the line
     * should be broken.
     */
    public final void setBreak( int breakpoint )

    {

	this._breaks.add( breakpoint );

    }

    /**
     * Sets break points (moveTo) in a rendered object following the
     * last point added.
     */
     public final void setBreak()

    {

	this.setBreak( this.size() );

    }

    /**
     * Renders the list of points to a <code>Context2d</code>.
     *
     * @param c2d The <code>Context2d</code>.
     * @param interval The sampling interval at which the points will
     * be rendered.
     * @param force If <code>true</code> render whether flagged as
     * visible or not.
     */
    public final void render( Context2d c2d,
			      double interval,
			      double linewidth,
			      double transparency,
			      boolean force )

    {

	if ( force || this._visible ) {

	    c2d.save();
	    if ( this._ignore_interval )
		interval = 1;

	    c2d.setLineWidth( linewidth );
	    c2d.setStrokeStyle( this.getCssColor( transparency ) );

	    c2d.beginPath();

	    double[][] xy = this.toArray( new double[0][] );

	    c2d.moveTo( xy[0][0], xy[0][1] );

	    for ( double p = interval;
		  p < xy.length - interval;
		  p += interval ) {

		int i = ( int ) Math.round( p );

		if ( this._breaks.contains( i ) )
		    c2d.moveTo( xy[i][0], xy[i][1] );
		else
		    c2d.lineTo( xy[i][0], xy[i][1] );

	    }
	    if ( this.isClosed() )
		c2d.lineTo( xy[0][0], xy[0][1] );
	    else
		c2d.lineTo( xy[xy.length - 1][0], xy[xy.length - 1][1] );
	    c2d.stroke();
	    c2d.restore();

	}

    }

    /**
     * Sets the color from a packed integer in the form of
     * <code>0xrrggbb</code>.
     *
     * @param rgb The packed integer.
     */
    public final void setRGB( int rgb )
    {

        this.setRGB( ( rgb >> 16 ) & 0x000000ff,
                     ( rgb >> 8 ) & 0x000000ff,
                     rgb & 0x000000ff );

    }

    /**
     * Returns the color as a packed integer in the form of
     * <code>0xrrggbb</code>.
     *
     * @return The packed integer.
     */
    public final int getRGB()
    {

        return ( this.getRed() << 16 )
            | ( this.getGreen() << 8 )
            | this.getBlue();

    }

    private double _xmin = Double.MAX_VALUE;
    private double _ymin = Double.MAX_VALUE;
    private double _xmax = -Double.MAX_VALUE;
    private double _ymax = -Double.MAX_VALUE;

    /**
     * Sets the limits of the bounding rectangle of this array of points.
     *
     * @param xmin The minimum <b><i>x</i></b> value.
     * @param ymin The minimum <b><i>y</i></b> value.
     * @param xmax The maximum <b><i>x</i></b> value.
     * @param ymax The maximum <b><i>y</i></b> value.
     */
    public final void setBoundingBox( double xmin, double ymin,
                                      double xmax, double ymax )
    {

        this._xmin = xmin;
        this._ymin = ymin;

        this._xmax = xmax;
        this._ymax = ymax;

    }

    /**
     * Returns the bounding box of this object as an array of array of
     * <code>double</code>s.
     * <code>bb[0][0]</code> corresponds to <b><i>x minimum</i></b>,
     * <code>bb[0][1]</code> corresponds to <b><i>y minimum</i></b>,
     * <code>bb[1][0]</code> corresponds to <b><i>x maximum</i></b>, and
     * <code>bb[1][1]</code> corresponds to <b><i>y maximum</i></b>.
     *
     * @return The bounding box.
     */
    public final double[][] getBoundingBox()
    {

        return new double[][]{
                { this._xmin, this._ymin },
                { this._xmax, this._ymax }
            };

    }

    /**
     * Returns the area subsumed by the boundinb box of this object.
     *
     * @return The area.
     */
    public final double getBBoxArea()
    {

        return ( this._xmax - this._xmin ) * ( this._ymax - this._ymin );

    }

    public final double[] getCentroid()

    {

	double[][] t = this.toArray( new double[0][] );

	double xcentroid = 0;
	double ycentroid = 0;

	double area = 0;

	for ( int i = t.length - 1, j = 0; j < t.length; i = j, j++ ) {

	    double xi = t[i][0];
	    double xj = t[j][0];

	    double yi = t[i][1];
	    double yj = t[j][1];

	    double weight = ( xi * yj ) - ( xj * yi );

	    area += weight;

	    xcentroid += ( xi + xj ) * weight;
	    ycentroid += ( yi + yj ) * weight;

	}

	return new double[] {
	    xcentroid / ( 3 * area ),
	    ycentroid / ( 3 * area )
	};

    }

    /**
     * Returns a newline delimited
     * <code>String</code> containing <b><i>x y</i></b> pairs further delimited
     * by one or more space characters.
     *
     * @return The <code>String</code>.
     */
    @Override
    public final String toString()
    {

        String plist = "";

        for ( double[] p : this.toArray( new double[0][] ) )
            plist += p[0] + " " + p[1] + "\n";

        return plist;

    }

    /**
     * Adds a coordinate pair to the array.
     *
     * @param xy An xy coordinate pair.
     */
    public final void add( Point xy )
    {

	double[] p = new double[] { xy.getX(), xy.getY() };

	if ( this._xmin > p[0] )
	    this._xmin = p[0];
	if ( this._xmax > p[0] )
	    this._xmax = p[0];

	if ( this._ymin > p[1] )
	    this._ymin = p[1];
	if ( this._ymax > p[1] )
	    this._ymax = p[1];

	this.add( p );

    }

    /**
     * Resamples to a zoom level of 1x.
     */
    public final void resample()
    {

        this.resampleArray( this.toArray( new double[0][] ) );

    }

    /**
     * Closes an open contour.
     */
    public final void close()
    {

        this._closed = true;

    }

     /**
     * Closes an open contour.
     */
    public final void setClosed( boolean close )
    {

        this._closed = close;

    }

   /**
     * A query as to whether the object is open (a <i>polyline</i>) or
     * closed (a <i>polygon</i>.
     *
     * @return <code>true</code> if a polygon, else <code>false</code>.
     */
    public final boolean isClosed()
    {

        return this._closed;

    }

    /**
     * Returns the line type as a
     * <code>String</code>.
     *
     * @return Either <code>polygon</code> or <code>polyline</code>.
     */
    public final String getLineType()
    {

        return this._closed ? "polygon" : "polyline";

    }

    /**
     * A test to determine whether the bounding box described by the arguments
     * overlaps or is entirely within the bounding box of this object.
     *
     * @param xmin <b><i>x minimum</i></b>
     * @param ymin <b><i>y minimum</i></b>
     * @param xmax <b><i>x maximum</i></b>
     * @param ymax <b><i>y maximum</i></b>
     * @return <code>true</code> if the box described by the arguments
     * overlaps or is entirely contained within this object's bounding
     * box.
     */
    public final boolean contains( double xmin, double ymin,
                                   double xmax, double ymax )
    {

        return !( ( xmax < this._xmin ) ||
		  ( xmin > this._xmax ) ||
		  ( ymax < this._ymin ) ||
		  ( ymin > this._ymax ) );

    }

    /**
     * A test to determine whether the bounding box described by the arguments
     * overlaps or is entirely within the bounding box of this object.
     *
     * @param bbox The object's bounding box.
     * @return <code>true</code> if the box described by the arguments
     * overlaps or is entirely contained within this object's bounding
     * box.
     */
    public final boolean contains( double[][] bbox )

    {

	return this.contains( bbox[0][0], bbox[0][1], bbox[1][0], bbox[1][1] );

    }

    /**
     * A test to determine whether the bounding box of this object overlaps or
     * is entirely within the bounding box described by the arguments.
     *
     * @param xmin <b><i>x minimum</i></b>
     * @param ymin <b><i>y minimum</i></b>
     * @param xmax <b><i>x maximum</i></b>
     * @param ymax <b><i>y maximum</i></b>
     * @return <code>true</code> if this object's bounding box
     * overlaps or is entirely contained within the bounding box
     * described by the arguments.
     */
    public final boolean containedBy( double xmin, double ymin,
                                      double xmax, double ymax )
    {

        return !( ( this._xmax < xmin ) ||
		  ( this._xmin > xmax ) ||
		  ( this._ymax < ymin ) ||
		  ( this._ymin > ymax ) );

    }

    /**
     * A test to determine whether the bounding box of this object overlaps or
     * is entirely within the bounding box described by the arguments.
     *
     * @param bbox The object's bounding box.
     * @return <code>true</code> if this object's bounding box
     * overlaps or is entirely contained within the bounding box
     * described by the arguments.
     */
    public final boolean containedBy( double[][] bbox )

    {

	return this.containedBy( bbox[0][0], bbox[0][1],
				 bbox[1][0], bbox[1][1] );

    }

    /**
     * A test to determine whether this object entirely encloses another.
     * @param drawable The object to test.
     * @return <code>true</code> if this object entirely encloses the
     * <code>Drawable</code> being tested.
     */
    public final boolean encloses( Drawable drawable )

    {

	boolean encloses = false;

	if ( !this.intersects( drawable ) ) {

	    if ( this.isClosed() &&
		 this.contains( drawable.getBoundingBox() ) ) {

		double[][] points = drawable.toArray( new double[0][] );

		boolean outside = false;
		for ( int i = 0; !outside && ( i < points.length ); i++ )
		    outside =
			!this.pointInPolygon( points[i][0], points[i][1] );

		encloses = !outside;

	    }

	}
	return encloses;

    }


    /**
     * A test to determine whether this object and another intersect
     * at any point.
     * @param drawable The object to be tested.
     * @return <code>true</code> if the objects intersect, otherwise
     * <code>false</code>.
     */
    public final boolean intersects( Drawable drawable )

    {

	boolean intersects = false;

	if ( this.contains( drawable.getBoundingBox() ) ||
	     this.containedBy( drawable.getBoundingBox() ) ) {

	    double[][] dmb =
		this.computeMB( drawable.toArray( new double[0][] ) );

	    double[] dmx = dmb[0];
	    double[] dmy = dmb[1];

	    double[] dbx = dmb[2];
	    double[] dby = dmb[3];

	    double[][] tmb =
		this.computeMB( this.toArray( new double[0][] ) );

	    double[] tmx = tmb[0];
	    double[] tmy = tmb[1];

	    double[] tbx = tmb[2];
	    double[] tby = tmb[3];

	    for ( int i = 0; !intersects && ( i < dmx.length ); i++ ) {

		double mx1 = dmx[i];
		double my1 = dmy[i];

		double bx1 = dbx[i];
		double by1 = dby[i];

		for ( int j = 0; !intersects && ( j < tmx.length ); j++ ) {

		    double mx2 = tmx[j];
		    double my2 = tmy[j];

		    double bx2 = tbx[j];
		    double by2 = tby[j];

		    double top =
			( mx2 * ( by1 - by2 ) ) - ( my2 * ( bx1 - bx2 ) );
		    double bottom =
			( mx1 * my2 ) - ( my1 * mx2 );

		    if ( bottom != 0 ) {

			double t1 = top / bottom;

			if ( ( 0 <= t1 ) && ( t1 <= 1 ) ) {

			    double t2;

			    if ( mx2 != 0 )
				t2 = ( ( mx1 * t1 ) + bx1 - bx2 ) / mx2;
			    else
				t2 = ( ( my1 * t1 ) + by1 - by2 ) / my2;

			    intersects = ( 0 <= t2 ) && ( t2 <= 1 );

			}

		    }

		}

	    }

	}

	return intersects;

    }

    /**
     * Removes all duplicate points and intersections. When an intersection is
     * encountered, the loop with the least points is removed.
     */
    public final void validate()
    {

        if ( this.size() > 2 )
            do
                this.removeDuplicates();
            while ( this.removeIntersection() );

        this._xmin = Double.MAX_VALUE;
        this._ymin = Double.MAX_VALUE;

        this._xmax = -Double.MAX_VALUE;
        this._ymax = -Double.MAX_VALUE;

        for ( double[] xyp : this ) {

            if ( this._xmin > xyp[0] )
                this._xmin = xyp[0];
            if ( this._ymin > xyp[1] )
                this._ymin = xyp[1];
            if ( this._xmax < xyp[0] )
                this._xmax = xyp[0];
            if ( this._ymax < xyp[1] )
                this._ymax = xyp[1];

        }

    }

    /**
     * Performs a &quot;<i>Point in polygon</i>&quot; test on the
     * <b><i>x</i></b>, <b><i>y</i></b> coordinate supplied.
     *
     * @param x The <b><i>x</i></b> component of the coordinate.
     * @param y The <b><i>y</i></b> component of the coordinate.
     * @return <code>true</code> if the coordinate is contained within this
     * polygon, <code>false</code> otherwise. If this object is an open contour
     * (a <i>polyline</i>), then <code>false</code> is returned by definition.
     */
    public final boolean pointInPolygon( double x, double y )

    {

        boolean inside = false;

        if ( this.isClosed() )	// Bounding box test.
            if ( ( ( this._xmin <= x ) && ( x <= this._xmax ) ) &&
		 ( ( this._ymin <= y ) && ( y <= this._ymax ) ) ) {

                int n = this.size();

                double[] pX = new double[n];
                double[] pY = new double[n];

                double[][] xy = this.toArray( new double[0][] );

                for ( int i = 0; i < n; i++ ) {

                    pX[i] = xy[i][0] - x;
                    pY[i] = xy[i][1] - y;

                }

                int crossings = 0;

                for ( int i = 0; i < n; i++ ) {

                    int i1 = ( i + ( n - 1 ) ) % n;

                    if ( ( ( pY[i] > 0 ) && ( pY[i1] <= 0 ) ) ||
			 ( ( pY[i1] > 0 ) && ( pY[i] <= 0 ) ) ) {

                        double X =
			    ( ( pX[i] * pY[i1] ) - ( pX[i1] * pY[i] ) )
                            / ( pY[i1] - pY[i] );

                        if ( X > 0 )
                            crossings++;

                    }

                }

                inside = ( crossings % 2 ) == 1;

            }

        return inside;

    }

    /**
     * Computes the minimum Euclidean distance from the point described by the
     * coordinate supplied to this object.
     *
     * @param x The <b><i>x</i></b> component of the coordinate.
     * @param y The <b><i>y</i></b> component of the coordinate.
     * @return The minimum Euclidean distance.
     */
    public final double distanceTo( double x, double y )
    {

        double closest = Double.MAX_VALUE;

        for ( double[] xy : this.toArray( new double[0][] ) ) {

            double dx = xy[0] - x;
            double dy = xy[1] - y;

            double dsqd = ( dx * dx ) + ( dy * dy );

            if ( dsqd < closest )
                closest = dsqd;

        }

        return Math.sqrt( closest );

    }

    /**
     * Creates a quadratic Bezier curve using the coordinate supplied as the P2
     * control point. Control points P1 and P3 are determined by marching along
     * the current curve positively and negatively until the radius value is
     * exceeded and using the last point within that Euclidean distance.
     *
     * @param radius The Euclidean distance.
     * @param x The <b><i>x</i></b> component of the coordinate.
     * @param y The <b><i>y</i></b> component of the coordinate.
     */
    public final void bezier( int radius, double x, double y )
    {

        double closest = Double.MAX_VALUE;
        int p1 = -1;

        double[][] xya = this.toArray( new double[0][] );

        int n = xya.length;

        for ( int i = 0; i < n; i++ ) {

            double[] xy = xya[i];

            double dx = xy[0] - x;
            double dy = xy[1] - y;

            double dsqd = ( dx * dx ) + ( dy * dy );

            if ( dsqd < closest ) {

                closest = dsqd;
                p1 = i;

            }

        }
        closest = Math.sqrt( closest );

        if ( closest < radius ) {

            double rsqd = radius * radius;

            int p0 = p1;
            for ( int i = 1; i < n; i++ ) {

                p0 = ( p1 + ( n - i ) ) % n;

                if ( !this.isWithin( xya[p0], x, y, rsqd ) )
                    break;

            }

            int p2 = p1;
            for ( int i = 1; i < n; i++ ) {

                p2 = ( p1 + i ) % n;

                if ( !this.isWithin( xya[p2], x, y, rsqd ) )
                    break;

            }

	    if ( !this.isClosed() ) {

		if ( p0 > p1 )
		    p0 = 0;
		if ( p1 > p2 )
		    p2 = xya.length - 1;

	    }

            double xp0 = xya[p0][0];
            double yp0 = xya[p0][1];

            double xp1 = x;
            double yp1 = y;

            double xp2 = xya[p2][0];
            double yp2 = xya[p2][1];

            Vector<double[]> d = new Vector<double[]>();

            for ( int i = 0; i <= 20; i++ ) {

                double t = i / 20.0;
                double tsqd = t * t;
                double onemt = 1 - t;
                double onemtsqd = onemt * onemt;
                double onemtxt = 2 * onemt * t;

                d.add( new double[]{
                        ( onemtsqd * xp0 ) + ( onemtxt * xp1 ) + ( tsqd * xp2 ),
                        ( onemtsqd * yp0 ) + ( onemtxt * yp1 ) + ( tsqd * yp2 )
                    } );


            }
            Drawable.resampleArray( d.toArray( new double[0][] ), d, false );

            this.clear();

	    if ( this.isClosed() ) {

		int p = p2 + 1;
		while ( ( p %= xya.length ) != p0 ) {

		    this.add( new double[]{ xya[p][0], xya[p][1] } );
		    p++;

		}

		double[][] xyd = d.toArray( new double[0][] );
		for ( int i = 0; i < xyd.length; i++ )
		    this.add( new double[]{ xyd[i][0], xyd[i][1] } );

	    } else {

		for ( int i = 0; i < p0; i++ )
		    this.add( new double[]{ xya[i][0], xya[i][1] } );

		double[][] xyd = d.toArray( new double[0][] );
		for ( int i = 0; i < xyd.length; i++ )
		    this.add( new double[]{ xyd[i][0], xyd[i][1] } );

		for ( int i = p2 + 1; i < xya.length; i++ )
		    this.add( new double[]{ xya[i][0], xya[i][1] } );

	    }

        }

    }

     /**
     * Extends the trace.
     *
     * @param x The <b><i>x</i></b> component of the coordinate.
     * @param y The <b><i>y</i></b> component of the coordinate.
     */
    public final void extend( double x, double y )
    {

	double d0 = this.distanceFrom( 0, x, y );
	double d1 = this.distanceFrom( this.size() - 1, x, y );

	if ( d0 < d1 )
	    this.add( 0, new double[] { x, y } );
	else
	    this.add( new double[] { x, y } );

    }

    /**
     * Nudges the trace away from the cursor coordinate by the radius supplied.
     *
     * @param radius The radius of the nudging circle.
     * @param x The <b><i>x</i></b> component of the coordinate.
     * @param y The <b><i>y</i></b> component of the coordinate.
     */
    public final void nudge( int radius, double x, double y )
    {

        double rsqd = radius * radius;

        double[][] xyr = this.toArray( new double[0][] );
        this.clear();

        boolean[] flag = new boolean[xyr.length];

        for ( int i = 0; i < xyr.length; i++ ) {

            double[] xy = xyr[i];

            double dx = xy[0] - x;
            double dy = xy[1] - y;

            if ( ( ( dx * dx ) + ( dy * dy ) ) <= rsqd ) {

                double s = radius / Math.sqrt( ( dx * dx ) + ( dy * dy ) );

                xy[0] = ( dx * s ) + x;
                xy[1] = ( dy * s ) + y;

                flag[i] = true;

            } else
                flag[i] = false;

        }

	int I0;
	int Ip;

	if ( this.isClosed() ) {

	    I0 = 0;
	    Ip = xyr.length - 1;

	} else {

	    I0 = 1;
	    Ip = 0;
	    this.add( xyr[0] );

	}

        for ( int i = I0, im1 = Ip; i < xyr.length; im1 = i, i++ ) {

            double[] xy = xyr[i];

            if ( flag[i] ) {

                double dx = xy[0] - xyr[im1][0];
                double dy = xy[1] - xyr[im1][1];

                double d = Math.sqrt( ( dx * dx ) + ( dy * dy ) );

                if ( d > ( radius / 2 ) ) {

                    int chunks = ( int ) d;
                    double mx = dx / ( chunks - 1 );
                    double my = dy / ( chunks - 1 );

                    for ( int c = 1; c < chunks; c++ )
                        this.add( new double[] {
                                ( mx * c ) + xyr[im1][0],
                                ( my * c ) + xyr[im1][1]
                            } );

                } else
                    this.add( xy );

            } else
                this.add( xy );

        }

    }

    private boolean _visible = true;

    /**
     * Sets the visibility state of the object.
     *
     * @param visible <code>true</code> if visible, <code>false</code> if not.
     */
    public final void setVisible( boolean visible )

    {

        this._visible = visible;

    }

    /**
     * Returns the intended visiblity state of the object.
     *
     * @return <code>true</code> if visible, <code>false</code> if not.
     */
    public final boolean isVisible()

    {

        return this._visible;

    }

    private final Data _data = Data.create();

    @Override
    public boolean add( double[] xy )

    {

	this._data.add( xy[0], xy[1] );
	return true;

    }

    /**
     * Inserts a point at the specified index.
     * @param index The index at which the point is to be inserted.
     * @param xy The point.
     * @return Always returns <code>true</code>.
     */
    public boolean add( int index, double[] xy )

    {

        this._data.insert( index, xy[0], xy[1] );
        return true;

    }

    /**
     * Gets a point at the specified index.
     * @param index
     * @return The point.
     */
    public double[] get( int index )

    {

        return new double[] {
	    this._data.getX( index ),
	    this._data.getY( index )
	};

    }

    @Override
    public boolean addAll( Collection<? extends double[]> c )

    {

	for ( double[] d : c )
	    this.add( d );
	return true;

    }

    @Override
    public void clear()

    {

	this._data.clear();

    }

    @Override
    public boolean contains( Object o )

    {

	boolean contains = false;

	if ( o instanceof double[] ) {

	    double[] xy = ( double[] ) o;
	    for ( int i = 0; !contains && ( i < this.size() ); i++ ) {

		double[] XY = this.get( i );

		contains = ( xy[0] == XY[0] ) && ( xy[1] == XY[1] );

	    }

	}
	return contains;

    }

    @Override
    public boolean containsAll( Collection<?> c )

    {

	boolean contained = false;

	if ( c instanceof Drawable ) {

	    Drawable d = ( Drawable ) c;

	    for ( int i = 0; i < d.size(); i++ )
		if ( !( contained = this.contains( d.get( i ) ) ) )
		    break;

	}
	return contained;

    }

    @Override
    public boolean equals( Object o )

    {

	boolean equals = false;

	if ( o instanceof Drawable ) {

	    Drawable d = ( Drawable ) o;

	    if ( d.size() == this.size() ) {

		equals = true;
		for ( int i = 0; i < this.size(); i++ ) {

		    double[] xy = this.get( i );
		    double[] XY = d.get( i );

		    if ( !( equals =
			    ( xy[0] == XY[0] ) && ( xy[1] == XY[1] ) ) )
			break;

		}

	    }

	}

	return equals;

    }

    @Override
    public int hashCode()

    {

	int result = 0;

	for ( int i = 0; i < this.size(); i++ ) {

	    double[] xy = this.get( i );
	    String x = xy[0] + "";
	    String y = xy[1] + "";

	    result = ( 37 * result ) + x.hashCode();
	    result = ( 37 * result ) + y.hashCode();

	}
	return result;

    }

    @Override
    public boolean isEmpty()

    {

	return this._data.length() == 0;

    }

    @Override
    public Iterator<double[]> iterator()

    {

	return new DataIterator( this );

    }

    private class DataIterator
	implements Iterator<double[]>

    {

	private int _p = 0;
	private final Drawable _data;

	DataIterator( Drawable data )

	{

	    this._data = data;

	}

        @Override
	public boolean hasNext()

	{

	    return this._p < this._data.size();

	}

        @Override
	public double[] next()

	{

	    if ( this._p == this._data.size() )
		throw new NoSuchElementException();

	    return this._data.get( this._p++ );

	}

        @Override
	public void remove()

	{

	    // Not implemented.

	}

    }

    @Override
    public boolean remove( Object o )

    {

	return false;

    }

    @Override
    public boolean removeAll( Collection<?> c )

    {

	return false;

    }

    @Override
    public boolean retainAll( Collection<?> c )

    {

	return false;

    }

    @Override
    public int size()

    {

	return this._data.length();

    }

    @Override
    public Object[] toArray()

    {

	Object[] o = new Object[this._data.length()];

	for ( int i = 0; i < this._data.length(); i++ )
	    o[i] = ( Object ) this.get( i );

	return o;

    }

    @Override
    public <T> T[] toArray( T[] t )
    {

	double[][] a;

	if ( t instanceof double[][] )
	    a = new double[0][];
	else
	    a = ( double[][] ) t;

    	if ( a.length < this._data.length() )
    	    a = new double[this._data.length()][];

    	for ( int i = 0; i < this._data.length(); i++ )
    	    a[i] = this.get( i );

    	return ( T[] ) a;

    }

    // Below here be private methods.

    private CssColor getCssColor( double transparency )

    {

        return CssColor.make( "rgba(" +
			      this.getRed() + "," +
			      this.getGreen() + "," +
			      this.getBlue() + "," +
			      transparency + ")" );

    }

    private double distanceFrom( int index, double x, double y )

    {

	double[] xy = this.get( index );
	double dx = xy[0] - x;
	double dy = xy[1] - y;

	return Math.sqrt( ( dx * dx ) + ( dy * dy ) );

    }

    private void resampleArray( double[][] xy )

    {

        Drawable.resampleArray( xy, this, this.isClosed() );

        this.validate();

    }

    private static void resampleArray( double[][] xy,
                                       Collection<double[]> list,
                                       boolean closed )
    {

        Vector<double[]> tx = new Vector<double[]>();
        Vector<double[]> ty = new Vector<double[]>();

        tx.add( new double[]{ 0, xy[0][0] } );
        ty.add( new double[]{ 0, xy[0][1] } );

        double tlast = 0;

        int n = xy.length + ( closed ? 1 : 0 );

        for ( int I = 1, im1 = 0; I < n; im1 = I, I++ ) {

            int i = I % xy.length;

            double dx = xy[i][0] - xy[im1][0];
            double dy = xy[i][1] - xy[im1][1];

            double dt = Math.sqrt( ( dx * dx ) + ( dy * dy ) );

            if ( dt > 0 ) {

                double t = dt + tlast;

                tx.add( new double[]{ t, xy[i][0] } );
                ty.add( new double[]{ t, xy[i][1] } );

                tlast = t;

            }

        }

        try {

            LaGrangianInterpolator lgix =
		new LaGrangianInterpolator( tx.toArray( new double[0][] ) );
            LaGrangianInterpolator lgiy =
		new LaGrangianInterpolator( ty.toArray( new double[0][] ) );

            list.clear();

            list.add( new double[]{ xy[0][0], xy[0][1] } );

            for ( double t = 1; t < tlast; t++ ) {

                double x = lgix.evaluate( t );
                double y = lgiy.evaluate( t );

                list.add( new double[]{ x, y } );

            }

        } catch ( IllegalArgumentException iae ) {
            // Just eat it.
        }

    }

    private Boolean _closed = false;

    private void removeDuplicates()
    {

        double[][] xy = this.toArray( new double[0][] );

        this.clear();

        this.add( new double[]{ xy[0][0], xy[0][1] } );

        for ( int i0 = 0, i1 = 1; i1 < xy.length; i0 = i1, i1++ ) {

            double dx = xy[i1][0] - xy[i0][0];
            double dy = xy[i1][1] - xy[i0][1];
            double dss = ( dx * dx ) + ( dy * dy );

            if ( dss > 0 )
                this.add( new double[]{ xy[i1][0], xy[i1][1] } );

        }

    }

    private boolean removeIntersection()
    {

        double[][] xy = this.toArray( new double[0][] );

        Intersection intersection = this.findIntersection( xy );
        boolean intersected;

        if ( intersection != null ) {

	    intersected = true;

            int i1 = intersection.getI1();
            int I1 = ( i1 - 1 + xy.length ) % xy.length;
            int i2 = intersection.getI2();
            int I2 = ( i2 - 1 + xy.length ) % xy.length;

	    double x = intersection.getX();
	    double y = intersection.getY();

	    int count1 = this.countPoints( i1, I2, xy.length );
	    int count2 = this.countPoints( i2, I1, xy.length );

	    if ( count2 < count1 )
		this.edit( i1, I2, xy, x, y );
	    else
		this.edit( i2, I1, xy, x, y );

        } else
	    intersected = false;

        return intersected;

    }

    private int countPoints( int a, int b, int l )
    {

        int count = 0;

        while ( a != b ) {

            a = ( a + 1 ) % l;
            count++;

        }

        return count;

    }

    private void edit( int a, int b,
                       double[][] xy,
                       double x, double y )
    {

        this.clear();
	ArrayList<Integer> alist = new ArrayList<Integer>();

        do {

	    alist.add( a );
            a = ( a + 1 ) % xy.length;

        } while ( a != b );

	Integer[] ilist = alist.toArray( new Integer[0] );

	Arrays.sort( ilist );

        int p0 = ilist[0].intValue();
        this.add( new double[]{ xy[p0][0], xy[p0][1] } );

	for ( int i = 1; i < ilist.length; i++ ) {

	    int p = ilist[i].intValue();
            if ( ( p - p0 ) > 1 )
                this.add( new double[] { x, y } );

            this.add( new double[]{ xy[p][0], xy[p][1] } );
            p0 = p;

	}

    }

    private double[][] computeMB( double[][] xy )

    {

        int n = xy.length - 1;

        double[] mx = new double[n];
        double[] my = new double[n];

        double[] bx = new double[n];
        double[] by = new double[n];

        for ( int i0 = 0, i1 = 1; i1 < xy.length; i0 = i1, i1++ ) {

            mx[i0] = xy[i1][0] - xy[i0][0];
            my[i0] = xy[i1][1] - xy[i0][1];

            bx[i0] = xy[i0][0];
            by[i0] = xy[i0][1];

        }

	return new double[][] { mx, my, bx, by };

    }

    private Intersection findIntersection( double[][] xy )
    {

	double[][] mb = this.computeMB( xy );

        double[] mx = mb[0];
        double[] my = mb[1];

        double[] bx = mb[2];
        double[] by = mb[3];

        Intersection isect = null;
        for ( int I1 = 0; ( isect == null ) && ( I1 < mx.length ); I1++ ) {

            double mx1 = mx[I1];
            double my1 = my[I1];

            double bx1 = bx[I1];
            double by1 = by[I1];

            for ( int I2 = I1 + 2;
                  ( isect == null ) && ( I2 < mx.length - 1 );
                  I2++ ) {

                double mx2 = mx[I2];
                double my2 = my[I2];

                double bx2 = bx[I2];
                double by2 = by[I2];

                double top =
                       ( mx2 * ( by1 - by2 ) ) - ( my2 * ( bx1 - bx2 ) );
                double bottom =
                       ( mx1 * my2 ) - ( my1 * mx2 );

                if ( bottom != 0 ) {

                    double t1 = top / bottom;

                    if ( ( 0 <= t1 ) && ( t1 <= 1 ) ) {

                        double t2;

                        if ( mx2 != 0 )
                            t2 = ( ( mx1 * t1 ) + bx1 - bx2 ) / mx2;
                        else
                            t2 = ( ( my1 * t1 ) + by1 - by2 ) / my2;

                        if ( ( 0 <= t2 ) && ( t2 <= 1 ) )
                            isect = new Intersection( I1, I2,
						      ( mx1 * t1 ) + bx1,
						      ( my1 * t1 ) + by1 );

                    }

                }

            }

        }

        return isect;

    }

    private boolean isWithin( double[] xy, double x, double y, double rsqd )
    {

        double dx = x - xy[0];
        double dy = y - xy[1];

        double dsqd = ( dx * dx ) + ( dy * dy );

        return ( dsqd <= rsqd );

    }

    private static class Intersection
    {

        private final int _i1;
        private final int _i2;
        private final double _x;
        private final double _y;

        Intersection( int i1, int i2, double x, double y )
        {

            this._i1 = i1;
            this._i2 = i2;

            this._x = x;
            this._y = y;

        }

        int getI1()
        {

            return this._i1;

        }

        int getI2()
        {

            return this._i2;

        }

        double getX()
        {

            return this._x;

        }

        double getY()
        {

            return this._y;

        }

        @Override
        public String toString()
        {

            return "intersection: "
                + this._i1 + " " + this._i2 + " "
                + this._x + " " + this._y;

        }

    }

    private static class Data
	extends JavaScriptObject

    {

	protected Data() {}

	static native Data create()
	/*-{
	  return {
	  data: [],
	  }
	  }-*/;

	final native void add( double x, double y )
	/*-{
	  this.data.push( [x, y] );
	  }-*/;

        final native void insert( int index, double x, double y )
	/*-{
	  this.data.splice( index, 0, [x, y] );
	  }-*/;

	final native double getX( int i )
	/*-{
	  return this.data[i][0];
	  }-*/;

	final native double getY( int i )
	/*-{
	  return this.data[i][1];
	  }-*/;

	final native int length()
	/*-{
	  return this.data.length;
	  }-*/;

	final native void clear()
	/*-{
	  this.data = [];
	  }-*/;

    }

}
