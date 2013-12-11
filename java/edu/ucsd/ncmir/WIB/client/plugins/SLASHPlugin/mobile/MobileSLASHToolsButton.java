package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.mobile;

import com.google.gwt.user.client.ui.Grid;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.dialog.PopinDialog;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractInteractionMessageFactory;
import edu.ucsd.ncmir.WIB.client.core.components.mobile.MobileButton;
import edu.ucsd.ncmir.WIB.client.core.messages.SetInteractionFactoryMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.AddPolygonMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.AddPolylineMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.DeleteTraceMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.DrawPolygonMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.DrawPolylineMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.EditMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.InterpolateMessageFactory;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.message_factory.SLASHDragZoomMessageFactory;

/**
 *
 * @author spl
 */
public class MobileSLASHToolsButton
    extends MobileButton
    implements TapHandler

{

    /**
     *
     */
    public MobileSLASHToolsButton()

    {

        super( "Tools" );

        super.addTapHandler( this );

    }

    @Override
    public void onTap( TapEvent event )
    {

	PopinDialog pd = new PopinDialog();
	pd.setHideOnBackgroundClick( true );

	Grid g = new Grid( 8, 1 );

	g.setWidget( 0, 0,  new DragButton( pd ) );
	g.setWidget( 1, 0,  new AddClosedObjectButton( pd ) );
	g.setWidget( 2, 0,  new AddToClosedObjectButton( pd ) );
	g.setWidget( 3, 0,  new AddOpenObjectButton( pd ) );
	g.setWidget( 4, 0,  new AddToOpenObjectButton( pd ) );
	g.setWidget( 5, 0,  new InterpolateButton( pd ) );
	g.setWidget( 6, 0,  new EditObjectButton( pd ) );
	g.setWidget( 7, 0,  new DeleteObjectButton( pd ) );
	ScrollPanel sp = new ScrollPanel();
	sp.add( g );
        pd.add( sp );
	pd.center();

    }

    private static class DragButton
	extends OptionButton

    {

	DragButton( PopinDialog pd )

	{

	    super( "Drag", pd );

	}

        @Override
	protected AbstractInteractionMessageFactory getIMF()

	{

	    return new SLASHDragZoomMessageFactory();

	}

    }

    private static class AddClosedObjectButton
	extends OptionButton

    {

	AddClosedObjectButton( PopinDialog pd )

	{

	    super( "Create Closed Object", pd );

	}

        @Override
	protected AbstractInteractionMessageFactory getIMF()

	{

	    return new DrawPolygonMessageFactory();

	}

    }

    private static class AddToClosedObjectButton
	extends OptionButton

    {

	AddToClosedObjectButton( PopinDialog pd )

	{

	    super( "Add Closed Contour", pd );

	}

        @Override
	protected AbstractInteractionMessageFactory getIMF()

	{

	    return new AddPolygonMessageFactory();

	}

    }

    private static class AddOpenObjectButton
	extends OptionButton

    {

	AddOpenObjectButton( PopinDialog pd )

	{

	    super( "Create Open Object", pd );

	}

        @Override
	protected AbstractInteractionMessageFactory getIMF()

	{

	    return new DrawPolylineMessageFactory();

	}

    }

    private static class AddToOpenObjectButton
	extends OptionButton

    {

	AddToOpenObjectButton( PopinDialog pd )

	{

	    super( "Add Open Contour", pd );

	}

        @Override
	protected AbstractInteractionMessageFactory getIMF()

	{

	    return new AddPolylineMessageFactory();

	}

    }

    private static class InterpolateButton
	extends OptionButton

    {

	InterpolateButton( PopinDialog pd )

	{

	    super( "Interpolate Traces", pd );

	}

        @Override
	protected AbstractInteractionMessageFactory getIMF()

	{

	    return new InterpolateMessageFactory();

	}

    }

    private static class EditObjectButton
	extends OptionButton

    {

	EditObjectButton( PopinDialog pd )

	{

	    super( "Edit Object", pd );

	}

        @Override
	protected AbstractInteractionMessageFactory getIMF()

	{

	    return new EditMessageFactory();

	}

    }

    private static class DeleteObjectButton
	extends OptionButton

    {

	DeleteObjectButton( PopinDialog pd )

	{

	    super( "Delete Trace", pd );

	}

        @Override
	protected AbstractInteractionMessageFactory getIMF()

	{

	    return new DeleteTraceMessageFactory();

	}

    }

    private static abstract class OptionButton
	extends MobileButton
	implements TapHandler

    {

	private PopinDialog _pd;

	public OptionButton( String label, PopinDialog pd )

	{

	    super( label );
	    super.addTapHandler( this );
	    this._pd = pd;

	}

	@Override
	public void onTap( TapEvent event )
	{

	    this._pd.hide();

	    new SetInteractionFactoryMessage().send( this.getIMF() );

	}

	abstract protected AbstractInteractionMessageFactory getIMF();

    }

}
