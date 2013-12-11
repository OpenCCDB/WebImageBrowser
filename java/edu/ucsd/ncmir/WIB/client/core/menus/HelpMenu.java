package edu.ucsd.ncmir.WIB.client.core.menus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;

/**
 * The Help menu.
 * @author spl
 */
public class HelpMenu
    extends WIBMenuBar

{

    /**
     * The Help menu.
     */
    public HelpMenu()

    {

	super( true );

	this.addCommandItem( "Manual", new ManualCommand() );
	this.addCommandItem( "About", new AboutCommand() );

    }

    private static class ManualCommand
	implements Scheduler.ScheduledCommand

    {

        @Override
        public void execute()

	{

            Window.open( GWT.getHostPageBaseURL() + "WIBHelp.html",
			 "_blank",
			 null );

        }

    }

    private static class AboutCommand
	implements Scheduler.ScheduledCommand

    {

        @Override
        public void execute()

        {

	    Window.alert( "About Web Image Browser (WIB)\n" +
			  "\n" +
                          "*** PRE-ALPHA Test Version ***\n" +
			  "\n" +
			  "Web Image Browser (WIB) Version 3.0.0\n" +
			  "Copyright 2013 " +
			  "Regents of the University of California\n" +
			  "\n" +
			  "Author:\tSteve Lamont\n" +
			  "\t\tspl@ucsd.edu" );

        }

    }

}
