package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 *
 * @author spl
 */
public class FilteredInputBox
    extends TextBox
    implements KeyDownHandler,
               Filter

{

    private final FilteredDataCallback _filtered_data_callback;
    private Filter _filter = this;

    public FilteredInputBox( FilteredDataCallback filtered_data_callback )

    {

	this.setStylePrimaryName( "FilteredInputBox" );
        super.addKeyDownHandler( this );
        this._filtered_data_callback = filtered_data_callback;

    }

    /**
     * Sets the character validity filter.
     * @param filter
     */
    protected void setFilter( Filter filter )

    {

        this._filter = filter;

    }

    @Override
    public void onKeyDown( KeyDownEvent event )

    {

        int ch = event.getNativeKeyCode();

	if ( ( ch == '\n' ) || ( ch == '\r' ) )
	    this.validateText();
        else if ( ( ch != '\b' ) && !this._filter.validate( ch ) )
	    this.cancelKey();

    }

    private boolean _in_here = false;

    @Override
    public void setText( String text )

    {

	super.setText( text );
	if ( !this._in_here ) {

	    this._in_here = true;
	    this.validateText();
	    this._in_here = false;

	}

    }

    @Override
    public boolean validate( int ch )
    {

        return true;

    }

    private void validateText()

    {

	String s = super.getText();

	boolean ok =
	    ( s.length() > 0 ) &&
	    this._filtered_data_callback.setValue( Double.parseDouble( s ) );

	if ( !ok )
	    super.setText( this._filtered_data_callback.getValue() + "" );

    }

}