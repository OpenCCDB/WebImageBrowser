package edu.ucsd.ncmir.WIB.DefaultWorker.worker;

import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.webworker.client.DedicatedWorkerEntryPoint;
import com.google.gwt.webworker.client.MessageEvent;
import com.google.gwt.webworker.client.MessageHandler;
import edu.ucsd.ncmir.WIB.DefaultWorker.client.CanvasData;

public class DefaultWorker
    extends DedicatedWorkerEntryPoint
    implements MessageHandler

{

    @Override
    public void onMessage( MessageEvent event )

    {

	CanvasData canvas_data = ( CanvasData ) event.getDataAsJSO();

	boolean red_on = canvas_data.getRedOn();
	boolean green_on = canvas_data.getGreenOn();
	boolean blue_on = canvas_data.getBlueOn();

	double A = canvas_data.getA();
	double B = canvas_data.getB();

	int red_map = canvas_data.getRedMap();
	int green_map = canvas_data.getGreenMap();
	int blue_map = canvas_data.getBlueMap();

	int flip_m = canvas_data.getFlipM();
	int flip_b = canvas_data.getFlipB();

	CanvasPixelArray in_cpa = canvas_data.getCPA();
	ImageData image_data = canvas_data.getImageData();

	CanvasPixelArray out_cpa = image_data.getData();

	int[] red_lut = this.buildLUT( red_on, A, B, flip_m, flip_b );
	int[] green_lut = this.buildLUT( green_on, A, B, flip_m, flip_b );
	int[] blue_lut = this.buildLUT( blue_on, A, B, flip_m, flip_b );

	int size = in_cpa.getLength();
	for ( int ch = 0; ch < size; ch += 4 ) {

	    out_cpa.set( ch + red_map, red_lut[in_cpa.get( ch + 0 )] );
	    out_cpa.set( ch + green_map, green_lut[in_cpa.get( ch + 1 )] );
	    out_cpa.set( ch + blue_map, blue_lut[in_cpa.get( ch + 2 )] );
	    out_cpa.set( ch + 3, 255 );

	}

	this.postMessage( ( JavaScriptObject ) canvas_data );

    }


    @Override
    public void onWorkerLoad()

    {

	super.setOnMessage( this );

    }

    private int[] buildLUT( boolean on, double A, double B, int m, int b )

    {

	int[] lut = new int[256];

	if ( on )
	    for ( int i = 0; i < 256; i++ ) {

		int value = ( i * m ) + b;

		value = ( int ) ( ( value * A ) + B );
		if ( value < 0 )
		    value = 0;
		else if ( value > 255 )
		    value = 255;

		lut[i] = value;

	    }

	return lut;

    }

}
