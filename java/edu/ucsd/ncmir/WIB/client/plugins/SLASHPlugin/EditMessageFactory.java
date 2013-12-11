package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractActionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditMotionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditSetupMessage;

/**
 *
 * @author spl
 */
class EditMessageFactory
    extends AbstractActionMessageFactory

{

    private final ContourEditSetupMessage _dsm =
	new ContourEditSetupMessage();
    private final ContourEditCompleteMessage _dcm =
	new ContourEditCompleteMessage();
    private final ContourEditMotionMessage _dm =
	new ContourEditMotionMessage();

    @Override
    public Message getMouseDownMessage()

    {

        return this._dsm;

    }

    @Override
    public Message getMouseUpMessage()

    {

        return this._dcm;

    }

    @Override
    public Message getMouseMoveMessage()

    {

        return this._dm;

    }

    @Override
    public String toString()

    {

        return "Edit Object";

    }

}
