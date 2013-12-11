package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mobile;

import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.BumpObjectPlaneMessage;
import com.google.gwt.user.client.ui.Grid;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.dialog.PopinDialog;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import edu.ucsd.ncmir.WIB.client.core.components.mobile.MobileButton;

/**
 *
 * @author spl
 */
public class MobilePlanesButton
    extends MobileButton
    implements TapHandler

{
    
    public MobilePlanesButton()
        
    {
        
        super( "Planes" );
        
        super.addTapHandler( this );
        
    }

    @Override
    public void onTap( TapEvent event )
    {

	PopinDialog pd = new PopinDialog();
	pd.setHideOnBackgroundClick( true );

	Grid g = new Grid( 4, 1 );

	g.setWidget( 0, 0,  new FirstPlaneButton( pd ) );
	g.setWidget( 1, 0,  new PreviousPlaneButton( pd ) );
	g.setWidget( 2, 0,  new NextPlaneButton( pd ) );
	g.setWidget( 3, 0,  new LastPlaneButton( pd ) );

	ScrollPanel sp = new ScrollPanel();

	sp.add( g );
        pd.add( sp );
	pd.center();

    }

    private static abstract class PlaneActionButton
	extends MobileButton
	implements TapHandler

    {

	private PopinDialog _pd;

	public PlaneActionButton( String label, PopinDialog pd )

	{

	    super( label );
	    super.addTapHandler( this );
	    this._pd = pd;

	}

	@Override
	public void onTap( TapEvent event )
	{
	    
	    this._pd.hide();
	    
	    new BumpObjectPlaneMessage().send( this.bump() );
	    
	}

	abstract protected int bump();

    }	

    private static class FirstPlaneButton
	extends PlaneActionButton

    {

	FirstPlaneButton( PopinDialog pd )

	{
	    
	    super( "First Object Plane", pd );

	}

        @Override
	public int bump()

	{

	    return Integer.MIN_VALUE;

	}

    }

    private static class PreviousPlaneButton
	extends PlaneActionButton

    {

	PreviousPlaneButton( PopinDialog pd )

	{
	    
	    super( "Previous Object Plane", pd );

	}

        @Override
	public int bump()

	{

	    return -1;

	}

    }

    private static class NextPlaneButton
	extends PlaneActionButton

    {

	NextPlaneButton( PopinDialog pd )

	{
	    
	    super( "Next Object Plane", pd );

	}

        @Override
	public int bump()

	{

	    return 1;

	}

    }

    private static class LastPlaneButton
	extends PlaneActionButton

    {

	LastPlaneButton( PopinDialog pd )

	{
	    
	    super( "Last Object Plane", pd );

	}

        @Override
	public int bump()

	{

	    return Integer.MAX_VALUE;

	}

    }

}