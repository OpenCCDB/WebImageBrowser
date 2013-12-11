package edu.ucsd.ncmir.WIB.client.plugins.SLASHPlugin.messages;

import edu.ucsd.ncmir.WIB.client.core.message.Message;

/**
 *
 * @author spl
 */
public class SearchObjectMessage 
    extends Message

{
    
    private final String _name;

    public SearchObjectMessage( String name )
        
    {
        
        this._name = name;
        
    }

    private boolean _found;
    
    public boolean found()
        
    {
        
        return this._found;
        
    }

    public String getName()

    {

	return this._name;

    }

    public void setStatus( boolean found )

    {

	this._found = found;

    }

}
