package edu.ucsd.ncmir.WIB.SLASHWorker.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author spl
 */
public class SLASHWorkerReturnData
    extends JavaScriptObject

{

    protected SLASHWorkerReturnData() {}

    public static native 
        SLASHWorkerReturnData create( String url,
                                      SLASHWorkerPointDataList data )
    /*-{
      return {
      type: 0,
      url: url,
      data: data
      }
      }-*/;

    public static native SLASHWorkerReturnData create( String status )
    /*-{
      return {
      type: 1,
      status: status
      }
      }-*/;

    public native final int getType()
    /*-{
      return this.type;
      }-*/;

    public native final String getStatus()
    /*-{
      return this.status;
      }-*/;

    public native final String getURL()
    /*-{
      return this.url;
      }-*/;

    public native final SLASHWorkerPointDataList getPointDataList()
    /*-{
      return this.data;
      }-*/;


}
