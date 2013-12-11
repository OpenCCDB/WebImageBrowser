package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class SetActionButtonListenerMessage
    extends Message

{

    private final boolean _is_enable_message;
    private String _label;
    private Message _message;
    private Object _data;

    public SetActionButtonListenerMessage()

    {

	this._is_enable_message = false;

    }

    public SetActionButtonListenerMessage( String label,
					   Message message,
					   Object data )

    {

        this._is_enable_message = true;
	this._label = label;
	this._message = message;
	this._data = data;

    }

    public boolean isEnableMessage()

    {

	return this._is_enable_message;

    }

    public String getText()

    {

	return this._label;

    }

    public Message getButtonMessage()

    {

	return this._message;

    }

    public Object getData()

    {

	return this._data;

    }

}
