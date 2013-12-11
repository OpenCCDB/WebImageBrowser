package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.user.client.ui.Label;
import edu.ucsd.ncmir.WIB.client.core.components.CenteredGrid;
import edu.ucsd.ncmir.WIB.client.core.components.HorizontalSliderBar;
import edu.ucsd.ncmir.WIB.client.core.messages.BrightnessMessage;

/**
 *
 * @author spl
 */
public class BrightnessBar
    extends CenteredGrid

{

    public BrightnessBar()

    {

	super( 2, 1 );

	this.setWidget( 0, 0, new Label( "Brightness" ) );

	HorizontalSliderBar bar =
	    new HorizontalSliderBar( -1.0, 1.0, 0.0, "150px",
				     new BrightnessMessage() );
        bar.setDelta( 0.1 );

        new BrightnessMessage().send( 0.0 );

	this.setWidget( 1, 0, bar );

    }

}