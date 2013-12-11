package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelButtons;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelHandler;
import edu.ucsd.ncmir.WIB.client.core.components.InfoDialog;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SetUserNameMessage;

public class UserNameDialog
    extends InfoDialog
    implements AcceptCancelHandler

{

    private final TextBox _user_name_box = new TextBox();

    public UserNameDialog()

    {

	super( false, true );

	super.addTitle( "User Name" );
	super.addLabeledWidget( "User Name:", this._user_name_box );

	this._user_name_box.setWidth( "200px" );

	this.addWidgets( new AcceptCancelButtons( this ), new Label( "" ) );

    }


    @Override
    public boolean onAcceptCancelAction( boolean accepted )

    {

	boolean ok = true;

	if ( accepted ) {

	    String errors = "";
	    String user_name = this._user_name_box.getText();

	    if ( user_name.contains( "'" ) )
		errors +=
		    "User name \"" + user_name + "\"\n" +
		    "contains an invalid character (').\n\n";
	    if ( user_name.equals( "" ) )
		errors += "A user name must be specified.\n\n";

	    if ( errors.equals( "" ) )
		new SetUserNameMessage().send( user_name );
	    else {

		ok = false;
		Window.alert( "Error:\n\n" + errors );

	    }

	}

        return ok;

    }

}
