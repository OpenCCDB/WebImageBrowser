package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.dialogs;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import edu.ucsd.ncmir.WIB.client.core.OracleHandler;
import edu.ucsd.ncmir.WIB.client.core.OracleItemManager;
import edu.ucsd.ncmir.WIB.client.core.SuggestionRequestHandler;
import edu.ucsd.ncmir.WIB.client.core.request.AbstractXMLRequestCallback;
import edu.ucsd.ncmir.WIB.client.core.request.HTTPRequest;

/**
 *
 * @author spl
 */
class AnnotationOracle
    extends OracleHandler
    implements SuggestionRequestHandler

{

    AnnotationOracle()

    {

	super();

	super.setSuggestionRequestHandler( this );

    }

    @Override
    public void handleSuggestionRequest( Request request,
					 Callback callback,
					 OracleItemManager item_list )

    {

	String t = request.getQuery().toLowerCase();

	String url =
	    "cgi-bin/db.pl?" +
	    "querytype=autocomplete" +
	    "&class=Entity" +
	    "&c=" + t;

	AnnotationOracleResponder responder =
	    new AnnotationOracleResponder( request, callback, item_list );

	HTTPRequest.get( url, responder );

    }

    private static class AnnotationOracleResponder
	extends AbstractXMLRequestCallback

    {

	private final SuggestOracle.Request _request;
	private final SuggestOracle.Callback _callback;
	private final OracleItemManager _item_manager;

	AnnotationOracleResponder( SuggestOracle.Request request,
				   SuggestOracle.Callback callback,
				   OracleItemManager item_manager )

	{

	    this._request = request;
	    this._callback = callback;
	    this._item_manager = item_manager;

	}

	@Override
	public void handleXMLResponse( Element autocomplete )

	{

	    for ( Node n = autocomplete.getFirstChild();
		  n != null;
		  n = n.getNextSibling() )
		if ( n.getNodeType() == Node.ELEMENT_NODE ) {

		    Element e = ( Element ) n;

		    this._item_manager.add( e.getAttribute( "value" ) );

		}

	    this._item_manager.complete( this._request, this._callback );

	}

    }

}
