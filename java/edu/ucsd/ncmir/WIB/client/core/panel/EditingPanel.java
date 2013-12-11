package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style.Cursor;
import edu.ucsd.ncmir.WIB.client.core.TraceFactory;
import edu.ucsd.ncmir.WIB.client.core.drawable.Drawable;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.messages.ContourDataMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.EditCompleteMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.EditMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.EditSetupMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetCursorMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetEditColorMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.SetTraceFactoryMessage;

/**
 * This is the <code>EditingPanel</code> wherein all interactive
 * editing operations take place.
 * @author spl
 */

public class EditingPanel
    extends AbstractCanvasPanel

{

    private CssColor _rgb = CssColor.make( "#000000" );
    private final Context2d _c2d;

    public EditingPanel()

    {

	super( EditSetupMessage.class,
               EditMessage.class,
               EditCompleteMessage.class,
               SetEditColorMessage.class);

	this._c2d = this.getContext2d();

	this.setVisible( false );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof EditMessage )
	    this.edit( ( Point ) o );
	else if ( m instanceof EditSetupMessage )
	    this.editSetup( ( Point ) o );
	else if ( m instanceof EditCompleteMessage )
	    this.editComplete();
	else if ( m instanceof SetEditColorMessage )
	    this.setEditColor( ( SetEditColorMessage ) m );
	else if ( m instanceof SetTraceFactoryMessage )
	    this._trace_factory = ( TraceFactory ) o;

    }

    private void setEditColor( SetEditColorMessage ecm )

    {

	this._rgb = CssColor.make( "rgb( " +
				   ecm.getRed() + ", " +
				   ecm.getGreen() + ", " +
				   ecm.getBlue() + ")" );

    }

    private Drawable _points;
    private TraceFactory _trace_factory;

    private void editSetup( Point xy )

    {

        this.setVisible( true );
	new SetCursorMessage().send( Cursor.DEFAULT );
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

    private void edit( Point xy )

    {

	this._points.add( xy );

	this._c2d.lineTo( xy.getPointerX() + 0.5, xy.getPointerY() + 0.5 );

        this._c2d.stroke();

    }

    private void editComplete()

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
