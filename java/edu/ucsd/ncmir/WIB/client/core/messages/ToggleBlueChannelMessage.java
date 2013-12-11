package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.components.menu.StateChangeMessageCommand;

/**
 * Sends a message requesting a toggling of the Blue channel.
 * @author spl
 */
public class ToggleBlueChannelMessage
    extends StateChangeMessageCommand
{
    
    /**
     * Creates the Blue channel message.
     * @param state The initial state.
     */
    public ToggleBlueChannelMessage( boolean state )
        
    {
        
        super( state );
        
    }
    
}
