package edu.ucsd.ncmir.WIB.client.core.request;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

/**
 *
 * @author spl
 */
public abstract class AbstractRequestCallback
    implements RequestCallback

{

    private String _request_id;

    /**
     * Sets the <code>RequestCallback</code>'s identification for
     * debugging purposes.
     * @param request_id The ID.
     */
    public final void setRequestID( String request_id )

    {

        this._request_id = request_id;

    }

    /**
     * Returns the ID of the request: usually the URL.
     * @return The ID.
     */
    public final String getRequestID()

    {

        return this._request_id;

    }

    /**
     * Handles the response data.
     * @param data Whatever the server sent us.
     */
    protected abstract void handleResponse( String data );

    @Override
    public final void onResponseReceived( Request request, Response response )

    {
    	System.err.println("-----------RESPONSE:"+response.getText());

	this.handleResponse( response.getText() );

    }

    @Override
    public final void onError( Request request, Throwable exception )

    {

	this.handleError( exception );

    }

    /**
     * Allows the application to handle an error, if need be.
     * @param exception The Exception
     */
    protected void handleError( Throwable exception )

    {

        // Does nothing.

    }

}
