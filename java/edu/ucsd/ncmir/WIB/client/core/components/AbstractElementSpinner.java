package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.dom.client.Style;
import java.util.Iterator;

/**
 *
 * @author spl
 */
public class AbstractElementSpinner
    extends AbstractSpinner

{

    private AbstractSpinnerElement _current_element = null;
    private SpinnerDataInterface _spinner_data_interface = null;

    protected AbstractElementSpinner()

    {

	super();

    }

    protected void setSpinnerDataInterface( SpinnerDataInterface sdi )

    {

        this._spinner_data_interface = sdi;
	this.showCurrentSelection();

    }

    private int _width = 200;

    @Override
    public void setWidth( String w )

    {

	this._width = Integer.parseInt( w.replaceAll( "[^0-9]+", "" ) );
	this._width -= 6;
        super.setWidth( this._width + "px" );

    }

    private int _current = 0;

    protected final void removeSpinnerElement( AbstractSpinnerElement se )

    {

        this._spinner_data_interface.deleteElement( se );
        this.showCurrentSelection();

    }

    private void showCurrentSelection()

    {

	if ( this._current_element != null )
	    this.remove( this._current_element );
	if ( this._current >= this._spinner_data_interface.size() )
	    this._current = this._spinner_data_interface.size() - 1;

	if ( this._current >= 0 )
	    this._current_element =
                this._spinner_data_interface.getElement( this._current );
	else
	    this._current_element = new Nothing();

	int padding =
	    Integer.parseInt( this.getElement().
			      getStyle().
			      getPadding().
			      replaceAll( "[^0-9]+", "" ) );

        int slop = padding * 2;

	this.add( this._current_element );
	this.setWidgetLeftWidth( this._current_element,
				 this.getW() + 5,
				 Style.Unit.PX,
				 this._width - ( this.getW() + 5 + slop ),
				 Style.Unit.PX );
	this.setWidgetTopHeight( this._current_element,
				 0, Style.Unit.PX,
				 this.getH(),
				 Style.Unit.PX );

	this._current_element.selected( this._current );
	this.setHeight( this.getH() + "px" );

    }

    public final void setCurrentIndex( int item )

    {

	this._current = item;
	this.showCurrentSelection();

    }

    public final Iterator<AbstractSpinnerElement> getSpinnerElements()

    {

        return this._spinner_data_interface.getIterator();

    }

    public final void selectFirstItem()

    {

	this._current = 0;
	this.showCurrentSelection();

    }

    public final void selectLastItem()

    {

	this._current = this._spinner_data_interface.size() - 1;
	this.showCurrentSelection();

    }

    public final AbstractSpinnerElement getCurrentItem()

    {

	AbstractSpinnerElement spinner_element = null;

	if ( this._current != -1 )
	    spinner_element =
                this._spinner_data_interface.getElement( this._current );

	return spinner_element;

    }

    public final int getCurrentIndex()

    {

        return this._current;

    }

    @Override
    public void bump( int dir )

    {

	this._current += dir;

	if ( this._current < 0 )
	    this._current = this._spinner_data_interface.size() - 1;
	else if ( this._current == this._spinner_data_interface.size() )
	    this._current = 0;

	this.showCurrentSelection();

    }

    private static class Nothing
        extends AbstractSpinnerElement

    {

        public Nothing()

        {

	    super( "" );

        }

        @Override
        protected void selected( int current )

        {

	    // Does nothing.

        }

        @Override
        public boolean equals( Object o )

        {

            boolean equals = false;
            if ( o instanceof Nothing )
                equals = false;
	    return equals;

        }

        @Override
        public int hashCode()

        {

	    return 42;

        }

    }

}