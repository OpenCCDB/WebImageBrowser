package edu.ucsd.ncmir.WIB.SLASHWorker.worker;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.webworker.client.DedicatedWorkerEntryPoint;
import com.google.gwt.webworker.client.MessageEvent;
import com.google.gwt.webworker.client.MessageHandler;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerAnnotation;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerPointData;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerPointDataList;
import edu.ucsd.ncmir.WIB.SLASHWorker.client.SLASHWorkerReturnData;
import edu.ucsd.ncmir.spl.MiniXML.Element;
import edu.ucsd.ncmir.spl.MiniXML.JDOMException;
import edu.ucsd.ncmir.spl.MiniXML.SAXBuilder;
import java.util.List;

public class SLASHWorker
    extends DedicatedWorkerEntryPoint
    implements MessageHandler,
	       RequestCallback

{

    public static final String NAME =
	"edu.ucsd.ncmir.WIB.SLASHWorker.SLASHWorker";
	
    private String _url;
    private int _retries;

    @Override
    public void onMessage( MessageEvent event )

    {

	this._url = event.getDataAsString();

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

	this.postMessage( SLASHWorkerReturnData.create( traceback ) );

    }

    @Override
    public void onWorkerLoad()

    {

	super.setOnMessage( this );

    }

    @Override
    public void onResponseReceived( Request request, Response response )

    {

	try {

	    String data = response.getText();

	    if ( ( data != null ) && data.length() > 0 )
		this.process( data );
	    else {

		if ( this._retries < 5 )
		    this.issueRequest();
		else {

		    String message = "Error handling URL " + this._url;

		    this.postMessage( SLASHWorkerReturnData.create( message ) );

		}

	    }

	} catch ( Throwable t ) {

	    this.postThrowableMessage( t );

	}

    }

    private void process( String data )
        throws JDOMException

    {

	Element root = new SAXBuilder().build( data ).getRootElement();

	if ( root.getAttributeValue( "status" ).equals( "success" ) ) {

	    List<Element> annotations = root.getChildren( "geometry" );

	    SLASHWorkerPointDataList ccipdl = SLASHWorkerPointDataList.create();

	    for ( int ann = 0; ann < annotations.size(); ann++ ) {

		Element a = annotations.get( ann );

		int annotation_id =
		    Integer.parseInt( a.getAttributeValue( "annotation_id" ) );
		int geometry_id =
		    Integer.parseInt( a.getAttributeValue( "geometry_id" ) );

		SLASHWorkerAnnotation p =
		    SLASHWorkerAnnotation.create( annotation_id );

		p.setGeometryID( geometry_id );

		String geometry_string = a.getValue();
		String[] string_array =
		    geometry_string.trim().split( "\n" );

		JsArrayNumber xa =
		    ( JsArrayNumber ) JavaScriptObject.createArray();
		JsArrayNumber ya =
		    ( JsArrayNumber ) JavaScriptObject.createArray();

		double xmin = Double.MAX_VALUE;
		double ymin = Double.MAX_VALUE;
		double xmax = -Double.MAX_VALUE;
		double ymax = -Double.MAX_VALUE;
		for ( int i = 0; i < string_array.length; i++ ) {

		    String[] t = string_array[i].trim().split( " " );

		    double x = Double.parseDouble( t[0] );
		    double y = Double.parseDouble( t[1] );

		    if ( xmin > x )
			xmin = x;
		    if ( ymin > y )
			ymin = y;
		    if ( xmax < x )
			xmax = x;
		    if ( ymax < y )
			ymax = y;

		    xa.push( x );
		    ya.push( y );

		}

		p.setPoints( xa, ya );

		p.setGeometryID( geometry_id );

		int z = Integer.parseInt( a.getAttributeValue( "z" ) );

		p.setBoundingBox( xmin, ymin, xmax, ymax );

		ccipdl.push( SLASHWorkerPointData.create( z, p ) );

	    }

	    this.postMessage( SLASHWorkerReturnData.create( this._url,
							    ccipdl ) );

	} else
	    this.postMessage( SLASHWorkerReturnData.create( data ) );

    }

    @Override
    public void onError( Request request, Throwable exception )

    {

	this.postThrowableMessage( exception );

    }

}
