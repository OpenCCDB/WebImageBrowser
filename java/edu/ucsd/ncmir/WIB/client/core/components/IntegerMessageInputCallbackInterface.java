package edu.ucsd.ncmir.WIB.client.core.components;

/**
 *
 * @author spl
 */
public interface IntegerMessageInputCallbackInterface

{

    /**
     * Called by the <code>IntegerMessageInputBox<code> to set or update a value.
     * @param value The value.
     * @return <code>true</code> if valid, else <code>false</code>.
     */
    public boolean setValue( int value );

    /**
     * Called by the <code>IntegerMessageInputBox<code> to fetch a current value.
     * @return The value.
     */
    public int getValue();

}
