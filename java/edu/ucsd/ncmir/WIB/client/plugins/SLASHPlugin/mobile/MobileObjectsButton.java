package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mobile;

import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.HideAllMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteCurrentObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.BumpObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SearchObjectMessage;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.dialog.PopinDialog;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.MSearchBox;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import edu.ucsd.ncmir.WIB.client.core.components.mobile.MobileButton;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.FlashCurrentObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.HideAllButThisObjectMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.HideNoneMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.HideThisObjectMessage;

/**
 *
 * @author spl
 */
public class MobileObjectsButton
    extends MobileButton
    implements TapHandler

{
    
    public MobileObjectsButton()
        
    {
        
        super( "Objects" );
        
        super.addTapHandler( this );
        
    }

    @Override
    public void onTap( TapEvent event )
    {

	PopinDialog pd = new PopinDialog();
	pd.setHideOnBackgroundClick( true );

	Grid g = new Grid( 11, 1 );

	g.setWidget( 0, 0,  new SearchButton( pd ) );
	g.setWidget( 1, 0,  new IDButton( pd ) );
	g.setWidget( 2, 0,  new FirstObjectButton( pd ) );
	g.setWidget( 3, 0,  new PreviousObjectButton( pd ) );
	g.setWidget( 4, 0,  new NextObjectButton( pd ) );
	g.setWidget( 5, 0,  new LastObjectButton( pd ) );
	g.setWidget( 6, 0,  new DeleteObjectButton( pd ) );
	g.setWidget( 7, 0,  new HideNoneButton( pd ) );
	g.setWidget( 8, 0,  new HideAllButton( pd ) );
	g.setWidget( 9, 0,  new HideCurrentObjectButton( pd ) );
	g.setWidget( 10, 0,  new HideAllButCurrentObjectButton( pd ) );

	ScrollPanel sp = new ScrollPanel();

	sp.add( g );
        pd.add( sp );
	pd.center();

    }

    private static class SearchButton
	extends ObjectActionButton
	implements ChangeHandler

    {

	SearchButton( PopinDialog pd )

	{

	    super( "Search", pd );

	}

	private PopinDialog _pd;
	private MSearchBox _msb;

        @Override
	protected void handler()

	{

	    this._pd = new PopinDialog();
	    this._pd.setHideOnBackgroundClick( true );

	    HeaderPanel hp = new HeaderPanel();
	    this._pd.add( hp );

	    this._msb = new MSearchBox();
	    this._msb.addChangeHandler( this );
	    hp.setLeftWidget( new Label( "Search" ) );
	    hp.setCenterWidget( this._msb );
	    hp.setRightWidget( new Label( "" ) );
	    this._pd.center();

	}

        @Override
        public void onChange( ChangeEvent event )
            
        {
            
	    SearchObjectMessage som =
		new SearchObjectMessage( this._msb.getText() );

	    som.send();

	    if ( som.found() )
		this._pd.hide();
            
        }

    }

    private static class IDButton
	extends ObjectActionButton

    {

	IDButton( PopinDialog pd )

	{

	    super( "ID", pd );

	}

        @Override
	protected void handler()

	{

	    new FlashCurrentObjectMessage().send();	    

	}

    }

    private static class FirstObjectButton
	extends ObjectActionButton

    {

	FirstObjectButton( PopinDialog pd )

	{

	    super( "First Object", pd );

	}

        @Override
	protected void handler()

	{

	    new BumpObjectMessage().send( Integer.MIN_VALUE );

	}

    }

    private static class PreviousObjectButton
	extends ObjectActionButton

    {

	PreviousObjectButton( PopinDialog pd )

	{

	    super( "Previous Object", pd );

	}

        @Override
	protected void handler()

	{

	    new BumpObjectMessage().send( -1 );

	}

    }

    private static class NextObjectButton
	extends ObjectActionButton

    {

	NextObjectButton( PopinDialog pd )

	{

	    super( "Next Object", pd );

	}

        @Override
	protected void handler()

	{

	    new BumpObjectMessage().send( 1 );

	}

    }

    private static class LastObjectButton
	extends ObjectActionButton

    {

	LastObjectButton( PopinDialog pd )

	{

	    super( "Last Object", pd );

	}

        @Override
	protected void handler()

	{

	    new BumpObjectMessage().send( Integer.MAX_VALUE );

	}

    }

    private static class DeleteObjectButton
	extends ObjectActionButton

    {

	DeleteObjectButton( PopinDialog pd )

	{

	    super( "Delete Object", pd );

	}

        @Override
	protected void handler()

	{

	    new DeleteCurrentObjectMessage().send();

	}

    }

    private static class HideAllButton
	extends ObjectActionButton

    {

	HideAllButton( PopinDialog pd )

	{

	    super( "Hide All", pd );

	}

        @Override
	protected void handler()

	{

	    new HideAllMessage().send();

	}

    }

    private static class HideNoneButton
	extends ObjectActionButton

    {

	HideNoneButton( PopinDialog pd )

	{

	    super( "Hide None", pd );

	}

        @Override
	protected void handler()

	{

	    new HideNoneMessage().send();

	}

    }

    private static class HideCurrentObjectButton
	extends ObjectActionButton

    {

	HideCurrentObjectButton( PopinDialog pd )

	{

	    super( "Hide This Object", pd );

	}

        @Override
	protected void handler()

	{

	    new HideThisObjectMessage().send();

	}

    }

    private static class HideAllButCurrentObjectButton
	extends ObjectActionButton

    {

	HideAllButCurrentObjectButton( PopinDialog pd )

	{

	    super( "Hide Others", pd );

	}

        @Override
	protected void handler()

	{

	    new HideAllButThisObjectMessage().send();

	}

    }

    private static abstract class ObjectActionButton
	extends MobileButton
	implements TapHandler

    {

	private PopinDialog _pd;

	public ObjectActionButton( String label, PopinDialog pd )

	{

	    super( label );
	    super.addTapHandler( this );
	    this._pd = pd;

	}

	@Override
	public void onTap( TapEvent event )
	{
	    
	    this._pd.hide();
	    
	    this.handler();
	    
	}

	abstract protected void handler();

    }	

}
