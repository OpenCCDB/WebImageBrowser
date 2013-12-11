package edu.ucsd.ncmir.WIB.client.core.menus;

import com.google.gwt.core.client.Scheduler;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.components.menu.AbstractRadioMenuItem;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.SetInteractionFactoryMessage;

/**
 *
 * @author spl
 */
public class AbstractActionRadioMenuItem
    extends AbstractRadioMenuItem

{

    protected AbstractActionRadioMenuItem( String label,
                                           AbstractInteractionMessageFactory imf )

    {

        super( label, new SendMessageFactory( imf ) );

	MessageManager.registerListener( imf.activateMessage().getClass(), 
					 new IMFMessageListener( this ) );
					     

    }

    private static class IMFMessageListener
	implements MessageListener

    {

	private final AbstractActionRadioMenuItem _aarmi;

	IMFMessageListener( AbstractActionRadioMenuItem  aarmi )

	{

	    this._aarmi = aarmi;

	}

        @Override
        public void action( Message m, Object o )

        {

	    this._aarmi.setSelected( true );

        }

    }

    private static AbstractRadioMenuItem _selected;

    @Override
    protected AbstractRadioMenuItem getSelectedItem()

    {

        return AbstractActionRadioMenuItem._selected;

    }

    @Override
    protected void setSelectedItem( AbstractRadioMenuItem selected )

    {

        AbstractActionRadioMenuItem._selected = selected;

    }

    private static class SendMessageFactory
	implements Scheduler.ScheduledCommand

    {

        private final AbstractInteractionMessageFactory _imf;

        SendMessageFactory( AbstractInteractionMessageFactory imf )

        {

            this._imf = imf;

        }

        @Override
        public void execute()

        {

            new SetInteractionFactoryMessage().send( this._imf );

        }

    }

}
