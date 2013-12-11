package edu.ucsd.ncmir.WIB.client.core.toolbox;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Image;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
class ToggleTool
    extends AbstractTool

{

    private final Message _toggle_message;

    public ToggleTool( Image icon, String tooltip, Message toggle_message )

    {

	super( icon, tooltip );
	this._toggle_message = toggle_message;
	super.getElement().getStyle().setBorderWidth( 3, Style.Unit.PX );
	super.getElement().getStyle().setBorderColor( "transparent" );

    }

    private boolean _state = false;

    @Override
    protected void onClick()

    {

	this._toggle_message.send( this._state = !this._state );
	super.getElement().getStyle().setBorderColor( this._state ?
						      "red" :
						      "transparent" );

    }

}
