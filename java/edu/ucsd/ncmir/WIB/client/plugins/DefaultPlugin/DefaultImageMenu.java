package edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin;

import edu.ucsd.ncmir.WIB.client.core.components.menu.AbstractRadioMenuItem;
import edu.ucsd.ncmir.WIB.client.core.components.menu.StateChangeMenuItem;
import edu.ucsd.ncmir.WIB.client.core.components.menu.StateChangeMessageCommand;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;
import edu.ucsd.ncmir.WIB.client.core.menus.AbstractImageMenu;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.AutoContrastMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ResetMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleBlueChannelMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleContrastMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleGreenChannelMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleRedChannelMessage;

/**
 * The Image menu.
 * @author spl
 */
public class DefaultImageMenu
    extends AbstractImageMenu
{

    /**
     * Creates the Image menu.
     */
    public DefaultImageMenu()

    {

        super();

        this.addItem( new FlipContrastMenuItem( false ) );
        this.addSubmenu( "Color Channels", new ColorChannelsMenu() );
        // this.addCommandItem( "Auto Contrast", new AutoContrastMenu() );

    }

    private static class FlipContrastMenuItem
	extends StateChangeMenuItem
	implements MessageListener

    {

        public FlipContrastMenuItem( boolean state )

        {

	    super( "Contrast Flipped", new ToggleContrastMessage( state ) );
	    MessageManager.registerListener( ResetMessage.class, this );

        }

        @Override
        public void action( Message message, Object object )

        {

	    super.setState( false );

        }

    }

    private class ColorStateChangeMenuItem
	extends StateChangeMenuItem
	implements MessageListener

    {

	ColorStateChangeMenuItem( String label, StateChangeMessageCommand scmc )

	{

	    super( label, scmc );
	    MessageManager.registerListener( ResetMessage.class, this );

	}

        @Override
        public void action( Message message, Object object )

        {

	    super.setState( true );

        }

    }

    private class ColorChannelsMenu
	extends WIBMenuBar
    {

	public ColorChannelsMenu()

	{

	    super( true );

	    this.addItem( new RedChannelMenuItem( true ) );
	    this.addItem( new GreenChannelMenuItem( true ) );
	    this.addItem( new BlueChannelMenuItem( true ) );

	}

	private class RedChannelMenuItem
	    extends ColorStateChangeMenuItem

	{

	    public RedChannelMenuItem( boolean state )
	    {

		super( "Red", new ToggleRedChannelMessage( state ) );

	    }

	}

	private class GreenChannelMenuItem
	    extends ColorStateChangeMenuItem

	{

	    public GreenChannelMenuItem( boolean state )
	    {

		super( "Green", new ToggleGreenChannelMessage( state ) );

            }

	}

	private class BlueChannelMenuItem
	    extends ColorStateChangeMenuItem

	{

	    public BlueChannelMenuItem( boolean state )
	    {

		super( "Blue", new ToggleBlueChannelMessage( state ) );

	    }

	}

    }

    private class AutoContrastMenu
	extends WIBMenuBar

    {

        public AutoContrastMenu()

        {

	    super( true );

            for ( int i = 0; i < 4; i++ )
                this.addItem( new AutoContrastMenuItem( i ) );

        }

    }

    private static class AutoContrastMenuItem
        extends AbstractRadioMenuItem
	implements MessageListener

    {

	private static final String[] LABEL = {
	    "Off",
	    "Max",
	    "Med",
	    "Min"
	};

        public AutoContrastMenuItem( int value )

        {

	    super( AutoContrastMenuItem.LABEL[value],
                   new AutoContrastMessage( value ) );
	    if ( value == 0 )  {
                
		MessageManager.registerListener( ResetMessage.class, this );
                this.setSelected( true );
                
            }

            MessageManager.registerListener( SelectMessage.class, this );

        }

        private class SelectMessage extends Message {}

        @Override
        public void action( Message message, Object object )

        {

            if ( message instanceof ResetMessage )
                super.execute();
            else if ( message instanceof SelectMessage )
                this.setSelected( true );

        }

	private static AbstractRadioMenuItem _selected;

	@Override
	protected AbstractRadioMenuItem getSelectedItem()

	{

	    return AutoContrastMenuItem._selected;

	}

	@Override
	protected void setSelectedItem( AbstractRadioMenuItem selected )

	{

	    AutoContrastMenuItem._selected = selected;

	}

    }

}