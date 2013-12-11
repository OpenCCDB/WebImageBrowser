package edu.ucsd.ncmir.WIB.client.core.request;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import edu.ucsd.ncmir.WIB.client.debug.Debug;

/**
 *
 * @author spl
 */
public class HTTPRequest
    extends RequestBuilder
    implements RequestCallback

{

    /**
     * Instantiates the request.
     * @param method The method by which the request is to be made.
     * @param url The <code>URL</code> of the web page to be queried.
     */
    private HTTPRequest( Method method, String url )

    {

        super( method, url );

    }

    private static Request request( Method method, String url,
                                    String data,
				    AbstractRequestCallback callback )

    {

	if ( callback != null )
	    callback.setRequestID( url );

	HTTPRequest request = new HTTPRequest( method, url );

	return request.sendRequest( data, callback );

    }

    /**
     * Issues an HTTP <code>GET</code>.
     * @param url The <code>URL</code> of the web page to be queried.
     * @return A <code>Request</object> by which this HTTP request may
     * be managed.
     */
    public static Request get( String url )

    {

        return HTTPRequest.get( url, null );

    }

    /**
     *
     * Issues an HTTP <code>GET</code>.
     * @param url The <code>URL</code> of the web page to be queried.
     * @param callback A callback to be issued issued when this
     * request completes.
     * @return A <code>Request</object> by which this HTTP request may
     * be managed.
     */
    public static Request get( String url,
			       AbstractRequestCallback callback )

    {

        return HTTPRequest.request( HTTPRequest.GET, url, null, callback );

    }

    /**
     *
     * Issues an HTTP <code>POST</code>.
     * @param url The <code>URL</code> of the web page to be queried.
     * @param data Data to be sent to the web server.
     * @param callback A callback to be issued issued when this
     * request completes.
     * @return A <code>Request</object> by which this HTTP request may
     * be managed.
     */
    public static Request post( String url,
				String data,
				AbstractRequestCallback callback )

    {

        return HTTPRequest.request( HTTPRequest.POST, url, data, callback );

    }

    /**
     *
     * Issues an HTTP <code>POST</code>.
     * @param url The <code>URL</code> of the web page to be queried.
     * @param data Data to be sent to the web server.
     * @return A <code>Request</object> by which this HTTP request may
     * be managed.
     */
    public static Request post( String url, String data )

    {

        return HTTPRequest.post( url, data, null );

    }

    @Override
    public final Request sendRequest( String data, RequestCallback callback )

    {

	Request r = null;

	try {

		System.err.println("--------------sendRequest:"+data);
	    r = super.sendRequest( data,
				   ( callback != null ) ? callback : this );

	} catch ( Throwable t ) {

	    Debug.traceback( t );

	}

	return r;

    }

    @Override
    public final void onResponseReceived( Request request, Response response )

    {

	// Does nothing.

    }

    @Override
    public final void onError( Request request, Throwable exception )

    {

	Debug.traceback( exception );

    }

}