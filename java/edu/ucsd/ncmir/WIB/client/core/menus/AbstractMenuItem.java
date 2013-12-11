package edu.ucsd.ncmir.WIB.client.core.menus;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.MenuItem;
import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;

/**
 *
 * @author spl
 */
public abstract class AbstractMenuItem
    extends MenuItem
    implements Scheduler.ScheduledCommand

{

    public AbstractMenuItem( String label )

    {

        super( WIBMenuBar.spaces( label ),
	       ( Scheduler.ScheduledCommand ) null );
        super.setScheduledCommand( this );

    }
    
    @Override
    public void setText( String text )
        
    {
        
        super.setHTML( WIBMenuBar.spaces( text ) );
        
    }

}