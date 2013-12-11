package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.user.client.ui.Grid;
import edu.ucsd.ncmir.WIB.client.debug.Debug;
import net.auroris.ColorPicker.client.ColorPicker;

/**
 *
 * @author spl
 */
public class ColorPickerBox
    extends AbstractDialogBox
    implements AcceptCancelHandler

{

    private final ColorPickerHandler _handler;
    private final ColorPicker _color_picker = new ColorPicker();

    public ColorPickerBox( ColorPickerHandler handler )

    {

	super( false, true );

	this._handler = handler;

	super.addTitle( "Color Chooser" );

	Grid g0 = new Grid( 2, 1 );

	g0.setWidget( 0, 0, this._color_picker );
	g0.setWidget( 1, 0, new AcceptCancelButtons( this ) );
        super.setWidget( g0 );

    }

    @Override
    public boolean onAcceptCancelAction( boolean accepted )

    {

	if ( accepted )
	    this._handler.onColorPicker( this._color_picker.getHexColor() );

        return true;

    }

    public void setRGB( int red, int green, int blue )

    {

	try {

	    this._color_picker.setRGB( red, green, blue );

	} catch ( Exception e ) {

	    Debug.traceback( e );

	}

    }

}
