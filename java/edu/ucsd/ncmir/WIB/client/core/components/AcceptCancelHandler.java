package edu.ucsd.ncmir.WIB.client.core.components;

/**
 *
 * @author spl
 */
public interface AcceptCancelHandler

{

    /**
     * Called when either the Accept or Cancel buttons are clicked.
     * @param accepted <code>true</code> if the Accept button clicked,
     * <code>false</code> if Cancel clicked.
     */

    public boolean onAcceptCancelAction( boolean accepted );

}
