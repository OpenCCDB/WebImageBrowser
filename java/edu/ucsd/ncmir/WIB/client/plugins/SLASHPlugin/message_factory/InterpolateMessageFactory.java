package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.InterpolateActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectInterpolationContourMessage;

/**
 *
 * @author spl
 */
public class InterpolateMessageFactory
    extends AbstractInteractionMessageFactory

{

    private static class NoOpMessage extends Message {}
    private final NoOpMessage _no_op = new NoOpMessage();

    @Override
    public Message getMouseDownMessage()

    {

        return this._no_op;

    }

    @Override
    public Message getMouseUpMessage()

    {

        return this._no_op;

    }

    @Override
    public Message getMouseMoveMessage()

    {

        return this._no_op;

    }

    private final SelectInterpolationContourMessage _sicm =
	new SelectInterpolationContourMessage();

    @Override
    public Message getControlAndMouseDownMessage()

    {

        return this._sicm;

    }

    @Override
    public String toString()

    {

        return "Interpolate contours.";

    }

    @Override
    public AbstractActivationMessage activateMessage()

    {

        return new InterpolateActivationMessage();

    }

}
