package edu.ucsd.ncmir.WIB.client.plugins;

/**
 * A mode handler for a plugin.  A plugin may have zero or more modes
 * in which it needs to operate.
 * @author spl
 */
public interface ModeHandlerInterface

{

    /**
     * Returns a URI acceptable to Loader.pl.
     * @return A URI string.
     */
    abstract public String getURI();

    /**
     * Called by the Plugin to perform any necessary initialization.
     */
    abstract public void start();

}
