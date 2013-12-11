package edu.ucsd.ncmir.WIB.client.core.request;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import edu.ucsd.ncmir.WIB.client.debug.Debug;

/**
 *
 * @author spl
 */
public abstract class AbstractXMLRequestCallback
    extends AbstractRequestCallback
{

    @Override
    protected final void handleResponse( String data )

    {

    	System.err.println("----------handleResponse\n"+data);
    	
	// Because of IE brain damage.

        data = data.replaceAll( "<\\?[^>]+>", "" ).trim();

	try {

	    Element root = ( Element ) XMLParser.parse( data ).getFirstChild();

	    if ( root != null )
		this.handleXMLResponse( root );
	    else
		Debug.message( "null root element!" );

	} catch ( Throwable t ) {

            Debug.traceback( t );
	    Debug.message( "data-----------"+data+"????" );
	    Debug.message( "Bad XML Request2: " + super.getRequestID()  );


	}

    }

    /**
     * The normal response path.
     *
     * @param root The response, converted to XML.
     */
    protected abstract void handleXMLResponse( Element root );

}
