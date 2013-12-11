package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;

/**
 *
 * @author spl
 */
public class KeyData

{

    private final int _key_code;
    private final boolean _ctrl_key;
    private final boolean _shift_key;

    /**
     *
     * @param event
     */
    public KeyData( KeyCodeEvent event )

    {

	this._key_code = event.getNativeKeyCode();
	this._ctrl_key = event.isControlKeyDown();
	this._shift_key = event.isShiftKeyDown();

    }

    public KeyData( KeyPressEvent event )

    {

	this._key_code = event.getCharCode();
	this._ctrl_key = event.isControlKeyDown();
	this._shift_key = event.isShiftKeyDown();

    }

    /**
     *
     * @return the key code.
     */
    public int getKeyCode()

    {

	return this._key_code;

    }

    /**
     *
     * @return true if the <code>Control</code> key is depressed.
     */
    public boolean isControlKeyDown()

    {

	return this._ctrl_key;

    }

    /**
     *
     * @return true if the <code>Shift</code> key is depressed.
     */
    public boolean isShiftKeyDown()

    {

	return this._shift_key;

    }

}
