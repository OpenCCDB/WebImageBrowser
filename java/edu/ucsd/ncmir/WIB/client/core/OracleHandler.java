package edu.ucsd.ncmir.WIB.client.core;

import com.google.gwt.user.client.ui.SuggestOracle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author spl
 */
public abstract class OracleHandler
    extends SuggestOracle
    implements ItemResponder

{

    private final HashMap<String,OracleItemManager> _preloads =
	new HashMap<String,OracleItemManager>();

    @Override
    public void requestSuggestions( SuggestOracle.Request request,
				    SuggestOracle.Callback callback )

    {

	String t = request.getQuery().toLowerCase();

	if ( t.length() == 1 ) {

	    if ( !this._preloads.containsKey( t ) ) {

		OracleItemManager il = new OracleItemManager( this );

		this._preloads.put( t, il );

		this._srh.handleSuggestionRequest( request, callback, il );

	    }

	} else
	    this.complete( this._preloads.get( t.substring( 0, 1 ) ),
			   request,
			   callback );

    }

    private SuggestionRequestHandler _srh;


    public void setSuggestionRequestHandler( SuggestionRequestHandler srh )

    {

        this._srh = srh;

    }

    private static class StringSuggestion
	implements SuggestOracle.Suggestion

    {

	final private String _string;

	public StringSuggestion( String string )

	{

	    this._string = string;

	}

        @Override
	public String getDisplayString()

	{

	    return this._string;

	}

        @Override
        public String getReplacementString()
        {

            return this._string;

        }

    }

    @Override
    public void complete( OracleItemManager item_list,
			  SuggestOracle.Request request,
			  SuggestOracle.Callback callback  )

    {

	if ( item_list != null ) {

	    String t = request.getQuery();

	    ArrayList<StringSuggestion> list =
		new ArrayList<StringSuggestion>();

	    for ( String s : item_list )
		if ( s.startsWith( t ) ) {

		    list.add( new StringSuggestion( s ) );
		    if ( list.size() == request.getLimit() )
			break;

		}

	    SuggestOracle.Response response =
		new SuggestOracle.Response( list );

	    callback.onSuggestionsReady( request, response );

	}

    }

}
