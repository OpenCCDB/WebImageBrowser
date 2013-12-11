package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin;

import edu.ucsd.ncmir.WIB.client.core.components.AbstractSpinnerElement;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages.SelectAnnotationMessage;

/**
 *
 * @author spl
 */

class SpinnerElement
    extends AbstractSpinnerElement

{

    private final SLASHAnnotationSpinnerInfo _object_info;

    SpinnerElement( SLASHAnnotationSpinnerInfo object_info )

    {

        super( object_info.getName() );
        this._object_info = object_info;

    }

    public SLASHAnnotationSpinnerInfo getObjectInfo()

    {

        return this._object_info;

    }

    @Override
    protected void selected( int current )

    {

        this._object_info.update();
        new SelectAnnotationMessage().send( current );

    }

    @Override
    public boolean equals( Object o )

    {

        boolean equals = false;

        if ( ( o != null ) && ( o instanceof SpinnerElement ) ) {

            SpinnerElement snp = ( SpinnerElement ) o;
            equals = snp._object_info.equals( this._object_info );

        }

        return equals;

    }

    @Override
    public int hashCode()

    {

        return this._object_info.hashCode();

    }

}
