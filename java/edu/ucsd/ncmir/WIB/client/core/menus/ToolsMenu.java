package edu.ucsd.ncmir.WIB.client.core.menus;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.MenuItem;
import edu.ucsd.ncmir.WIB.client.core.PreferencesDialog;
import edu.ucsd.ncmir.WIB.client.core.components.menu.StateChangeMenuItem;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.CollaborateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ShowInfoMessage;
import edu.ucsd.ncmir.WIB.client.core.toolbox.AbstractToolBox;

/**
 * The Tools menu.
 * @author spl
 */
public class ToolsMenu
    extends WIBMenuBar

{

    /**
     * Creates the Tools menu
     */
    public ToolsMenu()

    {

        super( true );

        this.addCommandItem( "Show Info", new ShowInfoCommand() );
        this.addSeparator();
        this.addItem( new CollaborateMenuItem() );
        this.addOptionItem( new SynchCollaboratorsMenuItem() );

        this.addSeparator();
        this.addCommandItem( "Preferences", new PreferencesCommand() );

    }

    private static class ShowInfoCommand
	implements Scheduler.ScheduledCommand

    {

        @Override
        public void execute()
        {

	    new ShowInfoMessage().send();

        }

    }

    private static class CollaborateMenuItem
	extends StateChangeMenuItem

    {

	CollaborateMenuItem()

	{

	    super( "Collaborate", new CollaborateMessage( false ) );

	}

    }

    private static class SynchCollaboratorsMenuItem
	extends MenuItem
	implements MessageListener

    {

        public SynchCollaboratorsMenuItem()
        {

	    super( "Synch Collaborators",
		   new SynchCollaboratorsCommand() );
	    super.setEnabled( false );
	    MessageManager.registerListener( CollaborateMessage.class, this );

        }

        @Override
        public void action( Message m, Object o )

        {

	    super.setEnabled( ( ( Boolean ) o ).booleanValue() );

        }

    }

    private static class ShowToolboxCommand
        implements Scheduler.ScheduledCommand

    {

	private final AbstractToolBox _toolbox;

        public ShowToolboxCommand( AbstractToolBox toolbox )

        {

	    this._toolbox = toolbox;

        }

        @Override
        public void execute()

        {

	    this._toolbox.display();

        }

    }

    private static class SynchCollaboratorsCommand
	implements Scheduler.ScheduledCommand

    {

        @Override
        public void execute()

        {

            //TODO add the works.

        }

    }

    private static class PreferencesCommand
	implements Scheduler.ScheduledCommand

    {

        @Override
        public void execute()

        {

	    new PreferencesDialog().displayCenteredMessage();

        }

    }

}
