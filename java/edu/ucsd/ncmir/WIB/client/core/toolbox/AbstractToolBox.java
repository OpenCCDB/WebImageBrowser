package edu.ucsd.ncmir.WIB.client.core.toolbox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWT;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractDialogBox;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.InteractionPanelInitializedMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToggleToolboxStateMessage;
import edu.ucsd.ncmir.WIB.client.core.messages.ToolboxStateMessage;
import edu.ucsd.ncmir.WIB.client.core.panel.InteractionPanel;

/**
 *
 * @author spl
 */
abstract public class AbstractToolBox
    extends AbstractDialogBox

{

    private int _column = 0;
    private final Grid _widget_grid;
    private final Grid _tool_grid;

    /**
     * Creates an instance of an <code>AbstractToolBox</code>.
     * @param columns The number of columns in this
     * <code>AbstractToolBox</code>.
     * @param messages A list of <code>Message</code> classes to
     * which this object listens.
     */
    protected AbstractToolBox( int columns,
			       boolean initial_state,
			       Class... messages )

    {

	super( false, false, true, true, false );

	MessageManager.
	    registerListener( InteractionPanelInitializedMessage.class,
			      new IPIMListener( this,
						initial_state ) );

	this._tool_grid = new Grid( 1, columns );
	this._tool_grid.setCellSpacing( 0 );
	this._tool_grid.setCellPadding( 0 );
        this._widget_grid = new Grid( 2, 1 );
	this._widget_grid.setWidget( 0, 0, this._tool_grid );
        this.add( this._widget_grid );

	super.setPopupPosition( 5, 55 );

    }

    @Override
    protected void onCloseClick( ClickEvent event )

    {

	new ToggleToolboxStateMessage().send();
	super.onCloseClick( event );

    }

    protected void setControlWidget( Widget w )

    {

	this._widget_grid.setWidget( 1, 0, w );

    }

    private static class IPIMListener
	implements MessageListener

    {

	private final AbstractToolBox _tool_box;
	private final boolean _initial_state;

	IPIMListener( AbstractToolBox tool_box, boolean initial_state )

	{

	    this._tool_box = tool_box;
	    this._initial_state = initial_state;

	}

        @Override
	public void action( Message m, Object o )

	{

	    InteractionPanel interaction_panel = ( InteractionPanel ) o;

            if ( MGWT.getOsDetection().isDesktop() ) {

                interaction_panel.add( this._tool_box );
                this._tool_box.hide();
                this._tool_box.setVisible( false );
                new ToolboxStateMessage().send( this._initial_state );
            
            }

	}

    }

    /**
     * Creates a &quot;toggle&quot; button.
     * @param icon An &quot;intuitive&quot; glyph describing the function.
     * @param tooltip A one line description of the action to take place.
     * @param toggle_message A <code>Message</code> to be fired upon
     * state change.  The <code>Object</code> component will be a
     * <code>Boolean</code> value indicating the current state.
     * @return The <code>AbstractTool</code> created.
     */
    protected AbstractTool addToggleTool( Image icon,
					  String tooltip,
					  Message toggle_message )

    {

        ToggleTool toggle_tool =
	    new ToggleTool( icon, tooltip, toggle_message );

	this.addTool( toggle_tool );

	return toggle_tool;

    }

    /**
     * Creates a &quot;radio&quot; button.
     * @param icon An &quot;intuitive&quot; glyph describing the function.
     * @param tooltip A one line description of the action to take place.
     * @param group_name The name of this &quot;radio&quot; group.
     * @param imf A factory producing messages based upon mouse action.
     * @return The <code>AbstractTool</code> created.
     */
    protected AbstractTool addRadioTool( Image icon, String tooltip,
					 String group_name,
					 AbstractInteractionMessageFactory imf )

    {

        RadioTool radio_tool = new RadioTool( icon, tooltip, group_name, imf );

	this.addTool( radio_tool );

	return radio_tool;

    }

    /**
     * Creates a regular single shot button.
     * @param icon An &quot;intuitive&quot; glyph describing the function.
     * @param tooltip A one line description of the action to take place.
     * @param action_message <code>Message</code> to be sent when selected.
     * @return The <code>AbstractTool</code> created.
     */
    protected AbstractTool addActionTool( Image icon, String tooltip,
					  Message action_message )

    {

        ActionTool action_tool =
	    new ActionTool( icon, tooltip, action_message );

	this.addTool( action_tool );

	return action_tool;

    }

    /**
     * Completes a row with empty cells.
     */
    protected void addSeparator()

    {

	int row = this._tool_grid.getRowCount() - 1;

	for ( int i = this._column; i < this._tool_grid.getColumnCount(); i++ )
	    this._tool_grid.clearCell( row, i );

	this.newRow();

    }

    private void newRow()

    {

	this._column = 0;
	this._tool_grid.resizeRows( this._tool_grid.getRowCount() + 1 );

    }

    private void addTool( AbstractTool tool )

    {

	if ( this._column == this._tool_grid.getColumnCount() )
	    this.newRow();
	this._tool_grid.setWidget( this._tool_grid.getRowCount() - 1,
				   this._column,
				   tool );
	this._column++;

    }

}
