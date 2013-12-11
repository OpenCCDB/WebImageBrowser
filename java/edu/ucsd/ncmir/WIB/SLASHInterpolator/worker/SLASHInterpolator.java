package edu.ucsd.ncmir.WIB.SLASHInterpolator.worker;

import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.webworker.client.DedicatedWorkerEntryPoint;
import com.google.gwt.webworker.client.MessageEvent;
import com.google.gwt.webworker.client.MessageHandler;
import edu.ucsd.ncmir.WIB.SLASHInterpolator.client.SLASHInterpolatorData;
import edu.ucsd.ncmir.spl.Graphics.ScanConvert;
import edu.ucsd.ncmir.spl.Graphics.contour_tracer.ContourTracer;
import edu.ucsd.ncmir.spl.LinearAlgebra.AbstractDoubleVector;
import edu.ucsd.ncmir.spl.LinearAlgebra.DoubleSquareMatrix;
import edu.ucsd.ncmir.spl.Mogrification.thin_plate_spline.ThinPlateSplineMogrifier;
import java.util.ArrayList;

public class SLASHInterpolator
    extends DedicatedWorkerEntryPoint
    implements MessageHandler,
	       RequestCallback

{

    public final static String NAME =
	"edu.ucsd.ncmir.WIB.SLASHInterpolator.SLASHInterpolator";

    @Override
    public void onMessage( MessageEvent event )

    {

	SLASHInterpolatorData sid =
	    ( SLASHInterpolatorData ) event.getDataAsJSO();

	int annotation_id = sid.getAnnotationID();
	String user_name = sid.getUserName();

	double centroid_x_0 = sid.getCentroidX( 0 );
	double centroid_y_0 = sid.getCentroidY( 0 );
	PlaneData apd = new PlaneData( sid.getX( 0 ), sid.getY( 0 ),
				       centroid_x_0, centroid_y_0 );
	int z0 = sid.getZ( 0 );

	double centroid_x_1 = sid.getCentroidX( 1 );
	double centroid_y_1 = sid.getCentroidY( 1 );
	PlaneData bpd = new PlaneData( sid.getX( 1 ), sid.getY( 1 ),
				       centroid_x_1, centroid_y_1 );
	int z1 = sid.getZ( 1 );

	double axmin = apd.getXMin();
	double axmax = apd.getXMax();
	double bxmin = bpd.getXMin();
	double bxmax = bpd.getXMax();

	double aymin = apd.getYMin();
	double aymax = apd.getYMax();
	double bymin = bpd.getYMin();
	double bymax = bpd.getYMax();

	double xmin = axmin > bxmin ? bxmin : axmin;
	double ymin = aymin > bymin ? bymin : aymin;

	double xmax = axmax < bxmax ? bxmax : axmax;
	double ymax = aymax < bymax ? bymax : aymax;

	double[][] adata = apd.getOffsetData( xmin - 1, ymin - 1 );
	double[][] bdata = bpd.getOffsetData( xmin - 1, ymin - 1 );

	int width = ( int ) ( Math.ceil( xmax ) - Math.floor( xmin ) ) + 2;
	int height = ( int ) ( Math.ceil( ymax ) - Math.floor( ymin ) ) + 2;

	double xc = ( xmax - xmin ) / 2;
	double yc = ( ymax - ymin ) / 2;

	double[][] af = new double[2][36];
	double[][] bf = new double[2][36];

	for ( int i = 0; i < 36; i++ ) {

	    double theta = Math.PI / 180.0 * i * 10;

	    double x = Math.cos( theta );
	    double y = Math.sin( theta );

	    this.chIntercept( xc, yc, x + xc, y + yc, adata, af, i );
	    this.chIntercept( xc, yc, x + xc, y + yc, bdata, bf, i );

	}

	PlaneConverter apc = new PlaneConverter( width, height, adata );
	apc.scanConvert();
	double[][] ascan = apc.getScan();

	PlaneConverter bpc = new PlaneConverter( width, height, bdata );
	bpc.scanConvert();
	double[][] bscan = bpc.getScan();

	double dx = centroid_x_1 - centroid_x_0;
	double dy = centroid_y_1 - centroid_y_0;
	double dz = z1 - z0;

	this._z0 = z0 + 1;
	this._zlast = z1 - 1;
	for ( int z = z0 + 1; z < z1; z++ ) {

	    double t0 = ( z - z0 ) / dz;

	    double[][] xy = new double[2][af[0].length];

	    for ( int i = 0; i < af[0].length; i++ ) {

		double mx = bf[0][i] - af[0][i];
		double my = bf[1][i] - af[1][i];

		xy[0][i] = ( t0 * mx ) + af[0][i];
		xy[1][i] = ( t0 * my ) + af[1][i];

	    }

            try {

		ThinPlateSplineMogrifier atpsm =
		    new ThinPlateSplineMogrifier( xy, af );
		ThinPlateSplineMogrifier btpsm =
		    new ThinPlateSplineMogrifier( xy, bf );

		byte[][] plane = new byte[height][width];

		double t1 = 1 - t0;

		for ( int j = 0; j < height; j++ )
		    for ( int i = 0; i < width; i++ ) {

			double[] pxy = new double[] { i, j };

			double[] axy = atpsm.mogrify( pxy );
			double[] bxy = btpsm.mogrify( pxy );

			double p1 =
			    ( ( ( 0 <= axy[0] ) && ( axy[0] < width ) ) &&
			      ( ( 0 <= axy[1] ) && ( axy[1] < height ) ) ) ?
			    ( ascan[( int ) axy[1]][( int ) axy[0]] ) : 0;
			double p2 =
			    ( ( ( 0 <= bxy[0] ) && ( bxy[0] < width ) ) &&
			      ( ( 0 <= bxy[1] ) && ( bxy[1] < height ) ) ) ?
			    ( bscan[( int ) bxy[1]][( int ) bxy[0]] ) : 0;

			if ( ( ( p1 * t1 ) + ( p2 * t0 ) ) == 1 )
                            plane[j][i] = 1;

		    }

		ContourTracer ct = new ContourTracer( plane );
		ArrayList<int[][]> traces = ct.trace( 1 );

		double xoffset = ( ( ( dx * t0 ) + centroid_x_0 ) - xc ) - 1;
		double yoffset = ( ( ( dy * t0 ) + centroid_y_0 ) - yc ) - 1;

		int[][] tr = new int[0][];

		for ( int i = 0; i < traces.size(); i++ ) {

		    int[][] ctr = traces.get( i );

		    if ( ctr.length > tr.length )
			tr = ctr;

		}

		double[] x = new double[tr.length];
		double[] y = new double[tr.length];

		for ( int i0 = tr.length - 3,
			  i1 = tr.length - 2,
			  i2 = tr.length - 1,
			  i3 = 0,
			  i4 = 1,
			  i5 = 2,
			  i6 = 3;
		      i3 < tr.length;
		      i0 = i1,
			  i1 = i2,
			  i2 = i3,
			  i3++,
			  i4 = i5,
			  i5 = i6,
			  i6 = ( i6 + 1 ) % tr.length ) {

		    x[i3] = 
			( tr[i0][0] +
			  tr[i1][0] + 
			  tr[i2][0] + 
			  tr[i3][0] + 
			  tr[i4][0] + 
			  tr[i5][0] + 
			  tr[i6][0] ) / 7.0;
		    y[i3] = 
			( tr[i0][1] +
			  tr[i1][1] + 
			  tr[i2][1] + 
			  tr[i3][1] + 
			  tr[i4][1] + 
			  tr[i5][1] + 
			  tr[i6][1] ) / 7.0;

		}
		String data = "";
		for ( int j = 0; j < x.length; j++ )
		    data +=
			( x[j] + xoffset ) + " " +
			( y[j] + yoffset ) + "\n";
		
		String url =
		    "../cgi-bin/SLASHdb.pl?" +
		    "request=add_contour&" +
		    "user_name=" + user_name + "&" +
		    "annotation_id=" + annotation_id + "&" +
		    "closed=true&" +
		    "z=" + z;

		RequestBuilder request =
		    new RequestBuilder( RequestBuilder.POST, url );

		request.sendRequest( data, this );

            } catch ( Throwable e ) {

		this.postMessage( "Exception: " + e.toString() );

            }

	}

    }

    private void chIntercept( double x0, double y0,
			      double x1, double y1,
			      double[][] outline,
			      double[][] xy, int p )

    {

	double mx0 = x1 - x0;
	double bx0 = x0;

	double my0 = y1 - y0;
	double by0 = y0;

	double tmax = 0;

	for ( int i = outline.length - 1, j = 0;
	      j < outline.length;
	      i = j, j++ )
	    try {

		double mx1 = outline[j][0] - outline[i][0];
		double bx1 = outline[i][0];

		double my1 = outline[j][1] - outline[i][1];
		double by1 = outline[i][1];

		DoubleSquareMatrix A =
		    new DoubleSquareMatrix( new double[][] {
			    { mx0, -mx1 },
			    { my0, -my1 }
			} );

		AbstractDoubleVector B =
		    new AbstractDoubleVector( new double[] {
			    bx1 - bx0,
			    by1 - by0
			} );

		AbstractDoubleVector t =
		    ( ( DoubleSquareMatrix ) A.inverse() ).multiply( B );

		double t0 = t.getComponent( 0 );
		double t1 = t.getComponent( 1 );

		if ( ( t0 > tmax ) && ( 0.0 <= t1 ) && ( t1 <= 1.0 ) ) {

		    tmax = t0;
		    xy[0][p] = ( t1 * mx1 ) + bx1;
		    xy[1][p] = ( t1 * my1 ) + by1;

		}

	    } catch ( Throwable t ) {

		// Don't worry about it, just eat it.

	    }

    }

    private int _z0;
    private int _zlast;

    @Override
    public void onResponseReceived( Request request, Response response )

    {

	this.count();

    }

    private void count()

    {

	this._z0++;

	if ( this._z0 == this._zlast ) {

	    this.postMessage( "complete" );
	    this.close();

	}

    }

    @Override
    public void onError( Request request, Throwable exception )
    {

	this.count();

    }

    private static class PlaneConverter
	extends ScanConvert

    {

	private double[][] _scan;

	PlaneConverter( int width, int height, double[][] data )

	{

            super( width, height, data );
	    this._scan = new double[height][width];

	}

        @Override
        public void handler( int x, int y )
        {

	    this._scan[y][x] = 1;

        }

	double[][] getScan()

	{

	    return this._scan;

	}

    }

    private static class PlaneData

    {

	private double[][] _data;
	private double _xmin = Double.MAX_VALUE;
	private double _ymin = Double.MAX_VALUE;
	private double _xmax = -Double.MAX_VALUE;
	private double _ymax = -Double.MAX_VALUE;

	PlaneData( JsArrayNumber x, JsArrayNumber y,
		   double centroid_x, double centroid_y )

	{

	    this._data = new double[x.length()][2];

	    double ax0 = centroid_x - 1;
	    double ay0 = centroid_y - 1;
	    for ( int i = 0; i < this._data.length; i++ ) {

		this._data[i][0] = x.get( i ) - ax0;
		this._data[i][1] = y.get( i ) - ay0;

		if ( this._xmin > this._data[i][0] )
		    this._xmin = this._data[i][0];
		if ( this._ymin > this._data[i][1] )
		    this._ymin = this._data[i][1];
		if ( this._xmax < this._data[i][0] )
		    this._xmax = this._data[i][0];
		if ( this._ymax < this._data[i][1] )
		    this._ymax = this._data[i][1];

	    }

	}

        public double[][] getOffsetData( double x0, double y0 )

        {

	    double[][] dout = new double[this._data.length][2];

	    for ( int i = 0; i < this._data.length; i++ ) {

		dout[i][0] = this._data[i][0] - x0;
		dout[i][1] = this._data[i][1] - y0;

	    }
	    return dout;

        }

        public double getXMin()

        {

            return this._xmin;

        }

        public double getYMin()

        {

            return this._ymin;

        }

        public double getXMax()

        {

            return this._xmax;

        }

        public double getYMax()

        {

            return this._ymax;

        }

    }

    @Override
    public void onWorkerLoad()

    {

	super.setOnMessage( this );

    }

}
