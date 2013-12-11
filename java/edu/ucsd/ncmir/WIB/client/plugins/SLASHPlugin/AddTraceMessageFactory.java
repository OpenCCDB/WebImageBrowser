package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractActionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddTraceCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddTraceDrawMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddTraceSelectCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddTraceSelectMotionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddTraceSelectSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddTraceSetupMessage;

/**
 *
 * @author spl
 */
class AddTraceMessageFactory
    extends AbstractActionMessageFactory

{

    private final AddTraceSetupMessage _dsm = new AddTraceSetupMessage();
    private final AddTraceCompleteMessage _dcm = new AddTraceCompleteMessage();
    private final AddTraceDrawMessage _dm = new AddTraceDrawMessage();

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

    private final AddTraceSelectSetupMessage _ssm =
	new AddTraceSelectSetupMessage();
    private final AddTraceSelectCompleteMessage _scm =
	new AddTraceSelectCompleteMessage();
    private final AddTraceSelectMotionMessage _sm =
	new AddTraceSelectMotionMessage();

    @Override
    public Message getShiftAndMouseDownMessage()

    {

        return this._ssm;

    }

    @Override
    public Message getShiftAndMouseUpMessage()

    {

        return this._scm;

    }

    @Override
    public Message getShiftAndMouseMOveMessage()

    {

        return this._sm;

    }

    @Override
    public String toString()

    {

        return "Add Trace to Object";

    }

}
