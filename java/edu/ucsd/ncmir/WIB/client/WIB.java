package edu.ucsd.ncmir.WIB.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.debug.Debug;

/**
 * Main entry point.
 *
 * @author spl
 */
public class WIB
    implements EntryPoint,
	       GWT.UncaughtExceptionHandler

{
	
	public static String servicePrefix = "http://ccdb-dev-portal.crbs.ucsd.edu/WebImageBrowserW/";

    /**
     * Creates a new instance of WIB
     */

    public WIB()

    {

	GWT.setUncaughtExceptionHandler( this );

    }

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    @Override
    public void onModuleLoad()

    {

	if ( Canvas.isSupported() )	    
	    GWT.runAsync( Configuration.getPlugin() );
	else
	    Window.alert( "Sorry. . . Canvas not supported.\n" +
	 		  "Please use a recent version of\n" +
	 		  "Google Chromium/Chrome,\n" +
	 		  "Firefox, Safari, or Opera to run\n" +
	 		  "the Web Image Browser." );
	
    }

    private static int _timeout;

    public static int getTimeoutSeconds()

    {

        return WIB._timeout;

    }

    public static void setTimeoutSeconds( int timeout )

    {

        WIB._timeout = timeout;

	if ( Cookies.isCookieEnabled() )
	    Cookies.setCookie( "timeout", timeout + "" );

    }

    @Override
    public void onUncaughtException( Throwable e )

    {

	this.death( e );

    }

    private void death( Throwable e )

    {

	Debug.traceback( e );
        Debug.message( "An error has occurred:" );
        Debug.message( "You may enter text here to include " +
                       "in your bug report:\n\n\n\n\n" );

    }


}
