package edu.ucsd.ncmir.WIB.client.core.components;

import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.DragCompleteMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DragMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DragSetupMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.KeyPressMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ZoomInMessage;

/**
 * A factory which creates messages for the mouse getMouseDownMessage, mouse getMouseUpMessage,
 * mouse getMouseMoveMessage, double click mouse actions and the key getMouseUpMessage keyboard
 * action.
 * @author spl
 */

abstract public class AbstractActionMessageFactory

{

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseDownMessage action occurs.
     * @return a <code>Message</code> object to be sent.
     */
    abstract public Message getMouseDownMessage();

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseUpMessage action occurs.
     * @return a <code>Message</code> object to be sent.
     */
    abstract public Message getMouseUpMessage();

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseMoveMessage action occurs.  This <code>Message</code>
     * will only be sent if the mouse button is getMouseDownMessage.
     * @return a <code>Message</code> object to be sent
     */
    abstract public Message getMouseMoveMessage();

    private final DragSetupMessage _shift_down = new DragSetupMessage();
    private final DragCompleteMessage _shift_up = new DragCompleteMessage();
    private final DragMessage _shift_move = new DragMessage();

    private final KeyPressMessage _key_press = new KeyPressMessage();
    private final ZoomInMessage _double_click = new ZoomInMessage();

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseDownMessage action occurs while <code>Shift</code> key
     * is pressed.
     * @return a <code>Message</code> object to be sent.
     */
    public Message getShiftAndMouseDownMessage()

    {

	return this._shift_down;

    }

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseUpMessage action occurs while <code>Shift</code> key is
     * pressed.
     * @return a <code>Message</code> object to be sent.
     */
    public Message getShiftAndMouseUpMessage()

    {

	return this._shift_up;

    }

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseMoveMessage action occurs while <code>Shift</code> key
     * is pressed.  This <code>Message</code> will only be sent if the
     * mouse button is getMouseDownMessage.
     * @return a <code>Message</code> object to be sent
     */
    public Message getShiftAndMouseMOveMessage()

    {

	return this._shift_move;

    }

    // A message for which there are no listeners.

    private class NoOpMessage extends Message {}

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseDownMessage action occurs while <code>Control</code>
     * key is pressed.
     * @return a <code>Message</code> object to be sent. Unless
     * overridden, this message is a NoOp.
     */
    public Message getControlAndMouseDownMessage()

    {

	return new NoOpMessage();

    }

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseUpMessage action occurs while <code>Control</code> key
     * is pressed.
     * @return a <code>Message</code> object to be sent. Unless
     * overridden, this message is a NoOp.
     */
    public Message getControlAndMouseUpMessage()

    {

	return new NoOpMessage();

    }

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseMoveMessage action occurs while <code>Control</code>
     * key is pressed.  This <code>Message</code> will only be sent if
     * the mouse button is getMouseDownMessage.
     * @return a <code>Message</code> object to be sent. Unless
     * overridden, this message is a NoOp.
     */
    public Message getControlAndMouseMoveMessage()

    {

	return new NoOpMessage();

    }

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseDownMessage action occurs while the
     * <code>Control</code> and <code>Shift</code> keys are pressed.
     * @return a <code>Message</code> object to be sent. Unless
     * overridden, this message is a NoOp.
     */
    public Message getControlAndShiftMouseDownMessage()

    {

	return new NoOpMessage();

    }

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseUpMessage action occurs while the <code>Control</code>
     * and <code>Shift</code> keys are pressed.
     * @return a <code>Message</code> object to be sent. Unless
     * overridden, this message is a NoOp.
     */

    public Message getControlAndShiftMouseUpMessage()

    {

	return new NoOpMessage();

    }

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * getMouseMoveMessage action occurs while the
     * <code>Control</code> and <code>Shift</code> keys are pressed.
     * This <code>Message</code> will only be sent if the mouse button
     * is getMouseDownMessage.
     * @return a <code>Message</code> object to be sent. Unless
     * overridden, this message is a NoOp.
     */

    public Message getControlAndShiftMouseMoveMessage()

    {

	return new NoOpMessage();

    }

    /**
     * Generates a <code>Message</code> to be sent when a key is
     * pressed.  May be overridden if the subclass
     * wishes.
     * @return a <code>Message</code> object to be sent.
     */

    public Message getKeyPressMessage()

    {

	return this._key_press;

    }

    /**
     * Generates a <code>Message</code> to be sent when the mouse
     * &quot;double click&quot; action occurs.  May be overridden if
     * the subclass wishes.
     * @return a <code>Message</code> object to be sent.
     */

    public Message getDoubleClickMessage()

    {

	return this._double_click;

    }

}
