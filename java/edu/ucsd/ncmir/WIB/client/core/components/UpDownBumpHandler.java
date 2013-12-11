package edu.ucsd.ncmir.WIB.client.core.components;

/**
 *
 * @author spl
 */
public interface UpDownBumpHandler

{

    /**
     * Called to bump the handler's value.
     * @param bumpvalue +1 indicates a positive bump, -1 indicates a negative one.
     */
    public void bump( int bumpvalue );

}
