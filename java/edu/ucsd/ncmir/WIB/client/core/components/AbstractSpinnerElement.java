package edu.ucsd.ncmir.WIB.client.core.components;

import com.google.gwt.user.client.ui.Label;

/**
 *
 * @author spl
 */
public abstract class AbstractSpinnerElement
    extends Label

{

    /**
     * Constructs spinner element.
     * @param name The name of the element.
     */
    protected AbstractSpinnerElement( String name )

    {

        super( name );

    }

    protected abstract void selected( int current );

    @Override
    abstract public boolean equals( Object o );

    @Override
    abstract public int hashCode();

}
