package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.user.client.ui.Label;
import edu.ucsd.ncmir.WIB.client.core.KeyData;
import edu.ucsd.ncmir.WIB.client.core.components.CenteredGrid;
import edu.ucsd.ncmir.WIB.client.core.components.HorizontalSliderBar;
import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.BumpZoomMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.KeyPressMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetZoomBarMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ZoomMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ZoomParametersMessage;

/**
 *
 * @author spl
 */
public class ZoomBar
    extends CenteredGrid
    implements MessageListener

{

    private final HorizontalSliderBar _bar;

    public ZoomBar()

    {

	super( 2, 1 );

	this.setWidget( 0, 0, new Label( "Zoom" ) );

	this._bar = new HorizontalSliderBar( "150px", new ZoomMessage() );

	this.setWidget( 1, 0, this._bar );

        MessageManager.registerAsListener( this,
					   BumpZoomMessage.class,
					   KeyPressMessage.class,
					   SetZoomBarMessage.class,
					   ZoomParametersMessage.class );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof BumpZoomMessage )
	    this._bar.bump( ( ( Double ) o ).doubleValue() );
	else if ( m instanceof SetZoomBarMessage )
	    this._bar.setValue( ( ( ScaleFactor ) o ).getExponent() );
	else if ( m instanceof ZoomParametersMessage ) {

	    ZoomParametersMessage zpm = ( ZoomParametersMessage ) m;

	    this._bar.setSliderParameters( zpm.getMaxZoomOut(),
					   zpm.getMaxZoomIn(),
					   zpm.getDefaultZoom(),
					   zpm.getZoomDelta() );
            this._bar.setArrowBumpDelta( 1 );

	} else if ( m instanceof KeyPressMessage )
	    this.bumpZoom( ( KeyData ) o );

    }

    private void bumpZoom( KeyData key_data )

    {

	switch ( key_data.getKeyCode() ) {

	case '_':
        case '-': {

	    new BumpZoomMessage().send( -1.0 );
	    break;

	}
	case '+':
	case '=': {

	    new BumpZoomMessage().send( 1.0 );
	    break;

	}

	}

    }

}
