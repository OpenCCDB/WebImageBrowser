package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * @author spl
 */
public class SliderValueUpdateEvent
    extends GwtEvent<SliderValueUpdateHandler>

{

    public static Type<SliderValueUpdateHandler> TYPE =
	new Type<SliderValueUpdateHandler>();

    private double value;

    public SliderValueUpdateEvent( double position )

    {

	this.value = position;

    }

    public double getValue()

    {

	return value;

    }

    public void setValue( double position )

    {

	this.value = position;

    }

    @Override
    public Type<SliderValueUpdateHandler> getAssociatedType()

    {

	return TYPE;

    }

    @Override
    protected void dispatch( SliderValueUpdateHandler handler )

    {

	handler.onBarValueChanged( this );

    }

}
