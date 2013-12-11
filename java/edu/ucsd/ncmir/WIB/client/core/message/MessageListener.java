package edu.ucsd.ncmir.WIB.client.core.message;

/**
 * This interface defines the <code>MessageListener</code>'s callback action.
 *
 * @author spl
 */
public interface MessageListener

{

    /**
     * A method to be called with the <code>MessageListener</code> is actuated
     * by a <code>Message</code> of the appropriate type.
     * @param m The initiating <code>Message</code>.
     * @param o An arbitrary <code>Object</code> passed via the
     * <code>Message</code>'s <code>send()</code> method.  This
     * <code>Object</code> may be <code>null</code>.  It is up to the
     * <code>MessageListener</code> implementation to determine this and act
     * accordingly.
     */
    public void action( Message m, Object o );

}
