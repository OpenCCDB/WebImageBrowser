package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractDialogBox;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelButtons;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelHandler;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteContourMessage;

public class DeleteDialog
    extends AbstractDialogBox
    implements AcceptCancelHandler

{

    public enum Mode {
	TRACE,
	OBJECT
    }

    private final DeleteDialog.Mode _mode;
    private final int _id;

    public DeleteDialog( int points, DeleteDialog.Mode mode )

    {

	super( false, true );

	this._id = points;
	this._mode = mode;

	super.addTitle( "Delete" );

	Grid g0 = new Grid( 3, 1 );

	g0.setWidget( 0, 0, new Label( "Are you sure?" ) );
	g0.setWidget( 1, 0, new Label( "This action CANNOT be undone." ) );
	g0.setWidget( 2, 0, new AcceptCancelButtons( this ) );
	this.setWidget( g0 );

    }

    @Override
    public boolean onAcceptCancelAction( boolean accepted )

    {

	if ( accepted ) {

	    if ( this._mode == DeleteDialog.Mode.TRACE )
		new DeleteContourMessage().send( this._id );
	    else if ( this._mode == DeleteDialog.Mode.OBJECT )
		new DeleteAnnotationMessage().send( this._id );

	}

        return true;

    }

}

