package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import java.util.ArrayList;

/**
 *
 * @author spl
 */
public class OracleItemManager
    extends ArrayList<String>

{

    private final ItemResponder _item_responder;

    OracleItemManager( ItemResponder item_responder )

    {

        this._item_responder = item_responder;

    }

    public void complete( Request request, Callback callback )

    {

        this._item_responder.complete( this, request, callback );

    }

}
