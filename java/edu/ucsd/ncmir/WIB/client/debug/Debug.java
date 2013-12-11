package edu.ucsd.ncmir.WIB.client.debug;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.core.components.InfoDialog;
import edu.ucsd.ncmir.WIB.client.core.components.ResizableDialogBox;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageData;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.StatusMessage;
import edu.ucsd.ncmir.WIB.client.core.panel.InteractionPanel;
import edu.ucsd.ncmir.WIB.client.core.request.AbstractRequestCallback;
import edu.ucsd.ncmir.WIB.client.core.request.HTTPRequest;
import edu.ucsd.ncmir.spl.XMLUtil.XML;
import java.util.Date;

/**
 * A window for debug messages. Not ordinarily displayed in production code.
 *
 * @author spl
 */
public class Debug
{

    private static TextArea _text = new TextArea();
    private static boolean _inited = false;

    static {

        Debug._db = new ResizableDialogBox( false, false, true, true, false );
        Debug._db.setText( "Debug" );
        Debug._db.setGlassEnabled( false );

        Debug._db.setWidth( "1024px" );
        Debug._db.setHeight( "500px" );
        Debug._text.setWidth( "1024px" );
        Debug._text.setHeight( "450px" );

        Grid g = new Grid( 2, 1 );
        Debug._db.add( g );
        Grid g1 = new Grid( 1, 2 );
        g.setWidget( 0, 0, g1 );

        Button email_button = new Button( "Click Here to Send Bug Report" );
	Debug._email_handler = new EmailHandler( Debug._text );
        email_button.addClickHandler( Debug._email_handler );
        g1.setWidget( 0, 0, email_button );

        g.setWidget( 1, 0, Debug._text );
        Debug._db.setPopupPosition( 50, 50 );

    }

    /**
     * Creates a
     * <code>Debug</code>.
     */
    private static DialogBox _db;

    private static EmailHandler _email_handler;

    public static void init( InteractionPanel ip )
    {

        ip.add( Debug._db );
	Debug._inited = true;
	Debug._db.hide();
	Debug._db.setVisible( false );

    }

    private static void message( String message )

    {

        if ( message.toLowerCase().startsWith( "<!doctype html" ) ) {

            HTML html = new HTML( message );
            DialogBox db = new DialogBox( true, false );
            db.add( html );
            db.show();

        } else {

            String text = message;

            text += "\n" + Debug._text.getValue();

            Debug._text.setValue( text );

	    if ( Debug._inited ) {

		Debug._db.show();
		Debug._db.setVisible( true );

	    } else {

		RootPanel rp = RootPanel.get();

		rp.add( Debug._db );
                Debug._inited = true;

	    }

        }

    }

    private static long _t0 = -1;
    private static long _t = -1;
    
    /**
     *
     * @param list
     */
    public static void timedMessage( Object... list )

    {

	long t = new Date().getTime();

	if ( Debug._t0 == -1 )
	    Debug._t0 = Debug._t = t;

	long dt = t - Debug._t;
	long xdt = t - Debug._t0;

	Debug.message( t + " " +
		       Debug.nfmt( dt, 5 ) + " " +
		       Debug.nfmt( xdt, 6 ) + ":",
		       Debug.expand( list ) );
	Debug._t = t;

    }

    private static String nfmt( long n, int l )

    {

	String s = "" + n;

	while ( s.length() < l )
	    s = " " + s;

	return s;

    }

    public static void message( Object... list )

    {

        Debug.message( Debug.expand( list ) );

    }

    private static String expand( Object[] list )

    {

        String message = "";
        for ( Object obj : list )
            message += ( obj != null ? obj.toString() : "null" ) + " ";

	return message;

    }

    private static int _level = 0;

    public static void traceback( Throwable e )

    {

        if ( e != null ) {

            String message = "\n\n" +
                "Level: " +
                Debug._level++ + "\nException: " + e.getMessage() + "\n" +
                "Class: " + e.getClass() + "\n\n" +
                "****** Begin Traceback ******\n";

            for ( StackTraceElement ste : e.getStackTrace() )
                message += "\n  " + ste.toString();

            message += "\n****** End Traceback ******\n\n";

            if ( e instanceof JavaScriptException ){

                JavaScriptException jse = ( JavaScriptException ) e;

                message += "Begin JavaScriptException\n";

                message += "Description:\n";
                message += jse.getDescription() + "\n";
                message += "Exception:\n";
                message += jse.getException() + "\n";
                message += "Name:\n";
                message += jse.getName() + "\n";


                message += "\nEnd JavaScriptException\n\n";

            }

            Debug.message( message );

            if ( e instanceof UmbrellaException )
                for ( Throwable t : ( ( UmbrellaException ) e ).getCauses() )
                    Debug.traceback( t );

        }
        Debug._level--;

    }

    public static void showXMLErrorMessage( String title,
                                            String message,
                                            Element root )
    {

        Element error = XML.find( root, "error" );
        Text text = ( Text ) error.getFirstChild();

        Debug.message( text.getData() );
        Debug.message( message );
        Debug.message( title );

    }

    /**
     * Used primarily for debugging purposes, to automatically send an
     * error report.
     */
    public static void automaticallySendEmail()

    {

	Debug._email_handler.sendDebugReport();

    }


    private static class EmailHandler
        extends AbstractRequestCallback
        implements ClickHandler,
		   MessageListener,
		   RequestCallback

    {

        private final TextArea _text;

        EmailHandler( TextArea text )
        {

            this._text = text;
            MessageManager.registerListener( StatusMessage.class, this );

        }

        private String _status = "Uninitialized";

        @Override
        public void action( Message m, Object o )
        {

            this._status = ( ( StatusMessage ) m ).getMessage();

        }

        @Override
        public void onClick( ClickEvent event )
        {

	    this.sendDebugReport();

	}

	public void sendDebugReport()

	{

            String s = "";

	    try {

		s += Configuration.getPlugin().getState();

	    } catch ( Throwable t ) {

		s += "Unable to get plugin state!";

	    }
	    s += "\n" +
		"User Agent: " + Window.Navigator.getUserAgent() + "\n" +
		"App Code Name: " + Window.Navigator.getAppCodeName() + "\n" +
		"App Name: " + Window.Navigator.getAppName() + "\n" +
		"App Version: " + Window.Navigator.getAppVersion() + "\n" +
		"Platform: " + Window.Navigator.getPlatform() + "\n" +
		"Cookie Enabled: " + Window.Navigator.isCookieEnabled() + "\n" +
		"Java Enabled: " + Window.Navigator.isJavaEnabled() + "\n" +
		"Host: " + Window.Location.getHost() + "\n" +
		"HostName: " + Window.Location.getHostName() + "\n" +
		"Href: " + Window.Location.getHref() + "\n" +
		"QueryString: " + Window.Location.getQueryString() + "\n" +
		"\n" +
		"Status: " + this._status + "\n" +
		this._text.getValue() + "\n" +
		this.getMessages();

            HTTPRequest.post( "cgi-bin/aieee.cgi", s, this );

        }

        private String getMessages()
        {

            String s = "\n";

	    for ( MessageData md : MessageManager.getMessageData() )
		s += "-------------------\n" + md.toString() + "\n";

            return s;

        }

        @Override
        protected void handleResponse( String data )

        {

            HTML html = new HTML( data.trim() );
            InfoDialog db = new InfoDialog();

            db.addTitle( "Bug Report" );
            db.addWidgets( new Label( "Server response:" ), html );
            db.displayCenteredMessage();

        }

    }

}
