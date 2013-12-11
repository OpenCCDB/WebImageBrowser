package edu.ucsd.ncmir.WIB.client.core.components;

import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.ResetMessage;

/**
 * Horizontal Slider bar.
 * @author spl
 */
public class HorizontalSliderBar
    extends SliderHorizontal
    implements MessageListener,
	       SliderValueUpdateHandler

{

    private double _default_value = 0;
    private double _delta = Double.MAX_VALUE;

    private final Message _message;

    /**
     * Creates a <code>HorizontalSliderBar</code> object.
     * @param width The width of the object as a <code>String</code>.
     * For example, <code>"100px"</code>.
     * @param message A <code>Message</code> to be sent when the bar's
     * value changes.
     */

    public HorizontalSliderBar( String width, Message message )

    {

	super( width );
        this._message = message;

	super.addSliderValueUpdateHandler( this );

	MessageManager.registerListener( ResetMessage.class, this );

    }

    /**
     * Creates a <code>HorizontalSliderBar</code> object.
     * @param min_value Minimum allowable value.
     * @param default_value Default value (will be reset to this when
     * <code>ResetMessage</code> is received).
     * @param max_value Maximum allowable value.
     * @param width The width of the object as a <code>String</code>.
     * For example, <code>"100px"</code>.
     * @param message A <code>Message</code> to be sent when the bar's
     * value changes.
     */

    public HorizontalSliderBar( double min_value, double max_value,
				double default_value,
				String width,
                                Message message )

    {

	this( width, message );

	this.setSliderParameters( min_value, max_value, default_value );


    }

    public final void setSliderParameters( double min_value, double max_value,
					   double default_value )

    {

	super.setMinValue( min_value );
	super.setMaxValue( max_value );

	this.setDefaultValue( default_value );

    }

    public final void setSliderParameters( double min_value, double max_value,
					   double default_value, double delta )
    {

	this.setSliderParameters( min_value, max_value, default_value );
	this.setDelta( delta );

    }
    
    private void setDefaultValue( double default_value )

    {

	this._default_value = default_value;

        this.setValue( default_value );

    }

    private boolean _transmit_value = true;

    /**
     * Updates the value of the slider without firing the handler.
     * @param value The value to be set.
     */
    public void setValueOnly( double value )

    {

	// Turn off the handler.
	this._transmit_value = false;
        this.setValue( value );
	this._transmit_value = true;

    }

    @Override
    public void setValue( double value )

    {

	if ( this._delta != Double.MAX_VALUE )
	    value = Math.round( value / this._delta ) * this._delta;

        super.setValue( value );

    }

    @Override
    public void action( Message m, Object o )

    {

        this.setValue( this._default_value );

    }

    private boolean _initial = true; // To prevent premature Message firing.

    /**
     * Fired when the bar value changes.
     * @param event The <code>SliderValueUpdateEvent</code>.
     */

    @Override
    public void onBarValueChanged( SliderValueUpdateEvent event )

    {

	if ( this._transmit_value && !this._initial ) {

	    double value = event.getValue();

	    boolean changed = false;
	    if ( this._delta != Double.MAX_VALUE ) {

		value = Math.round( value / this._delta ) * this._delta;
		changed = true;

	    }

	    this._message.send( value );
	    if ( changed )
		this.setValueOnly( value );

	}

	// Turn off the initial flag.  The SliderBar object fires a
	// spurious SliderValueUpdateEvent when the object is loaded.
	// This prevents it being propagated.

	this._initial = false;

    }

    public void bump( double bump )

    {

	if ( this._delta != Double.MAX_VALUE )
	    bump *= this._delta;
        super.setValue( super.getValue() + bump );

    }

    public void setDelta( double delta )

    {

        this._delta = delta;
        super.setMinMarkStep( delta );

    }

    public void setArrowBumpDelta( double delta )

    {

        super.setMinMarkStep( delta );

    }

}
