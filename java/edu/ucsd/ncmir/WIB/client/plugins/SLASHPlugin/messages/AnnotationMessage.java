package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.Annotation;

/**
 *
 * @author spl
 */
public class AnnotationMessage
    extends Message
{

    private Annotation _points;

    public void setPoints( Annotation points )

    {

        this._points = points;

    }

    public Annotation getPoints()

    {

        return this._points;

    }

    public int getAnnotationID()

    {

        return this._points.getAnnotationID();

    }

    private int _plane;

    public void setPlane( int plane )

    {

        this._plane = plane;

    }

    public int getPlane()

    {

        return this._plane;

    }

    private boolean _keep;

    public void setKeep( boolean keep )

    {

        this._keep = keep;

    }

    public boolean keep()

    {

        return this._keep;

    }

}
