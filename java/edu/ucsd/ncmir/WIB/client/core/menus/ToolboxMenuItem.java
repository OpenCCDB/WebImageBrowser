package edu.ucsd.ncmir.WIB.client.core.menus;

import edu.ucsd.ncmir.WIB.client.core.messages.ToggleToolboxStateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToolboxStateMessage;
import edu.ucsd.ncmir.WIB.client.core.toolbox.AbstractToolBox;

/**
 *
 * @author spl
 */
public class ToolboxMenuItem
    extends ToggleDialogMenuItem

{

    public ToolboxMenuItem( AbstractToolBox toolbox )

    {

        super( "Toolbox", toolbox,
	       ToolboxStateMessage.class,
	       ToggleToolboxStateMessage.class );

    }

}