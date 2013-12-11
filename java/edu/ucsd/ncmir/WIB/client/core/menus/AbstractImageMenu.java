package edu.ucsd.ncmir.WIB.client.core.menus;

import com.google.gwt.core.client.Scheduler;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;
import edu.ucsd.ncmir.WIB.client.core.messages.ResetMessage;

/**
 * The Image menu.
 * @author spl
 */
public class AbstractImageMenu
    extends WIBMenuBar
{

    /**
     * Creates the Image menu.
     */
    protected AbstractImageMenu()

    {

        super( true );

        this.addCommandItem( "Reset", new ResetCommand() );

    }

    private static class ResetCommand
	implements Scheduler.ScheduledCommand

    {

        @Override
        public void execute()

        {

            new ResetMessage().send();

        }

    }

}
