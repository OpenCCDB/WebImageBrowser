package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelButtons;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelHandler;
import edu.ucsd.ncmir.WIB.client.core.components.ColorButton;
import edu.ucsd.ncmir.WIB.client.core.components.ColorPickerBox;
import edu.ucsd.ncmir.WIB.client.core.components.ColorPickerHandler;
import edu.ucsd.ncmir.WIB.client.core.components.InfoDialog;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.Annotation;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationMessage;

public class AnnotationDialog
    extends InfoDialog
    implements AcceptCancelHandler,
               ClickHandler,
               ColorPickerHandler

{

    private static AnnotationOracle _oracle = new AnnotationOracle();

    private final SuggestBox _suggest_box =
	new SuggestBox( AnnotationDialog._oracle );
    private final TextBox _user_name_box = new TextBox();
    private final TextArea _description_box = new TextArea();
    private final ColorButton _color_button = new ColorButton( "Color" );

    private final Annotation _points;
    private final int _plane;
    private ColorPickerBox _picker;

    private final AnnotationMessage _message;

    public AnnotationDialog( Annotation points,
                             int plane,
                             AnnotationMessage message )

    {

	super( false, true );

        this._message = message;

	this._points = points;
	this._plane = plane;

	super.addTitle( "Annotation" );
	super.addLabeledWidget( "User Name:", this._user_name_box );
	super.addLabeledWidget( "Object Name:", this._suggest_box );
	super.addLabeledWidget( "Description:", this._description_box );

	this._user_name_box.setText( points.getUserName() );
	this._suggest_box.setText( points.getObjectName() );
	this._description_box.setText( points.getDescription() );

	this._user_name_box.setWidth( "200px" );
	this._suggest_box.setWidth( "200px" );
	this._description_box.setWidth( "200px" );

	this.addWidgets( new AcceptCancelButtons( this ), this._color_button );

	this._color_button.addClickHandler( this );

	this.updateButtonColor();

    }


    @Override
    public boolean onAcceptCancelAction( boolean accepted )

    {

	this._message.setPoints( this._points );
	this._message.setPlane( this._plane );

	boolean ok = true;

	if ( accepted ) {

	    String user_name = this._user_name_box.getText();
	    String object_name = this._suggest_box.getText();

	    String errors = "";

	    if ( user_name.contains( "'" ) )
		errors +=
		    "User name \"" + user_name + "\"\n" +
		    "contains an invalid character (').\n\n";
	    if ( user_name.equals( "" ) )
		errors += "A user name must be specified.\n\n";

	    if ( object_name.contains( "'" ) )
		errors +=
		    "Object name \"" + object_name + "\"\n" +
		    "contains an invalid character (').\n\n";
	    if ( object_name.equals( "" ) )
		errors += "An object name must be specified.\n\n";

	    if ( errors.equals( "" ) ) {

		this._message.setKeep( true );
		this._points.setObjectName( object_name );
		this._points.setUserName( user_name );
                this._points.setDescription( this._description_box.getText() );

	    } else {

		ok = false;
		Window.alert( "Error:\n\n" + errors );

	    }

	} else
	    this._message.setKeep( false );

	if ( ok )
	    this._message.send();

        return ok;

    }

    @Override
    public void onClick( ClickEvent event )

    {

	int x = event.getClientX();
	int y = event.getClientY();

	if ( ( x > 0 ) && ( y > 0 ) ) {

	    this._picker = new ColorPickerBox( this );
	    this._picker.setRGB( this._points.getRed(),
				 this._points.getGreen(),
				 this._points.getBlue() );

	    this._picker.display();

	}

    }

    @Override
    public void onColorPicker( String hex )
    {
	int value = Integer.valueOf( hex, 16 );

	this._points.setRGB( ( value >> 16 ) & 0xff,
			     ( value >> 8 ) & 0xff,
			     ( value ) & 0xff );

	this.updateButtonColor();

    }

    private void updateButtonColor()

    {

	this._color_button.setColor( this._points.getRed(),
				     this._points.getGreen(),
				     this._points.getBlue() );

    }


}
