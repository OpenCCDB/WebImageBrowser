package edu.ucsd.ncmir.WIB.SLASHWorker.client;

import com.google.gwt.core.client.JsArray;

/**
 *
 * @author spl
 */
public class SLASHWorkerPointDataList 
    extends JsArray<SLASHWorkerPointData>

{

    protected SLASHWorkerPointDataList() {}

    public static native SLASHWorkerPointDataList create()
	/*-{
	  return [];
	  }-*/;
      
}