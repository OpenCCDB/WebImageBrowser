package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import edu.ucsd.ncmir.WIB.client.WIB;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelButtons;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelHandler;
import edu.ucsd.ncmir.WIB.client.core.components.ColorButton;
import edu.ucsd.ncmir.WIB.client.core.components.ColorPickerBox;
import edu.ucsd.ncmir.WIB.client.core.components.ColorPickerHandler;
import edu.ucsd.ncmir.WIB.client.core.components.InfoDialog;
import edu.ucsd.ncmir.spl.Graphics.Color;

/**
 * A dialog to set preferences.
 * @author spl
 */
public class PreferencesDialog
    extends InfoDialog
    implements AcceptCancelHandler,
               ClickHandler,
               ColorPickerHandler,
               KeyPressHandler

{

    private final ColorButton _color_button = new ColorButton( "Select" );
    private final TextBox _timeout_value = new TextBox();
    private int _red;
    private int _green;
    private int _blue;

    /**
     * Instantiates the dialog.
     */
    public PreferencesDialog()

    {

	super( false, true );

	BodyElement body_element = Document.get().getBody();
     	int[] color =
	    Color.parseRGBToTriplet( body_element.getStyle().getBackgroundColor() );

	this._red = color[0];
	this._green = color[1];
	this._blue = color[2];

	this._color_button.addClickHandler( this );
	this.updateButtonColor();

	this._timeout_value.setText( "" + WIB.getTimeoutSeconds() );
	this._timeout_value.addKeyPressHandler( this );

	super.addTitle( "Preferences" );

	this.addWidgets( new Label( "Background Color" ),
			 this._color_button );
	this.addWidgets( new Label( "Timeout (Sec)" ),
			 this._timeout_value );
	Configuration.getPlugin().pluginPreferences( this );
	this.addWidgets( new AcceptCancelButtons( this ),
			 new Label( "" ) );

    }

    @Override
    public void onKeyPress( KeyPressEvent event )

    {

	if ( !Character.isDigit( event.getCharCode() ) )
	    this._timeout_value.cancelKey();

    }

    @Override
    public boolean onAcceptCancelAction( boolean accepted )

    {

	boolean ok = true;

	if ( accepted ) {

	    String s = this._timeout_value.getValue();

	    int timeout;
	    if ( ( s.length() > 0 ) &&
		 ( ( timeout = Integer.parseInt( s ) ) > 0 ) ) {

		WIB.setTimeoutSeconds( timeout );

		BodyElement body_element = Document.get().getBody();
		Style style = body_element.getStyle();

		String background_color =
		    "rgb(" +
		    this._red + "," +
		    this._green + "," +
		    this._blue +
		    ")";

		style.setBackgroundColor( background_color );
                
                Configuration.getPlugin().acceptPluginPreferences();
                
		if ( Cookies.isCookieEnabled() )
		    Cookies.setCookie( "background_color", background_color );

	    } else {

		Window.alert( "Error:\n\n" +
			      "You must enter a nonzero timeout value." );
		ok = false;

	    }

	}

        return ok;

    }

    private ColorPickerBox _color_picker;

    @Override
    public void onClick( ClickEvent event )

    {

	this._color_picker = new ColorPickerBox( this );

	this._color_picker.setRGB( this._red, this._green, this._blue );
	this._color_picker.display();

    }

    @Override
    public void onColorPicker( String hex )

    {

	int value = Integer.valueOf( hex, 16 );

	this._red = ( value >> 16 ) & 0xff;
	this._green = ( value >> 8 ) & 0xff;
	this._blue = ( value ) & 0xff;

	this.updateButtonColor();

    }

    private void updateButtonColor()

    {

	this._color_button.setColor( this._red, this._green, this._blue );

    }

}
