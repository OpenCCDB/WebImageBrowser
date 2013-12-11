package edu.ucsd.ncmir.WIB.client.core.components.mobile;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.MSlider;
import edu.ucsd.ncmir.WIB.client.core.components.HorizontalSliderInterface;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class MobileHorizontalSlider
    extends MSlider
    implements HorizontalSliderInterface,
	       ValueChangeHandler<Integer>

{
    
    private final Message _message;

    public MobileHorizontalSlider( Message message )
        
    {
        
        this._message = message;
	super.addValueChangeHandler( this );

    }

    private int _min_value;
    private int _default_value;

    @Override
    public void setSliderParameters( int min_value,
				     int max_value,
                                     int default_value )
    {

	this._min_value = min_value;
	super.setMax( max_value - min_value + 1 );
	this.setSliderValue( default_value );

    }

    @Override
    public void setSliderValue( double value )
    {

	super.setValue( ( int ) ( value - this._min_value ) );

    }

    
    @Override
    public void setSliderValueOnly( int value )
        
    {
        
        this._ignore = true;
	this.setSliderValue( value );
	this._ignore = false;

    }

    @Override
    public void setWidth( String size )
        
    {

        this._ignore = false;
	double value = super.getValue();

	super.setWidth( size );
	this.setSliderValueOnly( ( int ) value);
	this._ignore = true;

    }

    @Override
    public void updateHandler( double value )
    {
        
        this._message.send( value );
        
    }

    private boolean _ignore = false;

    @Override
    public void onValueChange( ValueChangeEvent<Integer> event )

    {

	if ( !this._ignore )
	    this.updateHandler( event.getValue().intValue() - this._min_value );

    }

    @Override
    public double getSliderValue()
    {
        
        return this.getValue() + this._min_value;
        
    }

    @Override
    public Widget widget()
    {
        
        return this;
        
    }

}
