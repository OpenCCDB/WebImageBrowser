package edu.ucsd.ncmir.WIB.client.core.message;

/**
 *
 * @author spl
 */
public class Message

{

    /**
     * Creates a <code>Message</code> object.
     */

    public Message() {}

    /**
     * Sends this <code>Message</code> with a <code>null</code>
     * <code>Object</code> argument.
     */

    public void send()

    {

	this.send( null );

    }

    /**
     * Sends this <code>Message</code> with a user-supplied argument.
     * @param o The <code>Object</code> to be sent to
     * <code>Listener</code>s, if any.
     */

    public void send( Object o )

    {

	MessageManager.send( this, o );

    }

    /**
     * Determines whether this <code>Message</code> is a superclass of
     * the argument object.
     * @param oc
     * @return
     */
    public boolean isInstance( Object oc )

    {

	boolean is_instance = false;

	if ( oc instanceof Class ) {

	    Class c = ( Class ) oc;
	    Class o = this.getClass();
	    
	    while ( ( o != null ) &&
		    !( is_instance = ( c == o ) ) )
		o = o.getSuperclass();
		    
	}

	return is_instance;
        
    }

}