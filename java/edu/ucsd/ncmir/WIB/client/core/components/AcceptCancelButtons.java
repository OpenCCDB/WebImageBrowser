package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import edu.ucsd.ncmir.WIB.client.core.components.mobile.MobileButton;

/**
 *
 * @author spl
 */
public class AcceptCancelButtons
    extends Grid
    implements ClickHandler,
	       TapHandler

{

    private final Button _dt_accept = new Button( "Accept" );
    private final Button _dt_cancel = new Button( "Cancel" );

    private final MobileButton _mobile_accept = new MobileButton( "Accept" );
    private final MobileButton _mobile_cancel = new MobileButton( "Cancel" );

    private final AcceptCancelHandler _handler;

    /**
     * A convenience class encapsulating a pair of buttons,
     * <code>Accept</code> and <code>Cancel</code>.  When one or the
     * other is clicked, the containing object, a subclass of a
     * <code>DialogBox</code>, is deleted from the display and the
     * <code>AcceptCancelHandler</code>
     * <code>onAcceptCancelAction</code> method is called.
     * @param handler The <code>AcceptCancelHandler</code>.
     */
    public AcceptCancelButtons( AcceptCancelHandler handler )

    {

        super( 1, 2 );
        this._handler = handler;
	if ( MGWT.getOsDetection().isDesktop() ) {

	    super.setWidget( 0, 0, this._dt_accept );
	    super.setWidget( 0, 1, this._dt_cancel );
	    this._dt_accept.addClickHandler( this );
	    this._dt_cancel.addClickHandler( this );

	} else {

	    super.setWidget( 0, 0, this._mobile_accept );
	    super.setWidget( 0, 1, this._mobile_cancel );
	    this._mobile_accept.addTapHandler( this );
	    this._mobile_cancel.addTapHandler( this );

	}

    }

    @Override
    public void onClick( ClickEvent event )

    {

	Button button = ( Button ) event.getSource();

	this.accept( button == this._dt_accept );

    }

    private void accept( boolean accepted )

    {

        if ( this._handler.onAcceptCancelAction( accepted ) ) {

	    for ( Widget p = this.getParent(); p != null; p = p.getParent() )
		if ( p instanceof DialogBox ) {

		    p.setVisible( false );
		    p.removeFromParent();

		}

	}

    }

    public void setEnabled( boolean enabled )

    {

	if ( MGWT.getOsDetection().isDesktop() ) {

	    this._dt_accept.setEnabled( enabled );
	    this._dt_cancel.setEnabled( enabled );

	} else {

	    this._mobile_accept.setVisible( enabled );
	    this._mobile_cancel.setVisible( enabled );

	}

    }

    @Override
    public void onTap( TapEvent event )
    {
        
	MobileButton button = ( MobileButton ) event.getSource();

	this.accept( button == this._mobile_accept );

    }

}
