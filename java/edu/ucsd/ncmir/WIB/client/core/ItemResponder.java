package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;

/**
 *
 * @author spl
 */
interface ItemResponder
{

    public void complete( OracleItemManager item_list,
                          Request request,
                          Callback callback );

}
