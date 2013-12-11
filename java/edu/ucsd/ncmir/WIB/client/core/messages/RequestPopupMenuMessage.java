package edu.ucsd.ncmir.WIB.client.core.messages;

import edu.ucsd.ncmir.WIB.client.core.components.menu.WIBMenuBar;
import edu.ucsd.ncmir.WIB.client.core.drawable.Point;
import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class RequestPopupMenuMessage
    extends Message

{

    private final WIBMenuBar _popup;
    private final Point _point;

    public RequestPopupMenuMessage( WIBMenuBar popup, Point point )

    {

	this._popup = popup;
	this._point = point;

    }

    /**
     * @return the popup
     */

    public WIBMenuBar getPopup()

    {

        return this._popup;

    }

    /**
     * @return the point
     */

    public Point getPoint()

    {

        return this._point;

    }

}
