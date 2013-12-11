package edu.ucsd.ncmir.WIB.SLASHGeometryWorker.worker;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.webworker.client.DedicatedWorkerEntryPoint;
import com.google.gwt.webworker.client.MessageEvent;
import com.google.gwt.webworker.client.MessageHandler;
import edu.ucsd.ncmir.WIB.SLASHGeometryWorker.client.SLASHGeometryWorkerReturnData;
import edu.ucsd.ncmir.spl.MiniXML.Element;
import edu.ucsd.ncmir.spl.MiniXML.JDOMException;
import edu.ucsd.ncmir.spl.MiniXML.SAXBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SLASHGeometryWorker
    extends DedicatedWorkerEntryPoint
    implements MessageHandler,
               RequestCallback

{

    public final static String NAME =
	"edu.ucsd.ncmir.WIB.SLASHGeometryWorker.SLASHGeometryWorker";

    @Override
    public void onWorkerLoad()

    {

	super.setOnMessage( this );

    }

    private int _retries;
    private String _url;

    @Override
    public void onMessage( MessageEvent event )

    {

	this._url = "../" + event.getDataAsString();

	this._retries = 0;
	this.issueRequest();

    }

    private void issueRequest()

    {

	try {

	    RequestBuilder rb =
		new RequestBuilder( RequestBuilder.GET, this._url );

	    rb.sendRequest( null, this );
	    this._retries++;

	} catch ( Throwable t ) {

	    this.postThrowableMessage( t );

	}

    }

    private void postThrowableMessage( Throwable t )

    {

	String traceback = t.toString() + "\n";
	StackTraceElement[] ste = t.getStackTrace();

	for ( int i = 0; i < ste.length; i++ )
	    traceback += "\n" + ste[i].toString();

	this.postMessage( SLASHGeometryWorkerReturnData.create( traceback ) );

    }

    @Override
    public void onResponseReceived( Request request, Response response )

    {

	try {

	    String data = response.getText();

	    if ( ( data != null ) && data.length() > 0 ) {

		SLASHGeometryWorkerReturnData sgwrd = this.process( data );
		this.postMessage( sgwrd );

            } else {

		if ( this._retries < 5 )
		    this.issueRequest();
		else {

		    String message = "Error handling URL " + this._url;

		    this.postMessage( SLASHGeometryWorkerReturnData.create( message ) );

		}

	    }

	} catch ( Throwable t ) {

	    this.postThrowableMessage( t );

	}
	this.close();

    }

    private SLASHGeometryWorkerReturnData process( String data )
        throws JDOMException

    {

	Element root = new SAXBuilder().build( data ).getRootElement();
	SLASHGeometryWorkerReturnData sgwrd = null;

	if ( root.getAttributeValue( "status" ).equals( "success" ) ) {

	    Element e;

	    if ( ( e = root.getChild( "surface" ) ) != null )
		sgwrd = this.processSurface( e );
	    else if ( ( e = root.getChild( "contours" ) ) != null )
		sgwrd = this.processContours( e );

	} else
	    sgwrd =
		SLASHGeometryWorkerReturnData.
		create( "Error: " + root.getAttributeValue( "reason" ) );

        return sgwrd;

    }

    private SLASHGeometryWorkerReturnData processSurface( Element surface )

    {

	SLASHGeometryWorkerReturnData sgwrd;

	List<Element> list = surface.getChildren( "indexed_triangles" );
	
	if ( list.size() > 0 ) {

	    int color = surface.getAttributeValueInt( "color" );

	    JsArray<JsArrayNumber> points =
		( JsArray<JsArrayNumber> ) 
		JavaScriptObject.createArray().cast();
	    JsArray<JsArrayNumber> normals =
		( JsArray<JsArrayNumber> ) 
		JavaScriptObject.createArray().cast();
	    JsArray<JsArrayInteger> indices =
		( JsArray<JsArrayInteger> )
		JavaScriptObject.createArray().cast();
	    
	    for ( Element indexed_triangles : list ) {
		
		Element vertices_element = 
		    indexed_triangles.getChild( "vertices" );
		int nvertices = vertices_element.getAttributeValueInt( "n" );
		Element triangles_element =
		    indexed_triangles.getChild( "triangles" );
		int ntriangles = triangles_element.getAttributeValueInt( "n" );
		
		JsArrayNumber v =
		    ( JsArrayNumber ) JavaScriptObject.createArray().cast();
		JsArrayNumber n =
		    ( JsArrayNumber ) JavaScriptObject.createArray().cast();
		
		String[] vlist = vertices_element.getValue().split( "\n" );
		
		for ( int i = 0; i < nvertices; i++ ) {
		    
		    String[] parts = vlist[i].trim().split( " " );

		    for ( int p = 0, q = 3; p < 3; p++, q++ ) {

			v.push( Double.parseDouble( parts[p] ) );
			n.push( Double.parseDouble( parts[q] ) );

		    }
		    
		}
		points.push( v );
		normals.push( n );
		
		JsArrayInteger t =
		    ( JsArrayInteger ) JavaScriptObject.createArray().cast();
		
		String[] tlist = triangles_element.getValue().split( "\n" );
		
		for ( int i = 0; i < ntriangles; i++ ) {
		    
		    String[] parts = tlist[i].trim().split( " " );
		    
		    for ( int p = 0; p < 3; p++ ) 
			t.push( Integer.parseInt( parts[p] ) );
		    
		}
		indices.push( t );

	    }
	    sgwrd = SLASHGeometryWorkerReturnData.create( color,
							  points,
							  normals,
							  indices );

	} else
	      sgwrd = SLASHGeometryWorkerReturnData.create( "No geometry." );

	return sgwrd;

    }

    @Override
    public void onError( Request request, Throwable exception )

    {


    }

    private static class ContourData

    {

	private final String _geometry_type;
	private final JsArrayNumber _x;
	private final JsArrayNumber _y;

	ContourData( String geometry_type,
		     JsArrayNumber x,
		     JsArrayNumber y )

	{

	    this._geometry_type = geometry_type;
	    this._x = x;
	    this._y = y;

	}

        private String getGeometryType()

        {

	    return this._geometry_type;

        }

        private JsArrayNumber getX()

        {

	    return this._x;

        }

        private JsArrayNumber getY()

        {

	    return this._y;

        }

    }

    private SLASHGeometryWorkerReturnData processContours( Element contours )

    {

	int color = contours.getAttributeValueInt( "color" );

	List<Element> contour_list = contours.getChildren( "contour" );

	HashMap<Integer,ArrayList<ContourData>> zmap =
	    new HashMap<Integer,ArrayList<ContourData>>();

	for ( Element contour : contour_list ) {

	    int z_index = contour.getAttributeValueInt( "z_index" );
	    String geometry_type = contour.getAttributeValue( "geometry_type" );

	    String[] poly = contour.getValue().split( "\n" );

	    JsArrayNumber x =
		( JsArrayNumber ) JavaScriptObject.createArray().cast();
	    JsArrayNumber y =
		( JsArrayNumber ) JavaScriptObject.createArray().cast();

	    for ( int i = 0; i < poly.length; i++ ) {

		String[] xy = poly[i].trim().split( " " );

		x.push( Double.parseDouble( xy[0] ) );
		y.push( Double.parseDouble( xy[1] ) );

	    }

	    ArrayList<ContourData> zvals = zmap.get( z_index );

	    if ( zvals == null )
		zmap.put( z_index, zvals = new ArrayList<ContourData>() );

	    zvals.add( new ContourData( geometry_type, x, y ) );

	}

	JsArrayInteger z_index =
	    ( JsArrayInteger ) JavaScriptObject.createArray().cast();
	JsArray<JsArrayInteger> types =
	    ( JsArray<JsArrayInteger> ) JavaScriptObject.createArray().cast();
	JsArray<JsArray<JsArrayNumber>> contourx =
	    ( JsArray<JsArray<JsArrayNumber>> )
	    JavaScriptObject.createArray().cast();
	JsArray<JsArray<JsArrayNumber>> contoury =
	    ( JsArray<JsArray<JsArrayNumber>> )
	    JavaScriptObject.createArray().cast();

	for ( Integer z : zmap.keySet() ) {

	    z_index.push( z.intValue() );

	    ArrayList<ContourData> cdlist = zmap.get( z );
	    JsArrayInteger tlist =
		( JsArrayInteger ) JavaScriptObject.createArray().cast();
	    JsArray<JsArrayNumber> xlist =
		( JsArray<JsArrayNumber> )
		JavaScriptObject.createArray().cast();
	    JsArray<JsArrayNumber> ylist =
		( JsArray<JsArrayNumber> )
		JavaScriptObject.createArray().cast();

	    for ( ContourData cd : cdlist ) {

		tlist.push( cd.getGeometryType().equals( "polyline" ) ? 0 : 1 );
		xlist.push( cd.getX() );
		ylist.push( cd.getY() );

	    }
	    types.push( tlist );
	    contourx.push( xlist );
	    contoury.push( ylist );

	}

	return SLASHGeometryWorkerReturnData.create( color,
						     z_index,
						     types,
						     contourx, contoury );
    }

}