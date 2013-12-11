package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author spl
 */

public class AbstractDialogBox
    extends ResizableDialogBox

{

    /**
     * Creates an empty AbstractDialogBox box.
     */
    protected AbstractDialogBox()
    {

        this( true, false );

    }

    protected AbstractDialogBox( boolean autohide )
    {

        this( autohide, false );

    }

    protected AbstractDialogBox( boolean autohide, boolean modal )
    {

        this( autohide, modal, false );

    }

    protected AbstractDialogBox( boolean autohide,
				 boolean modal,
				 boolean resizable )

    {

	this( autohide, modal, false, resizable );

    }

    protected AbstractDialogBox( boolean autohide,
				 boolean modal,
				 boolean show_close_icon,
				 boolean resizable )

    {

	this( autohide, modal, false, show_close_icon, resizable );

    }

    protected AbstractDialogBox( boolean autohide,
				 boolean modal,
				 boolean show_minimize_icon,
				 boolean show_close_icon,
				 boolean resizable )

    {

	super( autohide,
	       modal,
	       show_minimize_icon,
	       show_close_icon,
	       resizable );

        super.setGlassEnabled( false );

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

    private boolean _ignore = true;
    private int _x = -1;
    private int _y = -1;

    @Override
    protected void onDetach()

    {

	if ( !this._ignore ) {

	    this._x = this.getAbsoluteLeft();
	    this._y = this.getAbsoluteTop();

	}
	super.onDetach();

    }

    public void display()

    {

        // This just forces the panel to lay itself out.

        RootPanel panel = RootPanel.get();

	this._ignore = true;
        panel.add( this );

        this.show();
        this.setVisible( true );
	this._ignore = false;

	if ( ( this._x != -1 ) && ( this._y != -1 ) )
	    panel.setWidgetPosition( this, this._x, this._y );
	else {

	    int w = this.getOffsetWidth();
	    int h = this.getOffsetHeight();

	    // Because RootPanel gives us a bogus height, we ask
	    // RootLayoutPanel instead.

	    RootLayoutPanel layout_panel = RootLayoutPanel.get();

	    int rpw = layout_panel.getOffsetWidth();
	    int rph = layout_panel.getOffsetHeight();

	    int lr = ( rpw - w ) / 2;
	    int tb = ( rph - h ) / 2;

	    this._x = lr;
	    this._y = tb;

	    panel.setWidgetPosition( this, lr, tb );

	}

    }

}
