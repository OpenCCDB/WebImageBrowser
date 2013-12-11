package edu.ucsd.ncmir.WIB.DefaultWorker.client;

import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author spl
 */
public class CanvasData
    extends JavaScriptObject

{

    protected CanvasData() {}

    public static native CanvasData create( boolean red_on,
                                            boolean green_on,
                                            boolean blue_on,
					    double A,
					    double B,
					    int flip_m,
					    int flip_b,
                                            int red_map,
                                            int green_map,
                                            int blue_map,
                                            CanvasPixelArray cpa,
                                            ImageData id )
    /*-{
      return { red_on: red_on,
               green_on: green_on,
               blue_on: blue_on,
	       A: A,
	       B: B,
	       flip_m: flip_m,
	       flip_b: flip_b,
               red_map: red_map,
               green_map: green_map,
               blue_map: blue_map,
               cpa: cpa,
               id: id,
	       index: 0 };
	 }-*/;

    public final native void setIndex( int index )
    /*-{
      this.index = index;
      }-*/;

    public final native int getIndex()
    /*-{
      return this.index;
      }-*/;

    public final native boolean getRedOn()
    /*-{
      return this.red_on;
      }-*/;

    public final native boolean getGreenOn()
    /*-{
      return this.green_on;
      }-*/;

    public final native boolean getBlueOn()
    /*-{
      return this.blue_on;
      }-*/;

    public final native double getA()
    /*-{
      return this.A;
      }-*/;

    public final native double getB()
    /*-{
      return this.B;
      }-*/;

    public final native int getRedMap()
    /*-{
      return this.red_map;
      }-*/;

    public final native int getGreenMap()
    /*-{
      return this.green_map;
      }-*/;

    public final native int getBlueMap()
    /*-{
      return this.blue_map;
      }-*/;

    public final native CanvasPixelArray getCPA()
    /*-{
      return this.cpa;
      }-*/;

    public final native ImageData getImageData()
    /*-{
      return this.id;
      }-*/;

    public final native int getFlipM()
    /*-{
      return this.flip_m;
      }-*/;

    public final native int getFlipB()
    /*-{
      return this.flip_b;
      }-*/;

}
