package edu.ucsd.ncmir.WIB.client.core;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DragActivationMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DragCompleteMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DragMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DragSetupMessage;

/**
 *
 * @author spl
 */
public class DragZoomMessageFactory
    extends AbstractInteractionMessageFactory

{

    private final DragSetupMessage _down = new DragSetupMessage();
    private final DragCompleteMessage _up = new DragCompleteMessage();
    private final DragMessage _move = new DragMessage();

    @Override
    public Message getMouseDownMessage()

    {

        return this._down;

    }

    @Override
    public Message getMouseUpMessage()

    {

        return this._up;

    }

    @Override
    public Message getMouseMoveMessage()

    {

        return this._move;

    }

    @Override
    public String toString()

    {

        return "Move Image";

    }

    @Override
    public AbstractActivationMessage activateMessage()

    {

        return new DragActivationMessage();

    }

}
