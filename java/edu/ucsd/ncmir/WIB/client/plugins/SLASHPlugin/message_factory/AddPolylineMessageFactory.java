package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolylineActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolylineCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolylineDrawMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolylineSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ChooseObjectMessage;

/**
 *
 * @author spl
 */
public class AddPolylineMessageFactory
    extends AbstractDrawMessageFactory

{

    public AddPolylineMessageFactory()

    {

	super( new AddPolylineSetupMessage(),
	       new AddPolylineDrawMessage(),
	       new AddPolylineCompleteMessage() );

    }

    private final ChooseObjectMessage _choose_object_message =
	new ChooseObjectMessage();

    @Override
    public Message getControlAndMouseDownMessage()

    {

        return this._choose_object_message;

    }

    @Override
    public String toString()

    {

        return "Add Trace to Object";

    }

    @Override
    public AbstractActivationMessage activateMessage()

    {

        return new AddPolylineActivationMessage();

    }

}
