package edu.ucsd.ncmir.WIB.client.core.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Internal data for debugging purposes.
 * @author spl
 */
public class MessageData

{

    private final String _message_class;
    private final ArrayList<String> _listeners = new ArrayList<String>();

    private final Date _date = new Date();
    private final StackTraceElement[] _ste = new Exception().getStackTrace();

    MessageData( Message message, HashSet<MessageListener> listeners )

    {

	this._message_class = message.getClass().toString();

	if ( listeners != null )
	    for ( MessageListener li : listeners )
		this._listeners.add( li.getClass().toString() );
	else
	    this._listeners.add( "---- no listeners ----" );

    }

    /**
     * @return the message class
     */

    public String getMessageClass()

    {

        return this._message_class;

    }

    /**
     * @return the _listeners
     */

    public String[] getListeners()

    {

        return this._listeners.toArray( new String[0] );

    }

    public boolean isBefore( Date now )

    {

        return this._date.compareTo( now ) <= 0;

    }
    
    /**
     * @return the _date
     */

    public String getTime()

    {

	String[] time = this._date.toString().split( " " );

	return time[3] +
	    "." + MessageData.tformatter( this._date.getTime() % 1000 );

    }

    private static String tformatter( long t )

    {

	String s = t + "";

	while ( s.length() < 3 )
	    s = "0" + s;

	return s;

    }

    /**
     * @return the stack trace.
     */

    public String getStackTrace()

    {

	String st = "";

	for ( int i = 1; i < this._ste.length; i++ )
	    st += this._ste[i].toString() + "\n";

        return st;

    }

    @Override
    public String toString()

    {

	String s = "Message: " + this.getMessageClass();

	s += " " + this.getTime();

	s += "\n\nListeners:\n";

	for ( String l : this.getListeners() )
	    s += "\n" + l;

	s += "\n\nTraceback:\n\n" + this.getStackTrace();

	return s;

    }

}
