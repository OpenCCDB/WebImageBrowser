package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWT;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.drawable.ScaleFactor;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.BrightnessMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ContrastMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.MousePositionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetActionButtonListenerMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.StatusMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.TimestepMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ZoomMessage;

/**
 *
 * @author spl
 */
public class StatusPanel
    extends AbstractWIBPanel
    implements ClickHandler

{

    private final Label _brightness = new Label();
    private final Label _contrast = new Label();
    private final Label _scale = new Label();
    private final Readout _x = new Readout( "x" );
    private final Readout _y = new Readout( "y" );
    private final Label _timestep = new Label();
    private final Label _misc = new Label();
    private final Button _action_button = new Button();

    interface ClientImageBundle
	extends ClientBundle

    {

	@Source("resources/icons/logo.png")
	ImageResource image();

    }

    ClientImageBundle image = GWT.create( ClientImageBundle.class );

    /**
     *
     */
    public StatusPanel()

    {

	super( BrightnessMessage.class,
               ContrastMessage.class,
               ZoomMessage.class,
               TimestepMessage.class,
               MousePositionMessage.class,
               StatusMessage.class,
	       SetActionButtonListenerMessage.class );

        this.setStyleName( "WIB-status-panel" );

	Grid hp = new Grid( 1, 10 );
	hp.getElement().getStyle().setPaddingTop( 0, Style.Unit.PX );
	hp.getElement().getStyle().setPaddingBottom( 0, Style.Unit.PX );

	hp.setWidget( 0, 0, new Image( image.image() ) );
	hp.setWidget( 0, 1, this._brightness );
	hp.setWidget( 0, 2, this._contrast );
	hp.setWidget( 0, 3, this._scale );
	hp.setWidget( 0, 4, this._timestep );
        if ( MGWT.getOsDetection().isDesktop() ) {

	    hp.setWidget( 0, 5, new Label( "Position:" ) );
	    hp.setWidget( 0, 6, this._x );
	    hp.setWidget( 0, 7, this._y );

	}
        hp.setWidget( 0, 8, this._action_button );
        hp.setWidget( 0, 9, this._misc );

	HTMLTable.CellFormatter formatter = hp.getCellFormatter();
	for ( int i = 0; i < hp.getColumnCount(); i++ ) {

	    Widget w = hp.getWidget( 0, i );
	    if ( w != null ) {

		if ( w instanceof Label )
		    w.setStyleName( "WIB-status" );
		else if ( w instanceof Button )
		    w.setStyleName( "StatusButton" );
		
		w.getElement().getStyle().setPaddingTop( 0, Style.Unit.PX );
		w.getElement().getStyle().setPaddingBottom( 0, Style.Unit.PX );
		w.getElement().getStyle().setPaddingRight( 10, Style.Unit.PX );
		
		formatter.setAlignment( 0, i,
					HasHorizontalAlignment.ALIGN_CENTER,
					HasVerticalAlignment.ALIGN_MIDDLE );

	    }

	}

	Style s = this._misc.getElement().getStyle();
	s.setFontStyle( Style.FontStyle.ITALIC );

	this._action_button.setVisible( false );
	this._action_button.addClickHandler( this );

        this.add( hp );


    }

    private class Readout
	extends Grid

    {

	private final Label _data = new Label();

	Readout( String name )

	{

	    super( 1, 2 );

	    HTMLTable.CellFormatter formatter = this.getCellFormatter();

	    formatter.setAlignment( 0, 0,
				    HasHorizontalAlignment.ALIGN_LEFT,
				    HasVerticalAlignment.ALIGN_MIDDLE );
	    formatter.setAlignment( 0, 1,
				    HasHorizontalAlignment.ALIGN_RIGHT,
				    HasVerticalAlignment.ALIGN_MIDDLE );
	    Label lbl = new Label( name + ": " );

	    lbl.setStyleName( "WIB-status" );
	    this.setWidget( 0, 0, lbl );
	    this._data.setStyleName( "WIB-status" );
	    this.setWidget( 0, 1, this._data );
	    this._data.getElement().getStyle().setWidth( 50, Style.Unit.PX );

	}

	void setText( int value )

	{

	    this._data.setText( value + "" );

	}

    }

    private Message _button_message = null;
    private Object _button_data = null;

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof BrightnessMessage )
	    this._brightness.setText( "Brightness: " +
				      StatusPanel.makeString( o, 1 ) );
	else if ( m instanceof ContrastMessage )
	    this._contrast.setText( "Contrast: " +
				    StatusPanel.makeString( o, 1 ) );
	else if ( m instanceof ZoomMessage )
	    this._scale.setText( "Scale: " +
				 StatusPanel.makeString( ( ( ScaleFactor ) o ).
							 getScale(), 3 ) );
	else if ( m instanceof TimestepMessage )
	    this._timestep.setText( "Timestep: " +
				    ( ( Integer ) o ).intValue() );
	else if ( m instanceof MousePositionMessage ) {

	    Point xy = ( Point ) o;

	    this._x.setText( ( int ) xy.getX() );
	    this._y.setText( ( int ) xy.getY() );

	} else if ( m instanceof StatusMessage )
            this._misc.setText( ( ( StatusMessage ) m ).getMessage() );
	else if ( m instanceof SetActionButtonListenerMessage ) {

	    SetActionButtonListenerMessage sablm =
		( SetActionButtonListenerMessage ) m;

	    if ( sablm.isEnableMessage() ) {

		this._action_button.setText( sablm.getText() );
		this._action_button.setVisible( true );
		this._button_message = sablm.getButtonMessage();
		this._button_data = sablm.getData();

	    } else
		this._action_button.setVisible( false );

	}

    }

    @Override
    public void onClick( ClickEvent event )

    {

	this._button_message.send( this._button_data );

    }

    private static String makeString( Object o, int places )

    {

	return StatusPanel.stringify( ( ( Double ) o ).doubleValue(), places );

    }

    // Because there're no printf() function available under GWT. . .
    
    private static String stringify( double v, int places )

    {

	String sign = "";

	if ( v < 0 ) {

	    sign = "-";
	    v = -v;

	}

	v *= Math.pow( 10, places );
	v += 0.5;

	String sv = v + "";

	int li = sv.lastIndexOf( "." );

	if ( li > -1 )
	    sv = sv.substring( 0, li );

	while ( sv.length() < ( places + 1 ) )
	    sv = "0" + sv;

	int lipart = sv.length() - places;
	String ipart = sv.substring( 0, lipart );
	String fpart = sv.substring( lipart );

	return sign + ipart + "." + fpart;

    }

}
