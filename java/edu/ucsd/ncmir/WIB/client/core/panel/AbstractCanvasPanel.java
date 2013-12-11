package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

/**
 * Abstract representation of a panel containing a <code>Canvas</code> object.
 * @author spl
 */
public abstract class AbstractCanvasPanel
    extends AbstractWIBPanel
    implements ResizeHandler

{

    private final Canvas _canvas;

    /**
     * Creates the <code>AbstractCanvasPanel</code> and adds zero or more <code>Message</code>s
     * @param listeners
     */
    public AbstractCanvasPanel( Class... listeners )

    {

        super( listeners );

	Window.addResizeHandler( this );
	this._canvas = Canvas.createIfSupported();
	this.add( this._canvas );

    }

    /**
     * @return the internal drawing canvas's Context2d.
     */
    protected Context2d getContext2d()

    {

        return this._canvas.getContext2d();

    }

    @Override
    public void setVisible( boolean visible )

    {

        super.setVisible( visible );

        if ( visible )
            this.updateCanvasDimensions();

    }

    @Override
    public void onResize( ResizeEvent event )

    {

	this.updateCanvasDimensions();
	this.redraw();

    }

    /**
     * Called on a resize to redraw the contents of the panel.
     */

    protected abstract void redraw();

    private void updateCanvasDimensions()

    {

	this._canvas.setCoordinateSpaceWidth( this.getOffsetWidth() );
	this._canvas.setCoordinateSpaceHeight( this.getOffsetHeight() );

    }

}
