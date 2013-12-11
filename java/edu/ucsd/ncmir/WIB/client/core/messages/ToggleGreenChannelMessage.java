package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.components.menu.StateChangeMessageCommand;

/**
 * Sends a message requesting a toggling of the Green channel.
 * @author spl
 */
public class ToggleGreenChannelMessage
    extends StateChangeMessageCommand

{

    /**
     * Creates a Toggle Green Channel message.
     * @param state The initial state.
     */
    public ToggleGreenChannelMessage( boolean state )
        
    {
        
        super( state );
        
    }

}
