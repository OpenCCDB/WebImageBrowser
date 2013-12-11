package edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.MenuItem;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.menus.AbstractActionsMenu;
import edu.ucsd.ncmir.WIB.client.core.menus.ActionRadioMenuItem;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.AbstractActivationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.CompleteAddCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.CompleteAddMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.CompleteEditMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.CompleteSelectCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DeleteMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DragAddMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DragEditMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DrawAddCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.DrawSelectCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.EnableAddMarkOptionMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.EnableCandidateOptionsMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.EnableMarkDependentMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.GenerateWarpMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.SelectEditMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.StartAddCandidateMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.StartAddMarkMessage;
import edu.ucsd.ncmir.WIB.client.plugins.WarpPlugin.messages.StartSelectCandidateMessage;

/**
 *
 * @author spl
 */
class WarpActionsMenu
    extends AbstractActionsMenu
    implements MessageListener

{

    private final MenuItem _warp_command;

    WarpActionsMenu()

    {

	this.addItem( new SelectCandidateRadioMenuItem() );
	this.addItem( new AddCandidateRadioMenuItem() );
	this.addItem( new AddMarkRadioMenuItem() );
	this.addItem( new DeleteMarkRadioMenuItem() );
	this.addItem( new EditMarkRadioMenuItem() );
        this.addSeparator();
	this._warp_command =
	    this.addCommandItem( "Generate Warp", new GenerateWarpCommand() );

	MessageManager.registerListener( EnableMarkDependentMessage.class,
					 this );

    }

    @Override
    public void action( Message m, Object o )

    {

	this._warp_command.setEnabled( ( Boolean ) o );

    }

    private static class NoOpMessage extends AbstractActivationMessage {}

    private static class SelectCandidateRadioMenuItem
        extends ActionRadioMenuItem
	implements MessageListener

    {

        SelectCandidateRadioMenuItem()

        {

            super( "Select Candidate", new SelectCandidateMessageFactory() );
	    MessageManager.registerListener( EnableCandidateOptionsMessage.class,
					     this );

        }

        @Override
        public void action( Message m, Object o )

        {

	    this.setEnabled( ( Boolean ) o );

        }

        private static class SelectCandidateMessageFactory
            extends AbstractInteractionMessageFactory

        {

            @Override
            public Message getMouseDownMessage()

            {

		return new StartSelectCandidateMessage();

            }

            @Override
            public Message getMouseUpMessage()

            {

		return new CompleteSelectCandidateMessage();

            }

            @Override
            public Message getMouseMoveMessage()

            {

		return new DrawSelectCandidateMessage();

            }

            @Override
            public AbstractActivationMessage activateMessage()

            {

                return new NoOpMessage();

            }

        }

    }

    private static class AddCandidateRadioMenuItem
        extends ActionRadioMenuItem
	implements MessageListener

    {

        AddCandidateRadioMenuItem()

        {

            super( "Add Candidate", new AddCandidateMessageFactory() );
	    MessageManager.registerListener( EnableCandidateOptionsMessage.class,
					     this );

        }

        @Override
        public void action( Message m, Object o )

        {

	    this.setEnabled( ( Boolean ) o );

        }

        private static class AddCandidateMessageFactory
            extends AbstractInteractionMessageFactory

        {

            @Override
            public Message getMouseDownMessage()

            {

		return new StartAddCandidateMessage();

            }

            @Override
            public Message getMouseUpMessage()

            {

		return new CompleteAddCandidateMessage();

            }

            @Override
            public Message getMouseMoveMessage()

            {

		return new DrawAddCandidateMessage();

            }

            @Override
            public AbstractActivationMessage activateMessage()

            {

                return new NoOpMessage();

            }

        }

    }

    private static class DeleteMarkRadioMenuItem
        extends ActionRadioMenuItem
	implements MessageListener

    {

        DeleteMarkRadioMenuItem()

        {

            super( "Delete Mark", new DeleteMarkMessageFactory() );
	    MessageManager.registerListener( EnableAddMarkOptionMessage.class,
					     this );

        }

        @Override
        public void action( Message m, Object o )

        {

	    this.setEnabled( ( Boolean ) o );

        }

        private static class DeleteMarkMessageFactory
            extends AbstractInteractionMessageFactory

        {

            @Override
            public Message getMouseDownMessage()

            {

		return new DeleteMarkMessage();

            }

            @Override
            public Message getMouseUpMessage()

            {

		return null;

            }

            @Override
            public Message getMouseMoveMessage()

            {

		return null;

            }

            @Override
            public AbstractActivationMessage activateMessage()

            {

                return new NoOpMessage();

            }

        }

    }

    private static class AddMarkRadioMenuItem
        extends ActionRadioMenuItem
	implements MessageListener

    {

        AddMarkRadioMenuItem()

        {

            super( "Add Mark", new AddMarkMessageFactory() );
	    MessageManager.registerListener( EnableAddMarkOptionMessage.class,
					     this );

        }

        @Override
        public void action( Message m, Object o )

        {

	    this.setEnabled( ( Boolean ) o );

        }

        private static class AddMarkMessageFactory
            extends AbstractInteractionMessageFactory

        {

            @Override
            public Message getMouseDownMessage()

            {

		return new StartAddMarkMessage();

            }

            @Override
            public Message getMouseUpMessage()

            {

		return new CompleteAddMarkMessage();

            }

            @Override
            public Message getMouseMoveMessage()

            {

		return new DragAddMarkMessage();

            }

            @Override
            public AbstractActivationMessage activateMessage()

            {

                return new NoOpMessage();

            }

        }

    }

    private static class EditMarkRadioMenuItem
        extends ActionRadioMenuItem
	implements MessageListener

    {

        EditMarkRadioMenuItem()

        {

            super( "Edit Mark", new EditMarkMessageFactory() );
	    MessageManager.registerListener( EnableMarkDependentMessage.class,
					     this );

        }

        @Override
        public void action( Message m, Object o )

        {

	    this.setEnabled( ( Boolean ) o );

        }

        private static class EditMarkMessageFactory
            extends AbstractInteractionMessageFactory

        {

            @Override
            public Message getMouseDownMessage()

            {

		return new SelectEditMarkMessage();

            }

            @Override
            public Message getMouseUpMessage()

            {

		return new CompleteEditMarkMessage();

            }

            @Override
            public Message getMouseMoveMessage()

            {

		return new DragEditMarkMessage();

            }

            @Override
            public AbstractActivationMessage activateMessage()

            {

                return new NoOpMessage();

            }

        }

    }

    private static class GenerateWarpCommand
	implements Scheduler.ScheduledCommand

    {

	@Override
        public void execute()

        {

	    new GenerateWarpMessage().send();

        }

    }

}

