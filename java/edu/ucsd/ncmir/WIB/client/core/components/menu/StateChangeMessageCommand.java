package edu.ucsd.ncmir.WIB.client.core.components.menu;

import com.google.gwt.core.client.Scheduler;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class StateChangeMessageCommand
    extends Message
    implements SelectorInterface,
               Scheduler.ScheduledCommand

{

    private boolean _state;

    /**
     * <code>StateChangeMessageCommand</code> constructor
     * @param state Initial state.
     */
    public StateChangeMessageCommand( boolean state )

    {

        this._state = state;

    }

    private StateChangeMenuItem _scmi = null;

    /**
     * Internal method.  Should not be called by user.
     * @param scmi The <code>StateChangeMenuItem</code> to be set.
     */
    public void setStateChangeMenuItem( StateChangeMenuItem scmi )

    {

        this._scmi = scmi;

    }

    /**
     * Sets the current selection state.
     *
     * @param state The state.
     */
    @Override
    public void setSelected( boolean state )
    {

        this._state = state;
        this.send( state );
        if ( this._scmi != null )
            this._scmi.setState( state );

    }

    /**
     * Executed by the command.
     */
    @Override
    public void execute()
    {

        this.setSelected( !this._state );

    }

    /**
     * Returns the current selection state.
     * @return The state.
     */
    public boolean selected()

    {

        return this._state;

    }

}