package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author spl
 */
public class DisplayBox
    extends DialogBox
{

    /**
     * Creates an empty DisplayBox box.
     */
    protected DisplayBox()
    {

        this( true, false );

    }

    protected DisplayBox( boolean autohide )
    {

        this( autohide, false );

    }

    protected DisplayBox( boolean autohide, boolean modal )
    {

        super( autohide, modal );

        this.setGlassEnabled( false );

    }

    /**
     * Sets the title of the box.
     *
     * @param title The title.
     */
    public void addTitle( String title )
    {

        this.setText( title );

    }

    public void display( int x, int y )
    {

        RootPanel panel = RootPanel.get();

        panel.add( this );

        this.hide();
        this.setVisible( false );

        this.show();
        this.setVisible( true );
        panel.setWidgetPosition( this, x, y + 45 );

    }

    public void display()
    {

        // This just forces the panel to lay itself out.

        RootPanel rp = RootPanel.get();

        rp.add( this );

        this.show();
        this.setVisible( true );

        int w = this.getOffsetWidth();
        int h = this.getOffsetHeight();

        // Because RootPanel gives us a bogus height, we ask
        // RootLayoutPanel instead.

        RootLayoutPanel panel = RootLayoutPanel.get();

        int rpw = panel.getOffsetWidth();
        int rph = panel.getOffsetHeight();

        int lr = ( rpw - w ) / 2;
        int tb = ( rph - h ) / 2;

        rp.setWidgetPosition( this, lr, tb );

    }

}
