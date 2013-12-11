package edu.ucsd.ncmir.WIB.client.plugins.DefaultPlugin;

import edu.ucsd.ncmir.WIB.client.core.BrightnessBar;
import edu.ucsd.ncmir.WIB.client.core.ContrastBar;
import edu.ucsd.ncmir.WIB.client.core.panel.ControlsPanel;
import edu.ucsd.ncmir.WIB.client.core.TimestepBar;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.InitializeTimestepMessage;

/**
 *
 * @author spl
 */
public class DefaultControlsPanel
    extends ControlsPanel

{

    public DefaultControlsPanel()

    {

	MessageManager.registerListener( InitializeTimestepMessage.class,
					 new Listener( this ) );
        this.addControl( new BrightnessBar() );
        this.addControl( new ContrastBar() );

    }

    private class Listener
	implements MessageListener

    {

        private final DefaultControlsPanel _dcp;

	Listener( DefaultControlsPanel dcp )

	{

	    this._dcp = dcp;

	}

	@Override
	public void action( Message m, Object o )

	{

	    int maxts = ( ( Integer ) o ).intValue();

	    this._dcp.addControl( new TimestepBar( maxts ) );

	}

    }

}
