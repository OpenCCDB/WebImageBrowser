package edu.ucsd.ncmir.WIB.client.core.components.menu;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.MenuItem;

/**
 *
 * @author spl
 */
public class StateChangeMenuItem
    extends MenuItem

{

    private String _set_string;
    private String _unset_string;

    /**
     * Creates the menu item.
     * @param label The label for the menu item.
     * @param scmc The <code>Command</code>/<code>Message</code> to be
     * sent when the item is selected.
     */
    public StateChangeMenuItem( String label, StateChangeMessageCommand scmc )

    {

	super( "", true, ( Scheduler.ScheduledCommand ) scmc );

        scmc.setStateChangeMenuItem( this );
	this._set_string = "<strong>&#x2611;</strong> " + label;
	this._unset_string = "<strong>&#x2610;</strong> " + label;
	this.setState( scmc.selected() );

    }

    /**
     * Internal method: sets the selected label.
     */
    private void select()

    {

	super.setHTML( this._set_string );

    }

    /**
     * Internal method: sets the unselected label.
     */
    private void deselect()

    {

	super.setHTML( this._unset_string );

    }

    /**
     * Sets the state for this menu item.
     * @param state
     */
    public final void setState( boolean state )
    {

        if ( state )
            this.select();
        else
            this.deselect();

    }

}