package edu.ucsd.ncmir.WIB.SLASHWorker.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

/**
 *
 * @author spl
 */
public class SLASHWorkerAnnotation
    extends JavaScriptObject

{

    protected SLASHWorkerAnnotation() {}

    public static native SLASHWorkerAnnotation create( int annotation_id )
    /*-{
      return {
      annotation_id: annotation_id,
      x: null,
      y: null,
      geometry_id: -1,
      closed: false,
      object_name: null,
      user_name: null,
      uri: null,
      model_id: 0,
      rgb: 0,
      app_data: null,
      x0: -1,
      y0: -1,
      x1: -1,
      y1: -1
      }
      }-*/;


    public native final int getAnnotationID()
    /*-{
      return this.annotation_id;
      }-*/;

    public native final void setPoints( JsArrayNumber x, JsArrayNumber y )
    /*-{
      this.x = x;
      this.y = y;
      }-*/;

    public native final JsArrayNumber getX()
    /*-{
      return this.x;
      }-*/;

    public native final JsArrayNumber getY()
    /*-{
      return this.y;
      }-*/;

    public native final void setGeometryID( int geometry_id )
    /*-{
      this.geometry_id = geometry_id;
      }-*/;

    public native final int getGeometryID()
    /*-{
      return this.geometry_id;
      }-*/;

    public native final void closed()
    /*-{
      this.closed = true;
      }-*/;

    public native final boolean isClosed()
    /*-{
      return this.closed;
      }-*/;

    public native final void setObjectName( String object_name )
    /*-{
      this.object_name = object_name;
      }-*/;

    public native final String getObjectName()
    /*-{
      return this.object_name;
      }-*/;

    public native final void setUserName( String user_name )
    /*-{
      this.user_name = user_name;
      }-*/;

    public native final String getUserName()
    /*-{
      return this.user_name;
      }-*/;

    public native final void setURI( String uri )
    /*-{
      this.uri = uri;
      }-*/;

    public native final String getURI()
    /*-{
      return this.uri;
      }-*/;

    public native final void setModelID( int model_id )
    /*-{
      this.model_id = model_id;
      }-*/;

    public native final int getModelID()
    /*-{
      return this.model_id;
      }-*/;

    public native final void setRGB( int rgb )
    /*-{
      this.rgb = rgb;
      }-*/;

    public native final int getRGB()
    /*-{
      return this.rgb;
      }-*/;

    public native final void setAppData( String app_data )
    /*-{
      this.app_data = app_data;
      }-*/;

    public native final String getAppData()
    /*-{
      return this.app_data;
      }-*/;

    public native final void setBoundingBox( double x0, double y0,
				             double x1, double y1 )
    /*-{
      this.x0 = x0;
      this.y0 = y0;
      this.x1 = x1;
      this.y1 = y1;
      }-*/;

    public native final double getX0()
    /*-{
      return this.x0;
      }-*/;

    public native final double getY0()
    /*-{
      return this.y0;
      }-*/;

    public native final double getX1()
    /*-{
      return this.x1;
      }-*/;

    public native final double getY1()
    /*-{
      return this.y1;
      }-*/;

}
