package edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.menus.AbstractActionsMenu;
import edu.ucsd.ncmir.WIB.client.core.menus.ActionRadioMenuItem;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin.messages.QueryMessage;

/**
 *
 * @author spl
 */
class INCFActionsMenu
    extends AbstractActionsMenu
{

    public INCFActionsMenu()
    {

        this.addItem( new QueryRadioMenuItem() );

    }

    private static class QueryRadioMenuItem
        extends ActionRadioMenuItem
    {

        QueryRadioMenuItem()
        {

            super( "Query", new QueryMessageFactory() );

        }

	private static class QuerySelectMessage extends AbstractActivationMessage {}

        private static class QueryMessageFactory
            extends AbstractInteractionMessageFactory
        {

            @Override
            public Message getMouseDownMessage()

            {

		return new QueryMessage();

            }

            @Override
            public Message getMouseUpMessage()

            {

		return null;

            }

            @Override
            public Message getMouseMoveMessage()
            {

		return null;

            }

            @Override
            public AbstractActivationMessage activateMessage()

            {

		return new QuerySelectMessage();

            }

        }

    }

}
