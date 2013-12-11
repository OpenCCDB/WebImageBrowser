package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.resources.icons;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author spl
 */
public interface SelectAnnotationWidgetClientBundle
    extends ClientBundle

{

    // Object
    public ImageResource search();
    public ImageResource id();
    public ImageResource first_object();
    public ImageResource last_object();
    public ImageResource delete();

    // Plane
    public ImageResource first_plane();
    public ImageResource previous_plane();
    public ImageResource next_plane();
    public ImageResource last_plane();

}
