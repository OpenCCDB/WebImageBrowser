package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractDialogBox;
import edu.ucsd.ncmir.WIB.client.core.menus.ToggleDialogMenuItem;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ToggleView3DStateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.View3DStateMessage;

/**
 *
 * @author spl
 */
public class View3DMenuItem
    extends ToggleDialogMenuItem

{

    public View3DMenuItem( AbstractDialogBox three_d_dialog )

    {

        super( "View3D", three_d_dialog,
	       View3DStateMessage.class,
	       ToggleView3DStateMessage.class );

    }

}