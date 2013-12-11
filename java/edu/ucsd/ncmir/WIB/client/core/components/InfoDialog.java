package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

/**
 *
 * @author spl
 */
public class InfoDialog
    extends AbstractInputDialogBox

{

    /**
     * Creates an empty InfoDialog box.
     */
    public InfoDialog()

    {

        this( true, false );

    }

    public InfoDialog( boolean autohide )

    {

	this( autohide, false );

    }

    public InfoDialog( boolean autohide, boolean modal )

    {

	super( autohide, modal );

    }

    /**
     *
     * @param label
     * @param value
     */
    public void addInfoPair( String label, int value )

    {

	this.addInfoPair( label, Integer.toString( value ) );

    }

    /**
     *
     * @param label
     * @param value
     */
    public void addInfoPair( String label, String value )

    {

	this.addWidgets( new Label( label ), new Label( value ) );

    }

    private ArrayList<Widget[]> _pairs = new ArrayList<Widget[]>();

    public void addWidgets( Widget w1, Widget w2 )

    {

	this._pairs.add( new Widget[] { w1, w2 } );

    }

    /**
     * Shows the <code>InfoDialog</code> on the <code>panel</code>.
     *
     */
    public void displayMessage()

    {

	this.displayMessage( 0, 0 );

    }

    public void displayMessage( int x, int y )

    {

        this.setupGrid();

	super.display( x, y );

    }

    private void setupGrid()

    {

	CenteredGrid grid = new CenteredGrid( this._pairs.size(), 2 );

	for ( int i = 0; i < this._pairs.size(); i++ ) {

	    Widget[] pair = this._pairs.get( i );

	    grid.setWidget( i, 0, pair[0] );
	    grid.setWidget( i, 1, pair[1] );

	}
	this.setWidget( grid );

    }
    
    public void displayCenteredMessage()

    {

        this.setupGrid();

        super.display();

    }

    public void addLabeledWidget( String label, Widget w )

    {

        this.addWidgets( new Label( label ), w );

    }

}
