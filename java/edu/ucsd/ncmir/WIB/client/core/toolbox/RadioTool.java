package edu.ucsd.ncmir.WIB.client.core.toolbox;

import com.google.gwt.user.client.ui.Image;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.core.messages.SetInteractionFactoryMessage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author spl
 */
public class RadioTool
    extends AbstractTool

{

    private static HashMap<String,ArrayList<RadioTool>> _radio_map =
	new HashMap<String,ArrayList<RadioTool>>();

    private final AbstractInteractionMessageFactory _imf;

    private ArrayList<RadioTool> _rtlist;

    public RadioTool( Image icon,
		      String tooltip,
		      String group_name,
		      AbstractInteractionMessageFactory imf )

    {

	super( icon, tooltip );
	this._imf = imf;

	MessageManager.registerListener( imf.activateMessage().getClass(), 
					 new IMFMessageListener( this ) );

	this._rtlist = RadioTool._radio_map.get( group_name );

	if ( this._rtlist == null )
	    RadioTool._radio_map.put( group_name,
				      this._rtlist =
				      new ArrayList<RadioTool>() );

	this._rtlist.add( this );

    }

    private static class IMFMessageListener
	implements MessageListener

    {

	private final RadioTool _radio_tool;

	IMFMessageListener( RadioTool  radio_tool )

	{

	    this._radio_tool = radio_tool;

	}

        @Override
        public void action( Message m, Object o )

        {

	    this._radio_tool.select();

        }

    }

    @Override
    protected void onClick()

    {

	new SetInteractionFactoryMessage().send( this._imf );

    }

    private void select()

    {

	for ( RadioTool rt : this._rtlist )
	    if ( rt.isDown() && ( rt != this ) ) {

		rt.getElement().getStyle().setBorderColor( "transparent" );
		
		rt.setDown( false );
		break;

	    }

        this.setDown( true );	// Apparently when clicked, this isn't set.
	this.getElement().getStyle().setBorderColor( "#000000" );

    }

}
