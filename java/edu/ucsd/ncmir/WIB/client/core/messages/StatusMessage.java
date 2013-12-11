package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 * This <code>Message</code> exists for general status posting.
 *
 * @author spl
 */
public class StatusMessage
    extends Message

{

    private String _message = "";

    public StatusMessage( Object... list )

    {

	for ( Object obj : list )
	    this._message += obj.toString() + " ";

    }

    public String getMessage()

    {

        return this._message;

    }

}
