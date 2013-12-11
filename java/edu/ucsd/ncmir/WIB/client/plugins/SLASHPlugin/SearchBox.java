package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import edu.ucsd.ncmir.WIB.client.core.OracleHandler;
import edu.ucsd.ncmir.WIB.client.core.OracleItemManager;
import edu.ucsd.ncmir.WIB.client.core.SuggestionRequestHandler;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInputDialogBox;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractSpinnerElement;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SearchObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.resources.icons.SelectAnnotationWidgetClientBundle;
import java.util.Arrays;

/**
 *
 * @author spl
 */
public class SearchBox
    extends AbstractInputDialogBox
    implements KeyPressHandler

{

    private final SelectAnnotationSpinner _spinner;
    private final SuggestBox _suggest;

    public SearchBox( SelectAnnotationSpinner spinner )

    {

	super( false, true, true, false );

	super.addTitle( "Object Search" );

	this._spinner = spinner;

	Grid g = new Grid( 2, 1 );

	g.setWidget( 0, 0, new Label( "Enter object name:" ) );
	Grid sg = new Grid( 1, 2 );
	this._suggest = new SuggestBox( new Oracle( spinner ) );
	sg.setWidget( 0, 0, this._suggest );
	this._suggest.addKeyPressHandler( this );
	SearchButton search = new SearchButton( this );
	sg.setWidget( 0, 1, search );
	g.setWidget( 1, 0, sg );

	this.add( g );

    }

    private void handleSearch()

    {

	SearchObjectMessage som =
	    new SearchObjectMessage( this._suggest.getText() );

	som.send();

	if ( som.found() )
	    this.hide();

    }

    @Override
    public void onKeyPress( KeyPressEvent event )
    {

	if (  KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode() )
	    this.handleSearch();

    }

    private static class SearchButton
        extends CustomButton
	implements ClickHandler

    {

	private final SearchBox _search_box;
	private static final SelectAnnotationWidgetClientBundle _images =
	    GWT.create( SelectAnnotationWidgetClientBundle.class );

        public SearchButton( SearchBox search_box )

        {

	    super( new Image( SearchButton._images.search() ) );
	    super.setTitle( "Search." );
	    this.addClickHandler( this );
	    this._search_box = search_box;

        }


        @Override
        public void onClick( ClickEvent event )

        {

	    this._search_box.handleSearch();

        }

    }


    private static class Oracle
	extends OracleHandler
	implements SuggestionRequestHandler

    {

	private final String[] _list;

	Oracle( SelectAnnotationSpinner spinner )

	{

	    super.setSuggestionRequestHandler( this );
	    this._list = new String[spinner.size()];

	    int i = 0;
	    for ( AbstractSpinnerElement ase : spinner.getSpinnerElementList() )
		this._list[i++] = ase.getText();
	    Arrays.sort( this._list );

	}

	@Override
	public void handleSuggestionRequest( Request request,
					     Callback callback,
					     OracleItemManager item_list )

	{

            item_list.addAll( Arrays.asList( this._list ) );

	    item_list.complete( request, callback );

	}

    }

}
