package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class UpdatePlanePositionMessage
    extends Message

{

    /**
     * We must type mangle thanks to autoboxing.
     */

    @Override
    public void send( Object o )

    {

	Integer i;

	if ( o instanceof Double )
	    i = new Integer( ( ( Double ) o ).intValue() );
	else
	    i = ( Integer ) o;

	super.send( i );

    }

}
