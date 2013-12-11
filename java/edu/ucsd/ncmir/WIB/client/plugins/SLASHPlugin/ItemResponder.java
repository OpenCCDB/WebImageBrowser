package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;

/**
 *
 * @author spl
 */
interface ItemResponder
{

    public void complete( ItemList item_list,
                          Request request,
                          Callback callback );

}
