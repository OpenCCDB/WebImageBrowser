package edu.ucsd.ncmir.WIB.client.core.panel;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.ClearCursorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.EnableCursorOverlayMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.MousePositionMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.UpdateCursorOverlayMessage;

/**
 *
 * @author spl
 */
public class CursorOverlayPanel
    extends AbstractCanvasPanel

{

    private final Context2d _c2d;

    public CursorOverlayPanel()

    {

        super( ClearCursorOverlayMessage.class,
	       EnableCursorOverlayMessage.class,
	       UpdateCursorOverlayMessage.class );

	this._c2d = this.getContext2d();

	this.setVisible( false );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof ClearCursorOverlayMessage )
	    this.clearCursorOverlay();
	else if ( m instanceof EnableCursorOverlayMessage )
	    this.enableCursorOverlay( ( int[][] ) o );
	else if ( m instanceof UpdateCursorOverlayMessage )
	    this.updateCursorOverlay( ( int[][] ) o );
	else if ( m instanceof MousePositionMessage )
	    this.moveCursor( ( Point ) o );

    }

    private void clearCursorOverlay()

    {

	this.setVisible( false );
	MessageManager.deregisterListener( MousePositionMessage.class, this );

    }

    private int[][] _cursor;

    private void enableCursorOverlay( int[][] cursor )

    {

	this.setCursorOverlay( cursor );

	MessageManager.registerListener( MousePositionMessage.class, this );

    }

    private void updateCursorOverlay( int[][] cursor )

    {

	this.setCursorOverlay( cursor );

	this.drawCursor( this._x, this._y );

    }

    private void setCursorOverlay( int[][] cursor )

    {

	this._cursor = cursor;

    }

    private int _x;
    private int _y;

    private void moveCursor( Point xy )

    {

	this._x = xy.getPointerX();
	this._y = xy.getPointerY();

	this.drawCursor( this._x, this._y );

    }

    private void drawCursor( int x, int y )

    {

	this.setVisible( true );

	int width = this.getOffsetWidth();
	int height = this.getOffsetHeight();

	this._c2d.save();

	this._c2d.setFillStyle( CssColor.make( "rgba( 51, 146, 160, 0.5 )" ) );

	this._c2d.clearRect( 0, 0, width, height );
	this._c2d.translate( x, y );

	this._c2d.beginPath();
	this._c2d.moveTo( this._cursor[0][0], this._cursor[0][1] );
	for ( int i = 1; i < this._cursor.length; i++ )
	    this._c2d.lineTo( this._cursor[i][0], this._cursor[i][1] );
        this._c2d.closePath();
        this._c2d.stroke();
	this._c2d.fill();

    }

    @Override
    protected void redraw()
    {

        // Does nothing.

    }

}

