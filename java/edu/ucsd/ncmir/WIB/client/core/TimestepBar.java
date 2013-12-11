package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.user.client.ui.Label;
import edu.ucsd.ncmir.WIB.client.core.components.CenteredGrid;
import edu.ucsd.ncmir.WIB.client.core.components.HorizontalSlider;
import edu.ucsd.ncmir.WIB.client.core.messages.TimestepMessage;

/**
 *
 * @author spl
 */
public class TimestepBar
    extends CenteredGrid

{

    public TimestepBar( int timesteps )

    {

	super( 2, 1 );

	this.setWidget( 0, 0, new Label( "Timestep" ) );

	HorizontalSlider bar = new HorizontalSlider( new TimestepMessage() );
	bar.setSliderParameters( 0, timesteps - 1, ( timesteps - 1 ) / 2 );
	bar.setWidth( "150px" );
	this.setWidget( 1, 0, bar );

    }

}
