package edu.ucsd.ncmir.WIB.client.core.components;

import java.util.Iterator;

/**
 *
 * @author spl
 */
public interface SpinnerDataInterface

{

    public AbstractSpinnerElement getElement( int index );
    public int deleteElement( AbstractSpinnerElement element );
    public int size();
    public Iterator<AbstractSpinnerElement> getIterator();

}
