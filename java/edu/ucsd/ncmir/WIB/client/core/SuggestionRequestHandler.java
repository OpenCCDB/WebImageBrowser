package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;

/**
 *
 * @author spl
 */
public interface SuggestionRequestHandler

{

    public void handleSuggestionRequest( Request request, Callback callback, OracleItemManager il );

}
