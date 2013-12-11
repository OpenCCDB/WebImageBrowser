package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import edu.ucsd.ncmir.WIB.client.core.components.resources.icons.SpinnerClientBundle;
import java.util.ArrayList;

/**
 *
 * @author spl
 */
public class UpDownArrowButton
    extends AbsolutePanel

 {

     private final SpinnerClientBundle _images =
	 GWT.create( SpinnerClientBundle.class );
     private int _width;
     private int _height;
     private final ArrayList<UpDownBumpHandler> _udbh_list =
	 new ArrayList<UpDownBumpHandler>();
    private final Image _up_arrow;
    private final Image _down_arrow;
    private final Image _up_arrow_deactivated;
    private final Image _down_arrow_deactivated;

     public UpDownArrowButton()

     {

	 this( null );

     }

     public UpDownArrowButton( UpDownBumpHandler udbh )

     {

         if ( udbh != null )
             this._udbh_list.add( udbh );

	 this._up_arrow = new Image( this._images.up_arrow() );
	 this._down_arrow = new Image( this._images.down_arrow() );

	 this._up_arrow_deactivated =
	     new Image( this._images.up_arrow_deactivated() );
	 this._down_arrow_deactivated =
	     new Image( this._images.down_arrow_deactivated() );

	 this.activateButtons( true, true );

	 this.add( this._up_arrow_deactivated, 0, 0 );
	 this.add( this._down_arrow_deactivated,
		   0, this._up_arrow_deactivated.getHeight() + 2 );

	 this.add( this._up_arrow, 0, 0 );
	 this.add( this._down_arrow, 0, this._up_arrow.getHeight() + 2 );

	 this._up_arrow_deactivated.setVisible( false );
	 this._down_arrow_deactivated.setVisible( false );

	 this._width = this._up_arrow.getWidth();
	 this._height = this._up_arrow.getHeight() +
	     this._down_arrow.getHeight() + 2;

	 this.setPixelSize( this._width, this._height );

     }

     private HandlerRegistration _up_handler = null;
     private HandlerRegistration _down_handler = null;

     public final void activateButtons( boolean activate_up,
					boolean activate_down )

     {


	 this._up_arrow.setVisible( activate_up );
	 this._down_arrow.setVisible( activate_down );

	 this._up_arrow_deactivated.setVisible( activate_up );
	 this._down_arrow_deactivated.setVisible( activate_down );

	 if ( this._up_handler != null )
	     this._up_handler.removeHandler();

	 if ( this._down_handler != null )
	     this._down_handler.removeHandler();

	 this._up_handler =
	     this._up_arrow.addMouseDownHandler( new Up( this ) );
	 this._down_handler =
	     this._down_arrow.addMouseDownHandler( new Down( this ) );

     }

     public void addUpDownBumpHandler( UpDownBumpHandler udbh )

     {

	 this._udbh_list.add( udbh );

     }

     public void removeUpdDownBumpHandler( UpDownBumpHandler udbh )

     {

	 this._udbh_list.remove( udbh );

     }

     public final int getPixelWidth()

     {

	 return this._width;

     }

     public final int getPixelHeight()

     {

	 return this._height;

     }

     private void bump( int bumpvalue )

     {

	 for ( UpDownBumpHandler udbh : this._udbh_list )
	     udbh.bump( bumpvalue );

     }

     private static abstract class BumpHandler
	 implements MouseDownHandler

     {

	 @Override
	 public void onMouseDown( MouseDownEvent event )

	 {

	     event.preventDefault();
	     event.stopPropagation();

	     this.bump();

	 }

	 abstract protected void bump();

     }

     private class Up
	 extends BumpHandler

     {

	 private final UpDownArrowButton _audab;

	 Up( UpDownArrowButton audab )

	 {

	     this._audab = audab;

	 }

	 @Override
	 protected void bump()

	 {

	     this._audab.bump( 1 );

	 }

     }

     private class Down
	 extends BumpHandler

     {

	 private final UpDownArrowButton _audab;

	 Down( UpDownArrowButton audab )

	 {

	     this._audab = audab;

	 }

	 @Override
	 protected void bump()

	 {

	     this._audab.bump( -1 );

	 }

     }

}





