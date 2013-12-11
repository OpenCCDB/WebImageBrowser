package edu.ucsd.ncmir.WIB.client.core.toolbox;

import com.google.gwt.user.client.ui.Image;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
class ActionTool
    extends AbstractTool 

{

    private final Message _message;

    ActionTool( Image icon, String tooltip, Message message )

    {

	super( icon, tooltip );
	this._message = message;

    }


    @Override
    protected void onClick()

    {

	this._message.send();

    }
    
}
