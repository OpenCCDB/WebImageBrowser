package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.components.menu.StateChangeMessageCommand;

/**
 *
 * @author spl
 */
public class ToggleContrastMessage
    extends StateChangeMessageCommand
{

    /**
     * Creates a Toggle Contrast message.
     * @param state
     */
    public ToggleContrastMessage( boolean state )
    {
        
        super( state );
        
    }
    
}
