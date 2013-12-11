package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractElementSpinner;
import edu.ucsd.ncmir.WIB.client.core.components.AbstractSpinnerElement;
import edu.ucsd.ncmir.WIB.client.core.components.SpinnerDataInterface;
import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.core.message.MessageListener;
import edu.ucsd.ncmir.WIB.client.core.message.MessageManager;
import edu.ucsd.ncmir.WIB.client.plugins.INCFPlugin.messages.AnnotationUpdateCompleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AddAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.AnnotationUpdatedMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteAnnotationMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.DeleteContourMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectAnnotationByIDMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.ThreeDGeometryDeleteMessage;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.UpdateAnnotationPlanesMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author spl
 */
public class SelectAnnotationSpinner
    extends AbstractElementSpinner
    implements MessageListener,
               SpinnerDataInterface

{

    public SelectAnnotationSpinner()

    {

	super();

        super.setSpinnerDataInterface( this );
        super.setWidth( "250px" );

	MessageManager.registerAsListener( this,
					   AddAnnotationMessage.class,
                                           AnnotationUpdatedMessage.class,
                                           AnnotationUpdateCompleteMessage.class,
					   DeleteAnnotationMessage.class,
					   DeleteContourMessage.class,
					   SelectAnnotationByIDMessage.class,
					   UpdateAnnotationPlanesMessage.class );

    }

    @Override
    public void action( Message m, Object o )

    {

	if ( m instanceof AddAnnotationMessage ) {

	    int item = super.getCurrentIndex();

	    if ( o instanceof SLASHAnnotationSpinnerInfo[] )
		for ( SLASHAnnotationSpinnerInfo sasi :
			  ( SLASHAnnotationSpinnerInfo[] ) o )
		    this.insert( sasi );
	    else if ( o instanceof SLASHAnnotationSpinnerInfo )
		this.insert( ( SLASHAnnotationSpinnerInfo ) o );

	    if ( item < 0 )
		item = 0;
	    super.setCurrentIndex( item );

        } else if ( m instanceof DeleteAnnotationMessage ) {

	    int id = ( ( Integer ) o ).intValue();

	    Iterator<AbstractSpinnerElement> iase = super.getSpinnerElements();

	    if ( iase != null )
		while ( iase.hasNext() ) {

		    SpinnerElement se = ( SpinnerElement ) iase.next();
		    SLASHAnnotationSpinnerInfo sasi = se.getObjectInfo();

		    if ( sasi.getAnnotationID() == id ) {

			super.removeSpinnerElement( se );
			new ThreeDGeometryDeleteMessage().send( sasi );
			break;

		    }

		}

	} else if ( m instanceof SelectAnnotationByIDMessage )
	    super.setCurrentIndex( this.findElementByID( o ) );
	else if ( m instanceof UpdateAnnotationPlanesMessage )
	    this.updateByID( o );
	else if ( ( m instanceof DeleteContourMessage ) ||
                  ( m instanceof AnnotationUpdatedMessage ) )
            this.update();
	else if ( m instanceof AnnotationUpdateCompleteMessage )
            this.updateByID( o );

    }

    private void updateByID( Object o )

    {

	this.updateByID( ( ( Integer ) o ).intValue() );

    }

    private void updateByID( int id )

    {

	int element_index = this.findElementByID( id );

	SpinnerElement se = ( SpinnerElement ) this._list.get( element_index );
	SLASHAnnotationSpinnerInfo sasi = se.getObjectInfo();

	sasi.update();

    }

    private int findElementByID( Object o )

    {

	return this.findElementByID( ( ( Integer ) o ).intValue() );

    }

    private int findElementByID( int id )

    {

	int element_index = -1;
	for ( int i = 0; i < this._list.size(); i++ ) {

	    SpinnerElement se = ( SpinnerElement ) this._list.get( i );

	    if ( se.getObjectInfo().getAnnotationID() == id ) {

		element_index = i;
		break;

	    }

	}
	return element_index;

    }

    private final ArrayList<AbstractSpinnerElement> _list =
	new ArrayList<AbstractSpinnerElement>();
    private final HashMap<Integer,SpinnerElement> _table =
	new HashMap<Integer,SpinnerElement>();

    private void insert( SLASHAnnotationSpinnerInfo sasi )

    {

	if ( !this._table.containsKey( sasi.getAnnotationID() ) ) {

	    SpinnerElement se = new SpinnerElement( sasi );

	    this._list.add( se );
	    this._table.put( sasi.getAnnotationID(), se );

	}

    }

    private void update()

    {

        SpinnerElement se = ( SpinnerElement ) super.getCurrentItem();

	if ( se != null )
	    se.getObjectInfo().update();

    }

    @Override
    public AbstractSpinnerElement getElement( int index )

    {

	AbstractSpinnerElement ase = null;

	if ( index < this._list.size() )
	    ase = this._list.get( index );

	return ase;

    }

    @Override
    public int deleteElement( AbstractSpinnerElement element )

    {

	this._list.remove( element );

	int id =
	    ( ( SpinnerElement ) element ).getObjectInfo().getAnnotationID();
	this._table.remove( id );

	return this._list.size();

    }

    @Override
    public int size()

    {

	return this._list.size();

    }

    @Override
    public Iterator<AbstractSpinnerElement> getIterator()

    {

	return this._list.iterator();

    }

    Iterable<AbstractSpinnerElement> getSpinnerElementList()

    {

        return this._list;

    }

}
