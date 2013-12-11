package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireEnableMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.LivewireSelectMessage;

/**
 *
 * @author spl
 */
public abstract class AbstractDrawMessageFactory
    extends AbstractInteractionMessageFactory
    implements MessageListener

{

    private final Message _dsm;	// Draw setup.
    private final Message _dm;	// Draw move.
    private final Message _dcm;	// Draw completion.

    private static class NoOpMessage extends Message {}
    private final NoOpMessage _no_op = new NoOpMessage();
    private final LivewireSelectMessage _lwsm = new LivewireSelectMessage();

    public AbstractDrawMessageFactory( Message dsm, Message dm, Message dcm )

    {

	this._dsm = dsm;
	this._dm = dm;
	this._dcm = dcm;

	MessageManager.registerListener( LivewireEnableMessage.class, this );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof LivewireEnableMessage )
	    this.setMode( ( Boolean ) o );

    }

    private boolean _livewire = false;
    private boolean _first_time = false;

    private void setMode( Boolean livewire )

    {

	this._livewire = livewire.booleanValue();
	this._first_time = true;

    }

    @Override
    public final Message getMouseDownMessage()

    {

        return this._livewire ? this._no_op : this._dsm;

    }

    @Override
    public final Message getMouseUpMessage()

    {

	if ( this._livewire && this._first_time )
	    new LivewireActivationMessage().send();
	this._first_time = false;
        return this._livewire ? this._lwsm : this._dcm;

    }

    @Override
    public final Message getMouseMoveMessage()

    {

        return this._livewire ? this._no_op : this._dm;

    }

}
