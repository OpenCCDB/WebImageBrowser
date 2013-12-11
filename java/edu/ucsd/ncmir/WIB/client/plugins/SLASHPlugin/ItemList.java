package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import java.util.ArrayList;

/**
 *
 * @author spl
 */
class ItemList
    extends ArrayList<String>

{

    private final ItemResponder _item_responder;

    ItemList( ItemResponder item_responder )

    {

        this._item_responder = item_responder;

    }

    void complete( Request request, Callback callback )

    {

        this._item_responder.complete( this, request, callback );

    }

}
