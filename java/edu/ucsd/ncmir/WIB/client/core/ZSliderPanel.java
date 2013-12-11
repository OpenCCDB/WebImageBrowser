package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.googlecode.mgwt.ui.client.MGWT;
import edu.ucsd.ncmir.WIB.client.core.components.FilteredDataCallback;
import edu.ucsd.ncmir.WIB.client.core.components.HorizontalSlider;
import edu.ucsd.ncmir.WIB.client.core.components.HorizontalSliderInterface;
import edu.ucsd.ncmir.WIB.client.core.components.IntegerMessageInputBox;
import edu.ucsd.ncmir.WIB.client.core.components.mobile.MobileHorizontalSlider;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.DeleteZSliderPanelMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.InitializePlaneMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetPlaneSliderPositionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.UpdatePlanePositionMessage;
import edu.ucsd.ncmir.WIB.client.core.panel.AbstractWIBPanel;

/**
 *
 * @author spl
 */
public class ZSliderPanel
    extends AbstractWIBPanel
    implements ResizeHandler,
	       FilteredDataCallback

{

    private IntegerMessageInputBox _input;
    private final HorizontalSliderInterface _slider;

    /**
     * Creates an instance of <code>ZSliderPanel</code>.
     */
    public ZSliderPanel()

    {

	super( DeleteZSliderPanelMessage.class,
	       SetPlaneSliderPositionMessage.class,
               InitializePlaneMessage.class );
        if ( MGWT.getOsDetection().isDesktop() )
            this._slider = new PlaneSlider();
        else
            this._slider = new MobilePlaneSlider();
        this.setStylePrimaryName( "WIB-controls" );

	this.setVisible( false );
	this._input =
            new IntegerMessageInputBox( UpdatePlanePositionMessage.class,
                                        this );
	Window.addResizeHandler( this );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof SetPlaneSliderPositionMessage ) {

	    int plane = ( ( Integer ) o ).intValue();

	    this._slider.setSliderValueOnly( plane );
	    this._input.setText( plane + "" );

	} else if ( m instanceof InitializePlaneMessage ) {

	    this._input.setWidth( "38px" );
	    this._input.setHeight( "23px" );
	    this.add( this._slider.widget(), 0, 1 );
	    this._input.setHeight( "21px" );
	    this._input.setStylePrimaryName( "WIB-input" );
	    this.add( this._input );

	    this.setVisible( true );

	    int planes = ( ( Integer ) o ).intValue();
	    int init_planes = ( planes - 1 ) / 2;

	    this._input.setText( "" + init_planes );
	    this._slider.setSliderParameters( 0, planes - 1, init_planes );
	    this.setPlaneSliderSize();
	    this._slider.setSliderParameters( 0, planes - 1, init_planes );

	} else if ( m instanceof DeleteZSliderPanelMessage ) {

	    LayoutPanel parent = ( LayoutPanel ) this.getParent();

	    parent.remove( this );
	    MessageManager.deregisterListener( DeleteZSliderPanelMessage.class,
					       this );
	    MessageManager.deregisterListener( SetPlaneSliderPositionMessage.class,
					       this );
	    MessageManager.deregisterListener( InitializePlaneMessage.class,
					       this );

	}

    }

    private void setPlaneSliderSize()

    {

	int x = this._input.getOffsetWidth();
	int w = this.getOffsetWidth();

	this.setWidgetPosition( this._input, w - x, 1 );
	this._input.setHeight( "21px" );

	this._slider.setWidth( ( ( w - x ) - 10 ) + "px" );
	this._slider.setHeight( "23px" );

    }

    @Override
    public void onResize( ResizeEvent event )

    {

	if ( this.isVisible() )
	    this.setPlaneSliderSize();

    }

    @Override
    public boolean setValue( double value )

    {

        this._slider.setSliderValue( value );

        return true;

    }

    @Override
    public double getValue()

    {

        return this._slider.getSliderValue();

    }
    
    private static class MobilePlaneSlider
        extends MobileHorizontalSlider
    
    {
        
        MobilePlaneSlider()
            
        {
            
            super( new UpdatePlanePositionMessage() );
            
        }
        
    }

    private static class PlaneSlider
	extends HorizontalSlider

    {


	public PlaneSlider()

	{

	    super( new UpdatePlanePositionMessage() );
	    this.setStylePrimaryName( "WIB-PlaneSlider" );

	}

    }

}

