package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.user.client.ui.Widget;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.ResetMessage;

/**
 * HorizontalSlider bar.
 * @author spl
 */
public class HorizontalSlider
    extends SliderHorizontal
    implements HorizontalSliderInterface,
               MessageListener,
               SliderValueUpdateHandler

{

    private final Message _message;

    /**
     * Creates a <code>HorizontalSlider</code> object.
     */

    public HorizontalSlider( Message message )

    {

	super( "150px" );
        this._message = message;

	super.addSliderValueUpdateHandler( this );

	MessageManager.registerListener( ResetMessage.class, this );

    }

    private int _min_value = 0;
    private int _max_value = 1;
    private int _default_value = 0;

    @Override
    public final void setSliderParameters( int min_value, int max_value,
					   int default_value )

    {

	this._min_value = min_value;
	this._max_value = max_value;
	this._default_value = default_value;

	this.setMaxValue( this._max_value - this._min_value );
        this.setSliderValue( default_value );
	super.setMinMarkStep( 1 );

    }

    @Override
    public void setWidth( String size )

    {

        this._transmit_value = false;
	double value = super.getValue();

	super.setWidth( size );
	super.setValue( value );
	this._transmit_value = true;

    }

    private boolean _transmit_value = true;

    /**
     * Updates the value of the slider without firing the handler.
     * @param value The value to be set.
     */

    @Override
    public void setSliderValueOnly( int value )

    {

	// Turn off the handler.
	this._transmit_value = false;
        this.setSliderValue( value );
	this._transmit_value = true;

    }

    @Override
    public void setSliderValue( double value )

    {

        super.setValue( value - this._min_value );

    }

    @Override
    public void action( Message m, Object o )

    {

        this.setSliderValue( this._default_value );

    }

    private boolean _initial = true; // To prevent premature Message firing.

    /**
     * Fired when the bar value changes.
     * @param event The <code>BarValueChangedEvent</code>.
     */

    @Override
    public void onBarValueChanged( SliderValueUpdateEvent event )

    {

	if ( this._transmit_value && !this._initial )
	    this.updateHandler( event.getValue() + this._min_value );

	// Turn off the initial flag.  The SliderBar object fires a
	// spurious BarValueChangedEvent when the object is loaded.
	// This prevents it being propagated.

	this._initial = false;

    }

    @Override
    public void updateHandler( double value )
        
    {
        
        this._message.send( value );
        
    }

    @Override
    public Widget widget()
        
    {
        
        return this;
        
    }

    @Override
    public double getSliderValue()
        
    {
        
        return this.getValue();
        
    }

}
