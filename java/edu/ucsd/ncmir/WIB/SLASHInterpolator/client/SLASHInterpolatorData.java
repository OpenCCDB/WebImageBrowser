package edu.ucsd.ncmir.WIB.SLASHInterpolator.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

/**
 *
 * @author spl
 */
public class SLASHInterpolatorData
    extends JavaScriptObject

{

    protected SLASHInterpolatorData() {}

    public static native SLASHInterpolatorData create( int annotation_id,
						       String user_name )
    /*-{

      return { 
      annotation_id: annotation_id, 
      user_name: user_name,
      data: [] };

      }-*/;

    public native final int getAnnotationID()
    /*-{

      return this.annotation_id;

      }-*/;

    public native final String getUserName()
    /*-{

      return this.user_name;

      }-*/;

    public native final void addContour( int z,
					 double cx, double cy,
					 JsArrayNumber x,
					 JsArrayNumber y )
    /*-{

      this.data[this.data.length] = { cx: cx, cy: cy, x: x, y: y, z: z };

      }-*/;

    public native final int length()
    /*-{

      return this.data.length;

      }-*/;

    public native final double getCentroidX( int i )
    /*-{

      return this.data[i].cx;

      }-*/;

    public native final double getCentroidY( int i )
    /*-{

      return this.data[i].cy;

      }-*/;

    public native final JsArrayNumber getX( int i )
    /*-{

      return this.data[i].x;

      }-*/;

    public native final JsArrayNumber getY( int i )
    /*-{

      return this.data[i].y;

      }-*/;

    public native final int getZ( int i )
    /*-{

      return this.data[i].z;

      }-*/;

}

