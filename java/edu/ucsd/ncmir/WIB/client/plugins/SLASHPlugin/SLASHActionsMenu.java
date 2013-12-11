package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs.ThreeDDisplayDialog;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;
import edu.ucsd.ncmir.WIB.client.core.menus.ToolboxMenuItem;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteActionMenuItemMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DisableRedoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DisableUndoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.EnableRedoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.EnableUndoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.InsertActionMenuItemMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.RedoMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.UndoMessage;

/**
 *
 * @author spl
 */
class SLASHActionsMenu
    extends WIBMenuBar
    implements MessageListener

{

    private final MenuItem _undo;
    private final MenuItem _redo;
    private final MenuItemSeparator _separator;

    public SLASHActionsMenu()

    {

        super( true );

        MessageManager.registerAsListener( this,
					   DeleteActionMenuItemMessage.class,
                                           DisableRedoMessage.class,
                                           DisableUndoMessage.class,
                                           EnableRedoMessage.class,
                                           EnableUndoMessage.class,
					   InsertActionMenuItemMessage.class );

        this.addItem( new ToolboxMenuItem( new SLASHToolBox() ) );
        
	try {

	    this.addItem( new View3DMenuItem( new ThreeDDisplayDialog() ) );

	} catch ( Exception e ) {

	    // No WebGL.  Just eat it.

	}

	this._separator = this.addSeparator();
	this._undo = this.addCommandItem( "Undo", new UndoCommand() );
	this._redo = this.addCommandItem( "Redo", new RedoCommand() );

	this._undo.setEnabled( false );
	this._redo.setEnabled( false );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof EnableUndoMessage )
	    this._undo.setEnabled( true );
	else if ( m instanceof DisableUndoMessage )
	    this._undo.setEnabled( false );
	else if ( m instanceof EnableRedoMessage )
	    this._redo.setEnabled( true );
	else if ( m instanceof DisableRedoMessage )
	    this._redo.setEnabled( false );
	else if ( m instanceof InsertActionMenuItemMessage )
	    this.insertMenuItem( ( MenuItem ) o );
	else if ( m instanceof DeleteActionMenuItemMessage )
	    this.deleteMenuItem( ( MenuItem ) o );

    }

    private void insertMenuItem( MenuItem menu_item )

    {

	int seploc = this.getSeparatorIndex( this._separator );

	this.insertItem( menu_item, seploc );

    }


    private void deleteMenuItem( MenuItem menu_item )

    {

	this.removeItem( menu_item );

    }

    private static class UndoCommand
	implements Scheduler.ScheduledCommand

    {

        @Override
        public void execute()
        {

	    new UndoMessage().send();

        }

    }

    private static class RedoCommand
	implements Scheduler.ScheduledCommand

    {

        @Override
        public void execute()
        {

	    new RedoMessage().send();

        }

    }

}
