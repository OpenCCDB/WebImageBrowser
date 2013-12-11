package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractDialogBox;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;

/**
 *
 * @author spl
 */
public abstract class AbstractOperationDialog
    extends AbstractDialogBox

{

    public AbstractOperationDialog( Class<? extends Message> activate )

    {

	super( false, false );

	MessageManager.registerAsListener( new ActivationHandler( this,
								  activate ),
					   activate,
					   AbstractActivationMessage.class );

    }

    abstract protected void init();
    abstract protected void cleanup();

    private static class ActivationHandler
	implements MessageListener

    {

	private final AbstractOperationDialog _aod;
	private final Class<? extends Message> _activate;

	ActivationHandler( AbstractOperationDialog aod,
			   Class<? extends Message> activate )

	{

	    this._aod = aod;
	    this._activate = activate;

	}

        @Override
	public void action( Message m, Object o )
	    
	{
	    
	    if ( m.isInstance( this._activate ) ) {

		this._aod.init();
		this._aod.display();

	    } else if ( m instanceof AbstractActivationMessage ) {

		this._aod.hide();
		this._aod.cleanup();

	    }

	}

    }
	
}
