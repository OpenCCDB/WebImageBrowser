package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;
import edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.SelectAnnotationSpinner;

/**
 *
 * @author spl
 */
public class GetSpinnerMessage
    extends Message

{
    
    private SelectAnnotationSpinner _select_annotation_spinner;
    
    public void setSpinner( SelectAnnotationSpinner spinner )
        
    {
        
        this._select_annotation_spinner = spinner;
        
    }

    public SelectAnnotationSpinner getSpinner()
        
    {
        
        return this._select_annotation_spinner;
        
    }

}
