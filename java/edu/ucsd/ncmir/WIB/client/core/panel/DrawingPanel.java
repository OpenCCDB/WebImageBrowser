package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style.Cursor;
import edu.ucsd.ncmir.WIB.client.core.TraceFactory;
import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.ContourDataMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DisableDrawingMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DrawCompleteMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DrawMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.DrawSetupMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.EnableDrawingMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetCursorMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetDrawColorMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetTraceFactoryMessage;

/**
 * This is the <code>DrawingPanel</code> wherein all interactive
 * drawing operations take place.
 * @author spl
 */

public class DrawingPanel
    extends AbstractCanvasPanel

{

    private CssColor _rgb = CssColor.make( "#000000" );
    private final Context2d _c2d;

    public DrawingPanel()

    {

	super( DisableDrawingMessage.class,
               DrawSetupMessage.class,
               DrawMessage.class,
               DrawCompleteMessage.class,
               EnableDrawingMessage.class,
               SetTraceFactoryMessage.class,
               SetDrawColorMessage.class );

	this._c2d = this.getContext2d();

	this.setVisible( false );

    }

    private boolean _enabled = true;
    private TraceFactory _trace_factory = null;

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof EnableDrawingMessage )
	    this._enabled = true;
	else if ( m instanceof DisableDrawingMessage )
	    this._enabled = false;
	else if ( m instanceof SetDrawColorMessage )
	    this.setDrawColor( ( SetDrawColorMessage ) m );
	else if ( m instanceof SetTraceFactoryMessage )
	    this._trace_factory = ( TraceFactory ) o;
	else if ( this._enabled ) {

	    if ( m instanceof DrawMessage )
		this.draw( ( Point ) o );
	    else if ( m instanceof DrawSetupMessage )
		this.drawSetup( ( Point ) o );
	    else if ( m instanceof DrawCompleteMessage )
		this.drawComplete();

	}

    }

    private void setDrawColor( SetDrawColorMessage dcm )

    {

	this._rgb = CssColor.make( "rgb( " +
				   dcm.getRed() + ", " +
				   dcm.getGreen() + ", " +
				   dcm.getBlue() + ")" );

    }

    private Drawable _points = null;

    private void drawSetup( Point xy )

    {

	this.setVisible( true );

	new SetCursorMessage().send( Cursor.CROSSHAIR );
	this._points = this._trace_factory.create();
	this._points.add( xy );

	int width = this.getOffsetWidth();
	int height = this.getOffsetHeight();

	this._c2d.save();

	this._c2d.setLineWidth( 1.5 );
	this._c2d.setStrokeStyle( this._rgb );
	this._c2d.clearRect( 0, 0, width, height );
	this._c2d.beginPath();
	this._c2d.moveTo( xy.getPointerX() + 0.5, xy.getPointerY() + 0.5 );

    }

    private void draw( Point xy )

    {

	this._points.add( xy );

	this._c2d.lineTo( xy.getPointerX() + 0.5, xy.getPointerY() + 0.5 );

	this._c2d.stroke();

    }

    private void drawComplete()

    {

	new SetCursorMessage().send( Cursor.DEFAULT );
	this.setVisible( false );
	new ContourDataMessage().send( this._points );

    }

    @Override
    protected void redraw()
    {

        // Does nothing.

    }

}
