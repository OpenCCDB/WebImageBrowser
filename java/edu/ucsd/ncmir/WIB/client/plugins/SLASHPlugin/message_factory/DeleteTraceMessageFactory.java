package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteMotionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteTraceActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteTraceCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteTraceSetupMessage;

/**
 *
 * @author spl
 */
public class DeleteTraceMessageFactory
    extends AbstractInteractionMessageFactory

{

    private final DeleteTraceSetupMessage _dsm = new DeleteTraceSetupMessage();
    private final DeleteTraceCompleteMessage _dcm = new DeleteTraceCompleteMessage();
    private final DeleteMotionMessage _dm = new DeleteMotionMessage();

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

        return "Delete Trace";

    }

    @Override
    public AbstractActivationMessage activateMessage()

    {

        return new DeleteTraceActivationMessage();

    }

}
