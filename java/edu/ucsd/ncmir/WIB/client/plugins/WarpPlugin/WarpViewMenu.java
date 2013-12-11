package edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin;

import com.google.gwt.core.client.Scheduler;
import edu.ucsd.ncmir.WIB.client.core.components.menu.AbstractRadioMenuItem;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.ImageNameSelectionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.ImageSelectionMessage;
import java.util.HashMap;

/**
 *
 * @author spl
 */
class WarpViewMenu
    extends WIBMenuBar
    implements MessageListener

{

    private final HashMap<String,ImageRadioMenuItem> _map =
	new HashMap<String,ImageRadioMenuItem>();

    public WarpViewMenu()

    {

	super( true );

	ImageRadioMenuItem template = new ImageRadioMenuItem( "template" );
	this.addItem( template );
	template.setSelected( true );

	ImageRadioMenuItem i1 = new ImageRadioMenuItem( "i1" );
	this.addItem( i1 );

	ImageRadioMenuItem i2 = new ImageRadioMenuItem( "i2" );
	this.addItem( i2 );

	this._map.put( "template", template );
	this._map.put( "i1", i1 );
	this._map.put( "i2", i2 );
	MessageManager.registerListener( ImageNameSelectionMessage.class,
					 this );

    }

    @Override
    public void action( Message m, Object o )

    {

	this._map.get( ( String ) o ).setSelected( true );

    }

    private static class ImageRadioMenuItem
        extends AbstractRadioMenuItem
    {

        private static class NoOpMessage extends Message {}

        public ImageRadioMenuItem( String view )

        {

	    super( view, new SendMessageFactory( view ) );

        }

        private static class SendMessageFactory
            implements Scheduler.ScheduledCommand
        {

            private final String _view;

            SendMessageFactory( String image )
            {

                this._view = image;

            }

            @Override
            public void execute()
            {

                new ImageSelectionMessage().send( this._view );

            }

        }
        private static AbstractRadioMenuItem _selected;

        @Override
        protected AbstractRadioMenuItem getSelectedItem()
        {

            return ImageRadioMenuItem._selected;

        }

        @Override
        protected void setSelectedItem( AbstractRadioMenuItem selected )
        {

            ImageRadioMenuItem._selected = selected;

        }

    }

}
