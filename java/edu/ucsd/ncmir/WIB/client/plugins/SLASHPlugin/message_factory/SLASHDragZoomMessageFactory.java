package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.core.DragZoomMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ChooseObjectMessage;

/**
 *
 * @author spl
 */
public class SLASHDragZoomMessageFactory
    extends DragZoomMessageFactory

{

    private final ChooseObjectMessage _choose_object_message =
	new ChooseObjectMessage();

    @Override
    public Message getControlAndMouseDownMessage()

    {

        return this._choose_object_message;

    }

}
