package edu.ucsd.ncmir.WIB.client.core.messages;

import com.google.gwt.core.client.Scheduler;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class AutoContrastMessage
    extends Message
    implements Scheduler.ScheduledCommand

{

    private int _level;

    /**
     *
     * Creates an <code>AutoContrastMessage</code> object.
     * @param level An integer between 0 and 3 indicating the auto
     * contrast level to be used.
     */

    public AutoContrastMessage( int level )

    {

	this._level = level;

    }

    @Override
    public void execute()
    {

        this.send( this._level );

    }

}
