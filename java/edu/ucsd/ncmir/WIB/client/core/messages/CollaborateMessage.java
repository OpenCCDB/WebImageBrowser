package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.components.menu.StateChangeMessageCommand;

/**
 *
 * @author spl
 */
public class CollaborateMessage
    extends StateChangeMessageCommand

{

    /**
     * Creates a <code>CollaborateMessage</code>.
     * @param state Initial state.
     */
    public CollaborateMessage( boolean state )
        
    {
        
        super( state );
        
    }
    
}
