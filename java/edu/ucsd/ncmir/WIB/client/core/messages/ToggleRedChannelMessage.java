package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.components.menu.StateChangeMessageCommand;

/**
 * Sends a message requesting a toggling of the Red channel.
 * @author spl
 */
public class ToggleRedChannelMessage
    extends StateChangeMessageCommand
{
    
    /**
     * Creates a Toggle Red Channel message.
     * @param state The initial state.
     */
    public ToggleRedChannelMessage( boolean state )
        
    {
        
        super( state );
        
    }
    
}
