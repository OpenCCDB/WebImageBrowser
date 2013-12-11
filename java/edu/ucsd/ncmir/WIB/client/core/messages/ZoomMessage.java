package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class ZoomMessage
    extends Message

{

    @Override
    public void send( Object o )

    {

	if ( o instanceof Double )
	    o = new ScaleFactor( ( ( Double ) o ).doubleValue() );

	super.send( o );

    }

}
