package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import edu.ucsd.ncmir.WIB.client.core.Configuration;
import edu.ucsd.ncmir.WIB.client.core.components.CenteredGrid;
import edu.ucsd.ncmir.WIB.client.core.image.AbstractThumbnail;
import edu.ucsd.ncmir.WIB.client.core.image.ImageFactoryInterface;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.ParameterUpdateMessage;

/**
 *
 * @author spl
 */
public class ControlsPanel
    extends CenteredGrid
    implements MessageListener,
	       ResizeHandler

{

    interface ClientImageBundle
	extends ClientBundle

    {

	@Source("resources/icons/web-image-browser.png")
	ImageResource wib_logo();

    }

    ClientImageBundle _wib_logo = GWT.create( ClientImageBundle.class );

    public ControlsPanel()

    {

	super( 3, 1 );

        this.setStylePrimaryName( "WIB-controls" );

	MessageManager.registerListener( ParameterUpdateMessage.class, this );

	Image logo = new Image( this._wib_logo.wib_logo() );

	Style s = logo.getElement().getStyle();

	s.setBorderWidth( 10, Style.Unit.PX );
	s.setBorderStyle( Style.BorderStyle.SOLID );
	s.setBorderColor( "transparent" );

	this.setWidget( 0, 0, logo );
        this.setWidget( 2, 0, new ZoomBar() );
	Window.addResizeHandler( this );

    }

    @Override
    public void onResize( ResizeEvent event )

    {

	this.reposition();

    }

    private InteractionPanel _interaction_panel;

    @Override
    protected void onLoad()

    {

	for ( Widget w = this.getParent(); w != null; w = w.getParent() )
	    if ( w instanceof InteractionPanel ) {

		this._interaction_panel = ( InteractionPanel ) w;
		break;

	    }

    }

    private void reposition()

    {

	if ( this._interaction_panel != null ) {

	    int width = this._interaction_panel.getOffsetWidth();

	    this._interaction_panel.setWidgetPosition( this, width - 170, 10 );

	}

    }

    public void addControl( Widget w )

    {

	int rc = this.getRowCount();

	this.resizeRows( rc + 1 );
	this.setWidget( rc, 0, w );

    }

    @Override
    public final void action( Message m, Object o )

    {

	ParameterUpdateMessage pum = ( ParameterUpdateMessage ) m;

	ImageFactoryInterface image_factory =
	    Configuration.getPlugin().getImageFactory();

	AbstractThumbnail thumbnail =
	    image_factory.getThumbnail( pum.getQuaternion(),
					pum.getPlane(),
					pum.getTimestep() );

	this.setWidget( 1, 0, thumbnail );
	this.setVisible( true );
        thumbnail.setVisible( true );
	this.reposition();

    }

}
