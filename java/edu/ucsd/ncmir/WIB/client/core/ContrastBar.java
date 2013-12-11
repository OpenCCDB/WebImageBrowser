package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.user.client.ui.Label;
import edu.ucsd.ncmir.WIB.client.core.components.CenteredGrid;
import edu.ucsd.ncmir.WIB.client.core.components.HorizontalSliderBar;
import edu.ucsd.ncmir.WIB.client.core.messages.ContrastMessage;

/**
 *
 * @author spl
 */
public class ContrastBar
    extends CenteredGrid
{

    public ContrastBar()

    {

	super( 2, 1 );

	this.setWidget( 0, 0, new Label( "Contrast" ) );

	HorizontalSliderBar bar =
	    new HorizontalSliderBar( -1.0, 1.0, 0.0, "150px",
				     new ContrastMessage() );
        bar.setDelta( 0.1 );

        new ContrastMessage().send( 0.0 );

	this.setWidget( 1, 0, bar );

    }

}