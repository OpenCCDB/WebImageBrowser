package edu.ucsd.ncmir.WIB.client.core.menus;

import edu.ucsd.ncmir.WIB.client.core.DragZoomMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;

/**
 *
 * @author spl
 */
public abstract class AbstractActionsMenu
    extends WIBMenuBar
{

    public AbstractActionsMenu()

    {

        super( true );

	this.addItem( new DragCommand() );

    }

    private static class DragCommand
        extends AbstractActionRadioMenuItem

    {

        public DragCommand()

        {

            super( "Move", new DragZoomMessageFactory() );

        }

    }

}
