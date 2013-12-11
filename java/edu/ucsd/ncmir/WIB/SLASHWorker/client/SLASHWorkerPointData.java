package edu.ucsd.ncmir.WIB.SLASHWorker.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author spl
 */
public class SLASHWorkerPointData
    extends JavaScriptObject

{

    protected SLASHWorkerPointData() {}

    public static native SLASHWorkerPointData create( double offset,
					              SLASHWorkerAnnotation annotation )
    /*-{
      return { 
      offset: offset,
      annotation: annotation
      }
      }-*/;

    public native final int getOffset()
    /*-{
      return this.offset;
      }-*/;

    public native final SLASHWorkerAnnotation getAnnotation()
    /*-{
      return this.annotation;
      }-*/;

}
