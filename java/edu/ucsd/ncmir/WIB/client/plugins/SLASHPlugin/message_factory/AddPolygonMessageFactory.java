package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolygonActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolygonCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolygonDrawMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddPolygonSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ChooseObjectMessage;

/**
 *
 * @author spl
 */
public class AddPolygonMessageFactory
    extends AbstractDrawMessageFactory

{

    public AddPolygonMessageFactory()

    {

	super( new AddPolygonSetupMessage(),
	       new AddPolygonDrawMessage(),
	       new AddPolygonCompleteMessage() );

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

        return new AddPolygonActivationMessage();

    }

}
