package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractDialogBox;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelButtons;
import edu.ucsd.ncmir.WIB.client.core.components.AcceptCancelHandler;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.Annotation;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.SLASHPlugin;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationMessage;

/**
 *
 * @author spl
 */
public class AnnotationRetryDialog
    extends AbstractDialogBox
    implements AcceptCancelHandler,
	       ValueChangeHandler

{

    private final AnnotationMessage _adm;
    private final SLASHPlugin _sp;
    private final RadioButton _change;
    private final RadioButton _accept;
    private final TextBox _new_name;

    public AnnotationRetryDialog( AnnotationMessage adm, SLASHPlugin sp )

    {

	super( false, true );

	this._adm = adm;
	this._sp = sp;

	super.addTitle( "Annotation Name" );

	Grid main_grid = new Grid( 4, 1 );

	super.setWidget( main_grid );

	Annotation points = adm.getPoints();

	main_grid.setWidget( 0, 0, new HTML( "<p>The object " +
					     "<i>" + points.getObjectName() +
					     "</i> " +
					     "is already in the " +
					     "database.</p>" ) );
	Grid choice_grid = new Grid( 2, 2 );
	main_grid.setWidget( 1, 0, choice_grid );

	choice_grid.setWidget( 0, 0,
			       this._change =
			       new RadioButton( "ad", "Edit" ) );
	this._change.setValue( true );
	this._change.addValueChangeHandler( this );
	choice_grid.setWidget( 0, 1,
			       this._new_name = new TextBox() );
	this._new_name.setText( sp.autoincrement( points.getObjectName() ) );

	choice_grid.setWidget( 1, 0,
			       this._accept =
			       new RadioButton( "ad", "Accept As Is" ) );
	this._accept.addValueChangeHandler( this );

	main_grid.setWidget( 2, 0, new Label( "" ) );
	main_grid.setWidget( 3, 0, new AcceptCancelButtons( this ) );

    }

    @Override
    public boolean onAcceptCancelAction( boolean accepted )

    {

	if ( accepted ) {

	    if ( this._change.getValue() ) {

		this._adm.getPoints().setObjectName( this._new_name.getText() );

		this._sp.annotationInsertion( this._adm );

	    } else if ( this._accept.getValue() )
		this._sp.annotationInsertion( this._adm );

	} else
	    this._sp.cancelAnnotation( this._adm );

	return true;

    }

    @Override
    public void onValueChange( ValueChangeEvent event )

    {

	this._new_name.setEnabled( this._change.getValue() );

    }

}
