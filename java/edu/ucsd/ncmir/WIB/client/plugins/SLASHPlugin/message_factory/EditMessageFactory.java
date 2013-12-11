package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory;

import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.EditActivationMessage;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditMotionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ContourEditSetupMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectContourMessage;

/**
 *
 * @author spl
 */
public class EditMessageFactory
    extends AbstractInteractionMessageFactory

{

    private final ContourEditSetupMessage _contour_edit_setup_message =
	new ContourEditSetupMessage();
    private final ContourEditCompleteMessage _contour_edit_complete_message =
	new ContourEditCompleteMessage();
    private final ContourEditMotionMessage _contour_edit_motion_message =
	new ContourEditMotionMessage();
    private final SelectContourMessage _select_contour_message =
	new SelectContourMessage();

    @Override
    public Message getMouseDownMessage()

    {

        return this._contour_edit_setup_message;

    }

    @Override
    public Message getMouseUpMessage()

    {

        return this._contour_edit_complete_message;

    }

    @Override
    public Message getMouseMoveMessage()

    {

        return this._contour_edit_motion_message;

    }

    @Override
    public Message getControlAndMouseDownMessage()

    {

        return this._select_contour_message;

    }

    @Override
    public String toString()

    {

        return "Edit Object";

    }

    @Override
    public AbstractActivationMessage activateMessage()
    {

        return new EditActivationMessage();

    }

}
