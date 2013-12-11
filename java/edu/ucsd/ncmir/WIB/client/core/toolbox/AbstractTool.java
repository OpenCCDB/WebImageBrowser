package edu.ucsd.ncmir.WIB.client.core.toolbox;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 *
 * @author spl
 */
abstract public class AbstractTool
    extends ToggleButton

{

    AbstractTool( Image icon, String tooltip )

    {

	super( icon );

	super.setTitle( tooltip );
	super.setPixelSize( icon.getWidth(), icon.getHeight() );
	super.getElement().getStyle().setPadding( 0, Style.Unit.PX );
	super.getElement().getStyle().setBackgroundImage( "none" );
	super.getElement().getStyle().setBorderWidth( 1, Style.Unit.PX );
	super.getElement().getStyle().setBorderColor( "transparent" );

    }

}
