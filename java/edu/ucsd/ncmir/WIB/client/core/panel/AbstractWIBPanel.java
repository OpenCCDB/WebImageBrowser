package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;

/**
 *
 * @author spl
 */

public abstract class AbstractWIBPanel
    extends AbsolutePanel
    implements MessageListener

{

    /**
     * A convenience class to wrap all the messiness of registering
     * <code>this</code> as a listener for a list of zero or more
     * <code>Message</code>.
     * @param messages A list of <code>Message</code> classes to
     * which this object listens.
     */
    public AbstractWIBPanel( Class... messages )

    {

	super();
	MessageManager.registerAsListener( this, messages );

    }

    public HandlerRegistration addMouseOverHandler( MouseOverHandler h )

    {

	return super.addDomHandler( h, MouseOverEvent.getType() );

    }

    public HandlerRegistration addMouseOutHandler( MouseOutHandler h )

    {

	return super.addDomHandler( h, MouseOutEvent.getType() );

    }

    public HandlerRegistration addDoubleClickHandler( DoubleClickHandler h )

    {

	return super.addDomHandler( h, DoubleClickEvent.getType() );

    }

    public HandlerRegistration addMouseDownHandler( MouseDownHandler h )

    {

	return super.addDomHandler( h, MouseDownEvent.getType() );

    }

    public HandlerRegistration addMouseMoveHandler( MouseMoveHandler h )

    {

	return super.addDomHandler( h, MouseMoveEvent.getType() );

    }

    public HandlerRegistration addMouseUpHandler( MouseUpHandler h )

    {

	return super.addDomHandler( h, MouseUpEvent.getType() );

    }

    public HandlerRegistration addMouseWheelHandler( MouseWheelHandler h )

    {

	return super.addDomHandler( h, MouseWheelEvent.getType() );

    }

    public HandlerRegistration addTouchStartHandler( TouchStartHandler h )

    {

	return super.addDomHandler( h, TouchStartEvent.getType() );

    }

    public HandlerRegistration addTouchMoveHandler( TouchMoveHandler h )

    {

	return super.addDomHandler( h, TouchMoveEvent.getType() );

    }

    public HandlerRegistration addTouchEndHandler( TouchEndHandler h )

    {

	return super.addDomHandler( h, TouchEndEvent.getType() );

    }

}
