package edu.ucsd.ncmir.WIB.client.core.components.menu;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.MenuItem;

/**
 *
 * @author spl
 */
public abstract class AbstractRadioMenuItem
    extends MenuItem
    implements Scheduler.ScheduledCommand

{

    private Scheduler.ScheduledCommand _command;
    private SafeHtml _selected_label;
    private SafeHtml _unselected_label;

    /**
     * Creates a <code>AbstractRadioMenuItem</code> object.
     * @param label The menu item label.  A box with a <code>x</code>
     * or an empty box will be prepended to the label depending upon
     * whether the item is currently selected or not.
     * @param command A command to be executed on selection.
     */
    public AbstractRadioMenuItem( String label,
                                  Scheduler.ScheduledCommand command )

    {

	super( "", true, ( Scheduler.ScheduledCommand ) null );

        this._selected_label =
            SafeHtmlUtils.fromSafeConstant( "&#9746; " + label );
        this._unselected_label =
            SafeHtmlUtils.fromSafeConstant( "&#9744; " + label );

        super.setScheduledCommand( this );
	this._command = command;
	this.deselect();

    }

    /**
     * Part of the selection apparatus.
     * @return The current <code>AbstractRadioMenuItem</code> selected.
     */
    abstract protected AbstractRadioMenuItem getSelectedItem();
    /**
     * Sets the current <code>AbstractRadioMenuItem</code>.
     * @param menu_item The <code>AbstractRadioMenuItem</code> selected.
     */
    abstract protected void setSelectedItem( AbstractRadioMenuItem menu_item );

    public void setSelected( boolean selected )

    {

        if ( selected )
            this.select();
        else
            this.deselect();

    }

    private void select()

    {

	if ( this.getSelectedItem() != null )
	    this.getSelectedItem().deselect();
	this.setHTML( this._selected_label );
        this.setSelectedItem( this );

    }

    private void deselect()

    {

        this.setHTML( this._unselected_label );

    }

    @Override
    public void execute()

    {

        this.select();

        this._command.execute();

    }

}