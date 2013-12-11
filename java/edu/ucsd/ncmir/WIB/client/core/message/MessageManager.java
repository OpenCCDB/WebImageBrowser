package edu.ucsd.ncmir.WIB.client.core.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * <code>MessageManager</code> is the backplane through which all
 * <code>Messages</code> are passed between modules.
 * @author spl
 */
public final class MessageManager

{

    static HashMap<Class,HashSet<MessageListener>> _listeners =
        new HashMap<Class,HashSet<MessageListener>>();

    /**
     * Registers a <code>MessageListener</code> for a
     * <code>Message<Class>.
     *
     * @param message_class The <code>Class</code> of the
     * <code>Message</code> listened for.
     * @param listener The <code>MessageListener</code> to be invoked when
     * the registered <code>Message</code> is sent.
     */

    public static void registerListener( Class<? extends Message> message_class,
					 MessageListener listener )

    {

	HashSet<MessageListener> listeners =
	    MessageManager._listeners.get( message_class );
	if ( listeners == null )
	    MessageManager._listeners.put( message_class,
					   listeners =
                                           new HashSet<MessageListener>() );

	listeners.add( listener );

    }

    /**
     * Convenience method to register a whole flock of listeners at onec.
     * @param listener The <code>MessageListener</code> to be invoked when
     * the registered <code>Message</code> is sent.
     * @param messages A varargs list of <code>Class</code> of the
     * <code>Message</code>s listened for.
     */

    public static void registerAsListener( MessageListener listener,
				           Class<? extends Message>... messages )

    {

        for ( Class message_class : messages )
            MessageManager.registerListener( message_class, listener );

    }

    /**
     * Deregisters a <code>MessageListener</code> for a
     * <code>Message<Class>.
     *
     * @param message_class The <code>Class</code> of the
     * <code>Message</code> listened for.
     * @param listener The <code>MessageListener</code> to be removed.
     */

    public static void deregisterListener( Class<? extends Message> message_class,
					   MessageListener listener )

    {

	HashSet<MessageListener> listeners =
	    MessageManager._listeners.get( message_class );
	if ( listeners != null )
	    listeners.remove( listener );

    }

    /**
     * Convenience method to deregister a whole flock of listeners at onec.
     * @param listener The <code>MessageListener</code> to be invoked when
     * the registered <code>Message</code> is sent.
     * @param messages A varargs list of <code>Class</code> of the
     * <code>Message</code>s listened for.
     */

    public static void deregisterAsListener( MessageListener listener,
					     Class... messages )

    {

	for ( Class message_class : messages )
	    MessageManager.deregisterListener( message_class, listener );

    }

    /**
     * Method invoked by a <code>Message</code> to send itself.
     *
     * @param message The message being sent.
     * @param o An arbitary <code>Object</code> to be passed to the
     * <code>MessageListener</code> <code>action</code> method.
     */

    static void send( Message message, Object o )

    {

	MessageManager.messenger( message.getClass(), message, o );

    }

    private static LinkedList<MessageData> _message_data =
	new LinkedList<MessageData>();

    static private void messenger( Class<? extends Message> c,
                                   Message message,
                                   Object o )

    {

	HashSet<MessageListener> listeners =
	    MessageManager._listeners.get( c );

	MessageManager._message_data.add( new MessageData( message,
                                                           listeners ) );

	// Keep the message list to a managable size.

	while ( MessageManager._message_data.size() > 20 )
	    MessageManager._message_data.poll();

	if ( listeners != null )
	    for ( MessageListener l :
		      listeners.toArray( new MessageListener[0] ) )
		l.action( message, o );

	Class sc = c.getSuperclass();

	if ( sc != Message.class )
	    MessageManager.messenger( sc, message, o );

    }

    public static MessageData[] getMessageData()

    {

        return MessageManager._message_data.toArray( new MessageData[0] );

    }

}
