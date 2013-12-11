package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import edu.ucsd.ncmir.WIB.client.core.components.resources.icons.SliderClientBundle;

/**
 *
 * @author spl
 */
public class SliderHorizontal
    extends AbsolutePanel
    implements DoubleClickHandler,
	       MouseDownHandler,
	       MouseMoveHandler,
	       MouseUpHandler

{

    private final SliderClientBundle _images = 
	GWT.create( SliderClientBundle.class );

    private double _min_value = 0;
    private double _max_value = 1;
    private double _value = 0;

    private final Image _range_arrows;
    private final Image _bar;
    private final Image _left_arrow;
    private final Image _right_arrow;
    private double _min_mark_step = Double.MAX_VALUE;

    public SliderHorizontal( String swidth )

    {

	this._range_arrows = new Image( this._images.rangearrows() );
	this._bar = new Image( this._images.bar().getSafeUri() );
	this._left_arrow = new Image( this._images.leftarrow() );
	this._right_arrow = new Image( this._images.rightarrow() );

	this.add( this._range_arrows );
	this.add( this._bar );
	this.add( this._left_arrow );
	this.add( this._right_arrow );

	this.addMouseDownHandler( this );
	this.addDoubleClickHandler( this );

	this._range_arrows.addDomHandler( this, MouseDownEvent.getType() );
	this._bar.addDomHandler( this, MouseDownEvent.getType() );
	this._left_arrow.addDomHandler( this, MouseDownEvent.getType() );
	this._right_arrow.addDomHandler( this, MouseDownEvent.getType() );

	this.setupWidth( swidth );

    }

    @Override
    public void onDoubleClick( DoubleClickEvent event )

    {

	// Just eat it.

	event.preventDefault();
	event.stopPropagation();

    }

    @Override
    public void setWidth( String width )

    {

	this.setupWidth( width );

    }

    private void setupWidth( String width_str )

    {

	int width = Integer.parseInt( width_str.replaceAll( "px", "" ) );

	this.setupSlider( width );

    }

    private int _slider_origin;
    private int _rangewidth;
    private int _range_offset;

    private void setupSlider( int width )

    {

	int rawidth = this._images.rangearrows().getWidth();
	int rahalf = rawidth / 2;

	this._rangewidth = width - ( this._left_arrow.getWidth() +
				     rawidth +
				     this._right_arrow.getWidth() +
				     2 );

        int bh = this._images.bar().getHeight();

	this._bar.setWidth( this._rangewidth + "px" );
	this._bar.setHeight( bh + "px" );

	super.setWidgetPosition( this._left_arrow, 0, 0 );

	int offset = ( this._left_arrow.getHeight() - bh ) / 2;

	this._slider_origin = this._left_arrow.getWidth() + 1;

	super.setWidgetPosition( this._bar,
				 this._slider_origin + rahalf,
				 offset );
	super.setWidgetPosition( this._right_arrow,
				 this._left_arrow.getWidth() + 1 +
				 this._rangewidth + rawidth,
				 0 );

	super.setWidth( width + "px" );
	super.setHeight( this._left_arrow.getHeight() + "px" );

	this._yoffset = ( this._left_arrow.getHeight() -
			  this._range_arrows.getHeight() ) / 2;

	super.setWidgetPosition( this._range_arrows,
				 this._slider_origin,
				 this._yoffset );
	this._range_offset = 0;

    }

    private HandlerRegistration _mouse_move_handler_reg = null;
    private HandlerRegistration _mouse_up_handler_reg = null;

    public final HandlerRegistration addMouseDownHandler( MouseDownHandler h )

    {

	return super.addDomHandler( h, MouseDownEvent.getType() );

    }

    public final HandlerRegistration addDoubleClickHandler( DoubleClickHandler h )

    {

	return super.addDomHandler( h, DoubleClickEvent.getType() );

    }

    public HandlerRegistration addMouseMoveHandler( MouseMoveHandler h )

    {

	return super.addDomHandler( h, MouseMoveEvent.getType() );

    }

    public HandlerRegistration addMouseUpHandler( MouseUpHandler h )

    {

	return super.addDomHandler( h, MouseUpEvent.getType() );

    }

    private boolean _tracking = false;
    private int _position;
    private int _yoffset;

    @Override
    public void onMouseDown( MouseDownEvent event )

    {

	event.preventDefault();
	event.stopPropagation();

	if ( this._mouse_move_handler_reg == null ) {

	    this._mouse_move_handler_reg = this.addMouseMoveHandler( this );
	    this._mouse_up_handler_reg = this.addMouseUpHandler( this );

	    this._tracking = event.getSource() == this._range_arrows;
	    if ( !this._tracking ) {

		if ( event.getSource() == this._bar ) {

		    this._range_offset =
			event.getRelativeX( this._bar.getElement() );
		    this.computeValue();
		    this.updateWidgetPosition();

		} else if ( event.getSource() == this._left_arrow )
		    this.startBump( -1 );
		else if ( event.getSource() == this._right_arrow )
		    this.startBump( 1 );

	    }
	    this._position = event.getRelativeX( this.getElement() );

	}

    }

    private BumpTimer _bump_timer = null;
    private boolean _first_bump;

    private void startBump( double deltamul )

    {

	this.bump( deltamul );

	this._bump_timer = new BumpTimer( this, deltamul );
	this._bump_timer.schedule( 500 );
	this._first_bump = true;

    }

    private class BumpTimer
	extends Timer

    {

	private final SliderHorizontal _slider_horizontal;
	private final double _direction;

	BumpTimer( SliderHorizontal slider_horizontal, double direction )

	{

	    this._slider_horizontal = slider_horizontal;
	    this._direction = direction;

	}

        @Override
	public void run()

	{

	    this._slider_horizontal.bumper( this._direction );

	}

    }

    private void bumper( double deltamul )

    {

	this.bump( deltamul );

	if ( this._first_bump ) {

	    this._first_bump = false;

	    this._bump_timer.scheduleRepeating( 100 );

	}

    }

    private void bump( double deltamul )

    {

	double delta;

        if ( this._min_mark_step != Double.MAX_VALUE )
            delta = this._min_mark_step;
        else if ( ( this._max_value - this._min_value ) > 2 )
	    delta = 1;
	else
	    delta = ( this._max_value - this._min_value ) / 100.0;

	this._value += delta * deltamul;

	if ( this._value < this._min_value )
	    this._value = this._min_value;
	else if ( this._value > this._max_value )
	    this._value = this._max_value;

	this.recomputeSliderPosition();

    }

    @Override
    public void onMouseMove( MouseMoveEvent event )

    {

	event.preventDefault();
	event.stopPropagation();

	if ( this._tracking ) {

	    int p = event.getRelativeX( this.getElement() );

	    int dp = p - this._position;

	    this._range_offset += dp;

	    if ( this._range_offset < 0 )
		this._range_offset = 0;
	    else if ( this._rangewidth < this._range_offset )
		this._range_offset = this._rangewidth - 1;

	    this.updateWidgetPosition();
	    this._position += dp;

	    this.computeValue();

	}

    }

    private void computeValue()

    {

	this._value =
	    ( ( this._range_offset / ( this._rangewidth - 1.0 ) ) *
	      ( this._max_value - this._min_value ) ) + this._min_value;

    }

    @Override
    public void onMouseUp( MouseUpEvent event )

    {

	event.preventDefault();
	event.stopPropagation();

	this._mouse_move_handler_reg.removeHandler();
	this._mouse_up_handler_reg.removeHandler();
	this._mouse_move_handler_reg = this._mouse_up_handler_reg = null;
	this.notifyValueUpdateHandlers();

	if ( this._bump_timer != null ) {

	    this._bump_timer.cancel();
	    this._bump_timer = null;

	}

    }

    protected HandlerManager handlerManager = new HandlerManager( this );

    public HandlerRegistration
	addSliderValueUpdateHandler( SliderValueUpdateHandler handler )

    {

	return this.handlerManager.addHandler( SliderValueUpdateEvent.TYPE,
					       handler );

    }

    public void setMinValue( double value )

    {

	this._min_value = value;
	if ( this._value < this._min_value )
	    this._value = this._min_value;
	this.recomputeSliderPosition();
	this.notifyValueUpdateHandlers();

    }

    public void setMaxValue( double value )

    {

	this._max_value = value;
	if ( this._value > this._max_value )
	    this._value = this._max_value;
	this.recomputeSliderPosition();
	this.notifyValueUpdateHandlers();

    }

    public double getValue()

    {

	return this._value;

    }

    public void setValue( double value )

    {

	if ( value < this._min_value )
	    value = this._min_value;
	else if ( value > this._max_value )
	    value = this._max_value;
	this._value = value;
	this.recomputeSliderPosition();
	this.notifyValueUpdateHandlers();

    }

    private void notifyValueUpdateHandlers()

    {

	SliderValueUpdateEvent event =
	    new SliderValueUpdateEvent( this.getValue() );
	this.handlerManager.fireEvent( event );

    }

    private void recomputeSliderPosition()

    {

	double dvalue = this._max_value - this._min_value;
	double voffset = this._value - this._min_value;

	this._range_offset =
            ( int ) ( ( voffset / dvalue ) * this._rangewidth );
	this.updateWidgetPosition();

    }

    private void updateWidgetPosition()

    {

	this.setWidgetPosition( this._range_arrows,
				this._range_offset + this._slider_origin,
				this._yoffset );

    }

    void setMinMarkStep( double min_mark_step )

    {

        this._min_mark_step = min_mark_step;

    }

}
