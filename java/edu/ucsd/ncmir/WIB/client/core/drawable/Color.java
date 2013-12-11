package edu.ucsd.ncmir.WIB.client.core.drawable;

import java.util.HashMap;

/**
 * A utility class to define and parse colors.
 * @author spl
 */
public class Color

{

    /**
     * Thou shalt not instantiate.
     */

    private Color() {}

    private static HashMap<String,Integer> _color =
	new HashMap<String,Integer>();

    static {

	Color._color.put( "aliceblue", Color.rgb( 240, 248, 255 ) );
	Color._color.put( "antiquewhite", Color.rgb( 250, 235, 215 ) );
	Color._color.put( "aqua", Color.rgb( 0, 255, 255 ) );
	Color._color.put( "aquamarine", Color.rgb( 127, 255, 212 ) );
	Color._color.put( "azure", Color.rgb( 240, 255, 255 ) );
	Color._color.put( "beige", Color.rgb( 245, 245, 220 ) );
	Color._color.put( "bisque", Color.rgb( 255, 228, 196 ) );
	Color._color.put( "black", Color.rgb( 0, 0, 0 ) );
	Color._color.put( "blanchedalmond", Color.rgb( 255, 235, 205 ) );
	Color._color.put( "blue", Color.rgb( 0, 0, 255 ) );
	Color._color.put( "blueviolet", Color.rgb( 138, 43, 226 ) );
	Color._color.put( "brown", Color.rgb( 165, 42, 42 ) );
	Color._color.put( "burlywood", Color.rgb( 222, 184, 135 ) );
	Color._color.put( "cadetblue", Color.rgb( 95, 158, 160 ) );
	Color._color.put( "chartreuse", Color.rgb( 127, 255, 0 ) );
	Color._color.put( "chocolate", Color.rgb( 210, 105, 30 ) );
	Color._color.put( "coral", Color.rgb( 255, 127, 80 ) );
	Color._color.put( "cornflowerblue", Color.rgb( 100, 149, 237 ) );
	Color._color.put( "cornsilk", Color.rgb( 255, 248, 220 ) );
	Color._color.put( "crimson", Color.rgb( 220, 20, 60 ) );
	Color._color.put( "cyan", Color.rgb( 0, 255, 255 ) );
	Color._color.put( "darkblue", Color.rgb( 0, 0, 139 ) );
	Color._color.put( "darkcyan", Color.rgb( 0, 139, 139 ) );
	Color._color.put( "darkgoldenrod", Color.rgb( 184, 134, 11 ) );
	Color._color.put( "darkgray", Color.rgb( 169, 169, 169 ) );
	Color._color.put( "darkgreen", Color.rgb( 0, 100, 0 ) );
	Color._color.put( "darkgrey", Color.rgb( 169, 169, 169 ) );
	Color._color.put( "darkkhaki", Color.rgb( 189, 183, 107 ) );
	Color._color.put( "darkmagenta", Color.rgb( 139, 0, 139 ) );
	Color._color.put( "darkolivegreen", Color.rgb( 85, 107, 47 ) );
	Color._color.put( "darkorange", Color.rgb( 255, 140, 0 ) );
	Color._color.put( "darkorchid", Color.rgb( 153, 50, 204 ) );
	Color._color.put( "darkred", Color.rgb( 139, 0, 0 ) );
	Color._color.put( "darksalmon", Color.rgb( 233, 150, 122 ) );
	Color._color.put( "darkseagreen", Color.rgb( 143, 188, 143 ) );
	Color._color.put( "darkslateblue", Color.rgb( 72, 61, 139 ) );
	Color._color.put( "darkslategray", Color.rgb( 47, 79, 79 ) );
	Color._color.put( "darkslategrey", Color.rgb( 47, 79, 79 ) );
	Color._color.put( "darkturquoise", Color.rgb( 0, 206, 209 ) );
	Color._color.put( "darkviolet", Color.rgb( 148, 0, 211 ) );
	Color._color.put( "deeppink", Color.rgb( 255, 20, 147 ) );
	Color._color.put( "deepskyblue", Color.rgb( 0, 191, 255 ) );
	Color._color.put( "dimgray", Color.rgb( 105, 105, 105 ) );
	Color._color.put( "dimgrey", Color.rgb( 105, 105, 105 ) );
	Color._color.put( "dodgerblue", Color.rgb( 30, 144, 255 ) );
	Color._color.put( "firebrick", Color.rgb( 178, 34, 34 ) );
	Color._color.put( "floralwhite", Color.rgb( 255, 250, 240 ) );
	Color._color.put( "forestgreen", Color.rgb( 34, 139, 34 ) );
	Color._color.put( "fuchsia", Color.rgb( 255, 0, 255 ) );
	Color._color.put( "gainsboro", Color.rgb( 220, 220, 220 ) );
	Color._color.put( "ghostwhite", Color.rgb( 248, 248, 255 ) );
	Color._color.put( "gold", Color.rgb( 255, 215, 0 ) );
	Color._color.put( "goldenrod", Color.rgb( 218, 165, 32 ) );
	Color._color.put( "gray", Color.rgb( 128, 128, 128 ) );
	Color._color.put( "grey", Color.rgb( 128, 128, 128 ) );
	Color._color.put( "green", Color.rgb( 0, 128, 0 ) );
	Color._color.put( "greenyellow", Color.rgb( 173, 255, 47 ) );
	Color._color.put( "honeydew", Color.rgb( 240, 255, 240 ) );
	Color._color.put( "hotpink", Color.rgb( 255, 105, 180 ) );
	Color._color.put( "indianred", Color.rgb( 205, 92, 92 ) );
	Color._color.put( "indigo", Color.rgb( 75, 0, 130 ) );
	Color._color.put( "ivory", Color.rgb( 255, 255, 240 ) );
	Color._color.put( "khaki", Color.rgb( 240, 230, 140 ) );
	Color._color.put( "lavender", Color.rgb( 230, 230, 250 ) );
	Color._color.put( "lavenderblush", Color.rgb( 255, 240, 245 ) );
	Color._color.put( "lawngreen", Color.rgb( 124, 252, 0 ) );
	Color._color.put( "lemonchiffon", Color.rgb( 255, 250, 205 ) );
	Color._color.put( "lightblue", Color.rgb( 173, 216, 230 ) );
	Color._color.put( "lightcoral", Color.rgb( 240, 128, 128 ) );
	Color._color.put( "lightcyan", Color.rgb( 224, 255, 255 ) );
	Color._color.put( "lightgoldenrodyellow", Color.rgb( 250, 250, 210 ) );
	Color._color.put( "lightgray", Color.rgb( 211, 211, 211 ) );
	Color._color.put( "lightgreen", Color.rgb( 144, 238, 144 ) );
	Color._color.put( "lightgrey", Color.rgb( 211, 211, 211 ) );
	Color._color.put( "lightpink", Color.rgb( 255, 182, 193 ) );
	Color._color.put( "lightsalmon", Color.rgb( 255, 160, 122 ) );
	Color._color.put( "lightseagreen", Color.rgb( 32, 178, 170 ) );
	Color._color.put( "lightskyblue", Color.rgb( 135, 206, 250 ) );
	Color._color.put( "lightslategray", Color.rgb( 119, 136, 153 ) );
	Color._color.put( "lightslategrey", Color.rgb( 119, 136, 153 ) );
	Color._color.put( "lightsteelblue", Color.rgb( 176, 196, 222 ) );
	Color._color.put( "lightyellow", Color.rgb( 255, 255, 224 ) );
	Color._color.put( "lime", Color.rgb( 0, 255, 0 ) );
	Color._color.put( "limegreen", Color.rgb( 50, 205, 50 ) );
	Color._color.put( "linen", Color.rgb( 250, 240, 230 ) );
	Color._color.put( "magenta", Color.rgb( 255, 0, 255 ) );
	Color._color.put( "maroon", Color.rgb( 128, 0, 0 ) );
	Color._color.put( "mediumaquamarine", Color.rgb( 102, 205, 170 ) );
	Color._color.put( "mediumblue", Color.rgb( 0, 0, 205 ) );
	Color._color.put( "mediumorchid", Color.rgb( 186, 85, 211 ) );
	Color._color.put( "mediumpurple", Color.rgb( 147, 112, 219 ) );
	Color._color.put( "mediumseagreen", Color.rgb( 60, 179, 113 ) );
	Color._color.put( "mediumslateblue", Color.rgb( 123, 104, 238 ) );
	Color._color.put( "mediumspringgreen", Color.rgb( 0, 250, 154 ) );
	Color._color.put( "mediumturquoise", Color.rgb( 72, 209, 204 ) );
	Color._color.put( "mediumvioletred", Color.rgb( 199, 21, 133 ) );
	Color._color.put( "midnightblue", Color.rgb( 25, 25, 112 ) );
	Color._color.put( "mintcream", Color.rgb( 245, 255, 250 ) );
	Color._color.put( "mistyrose", Color.rgb( 255, 228, 225 ) );
	Color._color.put( "moccasin", Color.rgb( 255, 228, 181 ) );
	Color._color.put( "navajowhite", Color.rgb( 255, 222, 173 ) );
	Color._color.put( "navy", Color.rgb( 0, 0, 128 ) );
	Color._color.put( "oldlace", Color.rgb( 253, 245, 230 ) );
	Color._color.put( "olive", Color.rgb( 128, 128, 0 ) );
	Color._color.put( "olivedrab", Color.rgb( 107, 142, 35 ) );
	Color._color.put( "orange", Color.rgb( 255, 165, 0 ) );
	Color._color.put( "orangered", Color.rgb( 255, 69, 0 ) );
	Color._color.put( "orchid", Color.rgb( 218, 112, 214 ) );
	Color._color.put( "palegoldenrod", Color.rgb( 238, 232, 170 ) );
	Color._color.put( "palegreen", Color.rgb( 152, 251, 152 ) );
	Color._color.put( "paleturquoise", Color.rgb( 175, 238, 238 ) );
	Color._color.put( "palevioletred", Color.rgb( 219, 112, 147 ) );
	Color._color.put( "papayawhip", Color.rgb( 255, 239, 213 ) );
	Color._color.put( "peachpuff", Color.rgb( 255, 218, 185 ) );
	Color._color.put( "peru", Color.rgb( 205, 133, 63 ) );
	Color._color.put( "pink", Color.rgb( 255, 192, 203 ) );
	Color._color.put( "plum", Color.rgb( 221, 160, 221 ) );
	Color._color.put( "powderblue", Color.rgb( 176, 224, 230 ) );
	Color._color.put( "purple", Color.rgb( 128, 0, 128 ) );
	Color._color.put( "red", Color.rgb( 255, 0, 0 ) );
	Color._color.put( "rosybrown", Color.rgb( 188, 143, 143 ) );
	Color._color.put( "royalblue", Color.rgb( 65, 105, 225 ) );
	Color._color.put( "saddlebrown", Color.rgb( 139, 69, 19 ) );
	Color._color.put( "salmon", Color.rgb( 250, 128, 114 ) );
	Color._color.put( "sandybrown", Color.rgb( 244, 164, 96 ) );
	Color._color.put( "seagreen", Color.rgb( 46, 139, 87 ) );
	Color._color.put( "seashell", Color.rgb( 255, 245, 238 ) );
	Color._color.put( "sienna", Color.rgb( 160, 82, 45 ) );
	Color._color.put( "silver", Color.rgb( 192, 192, 192 ) );
	Color._color.put( "skyblue", Color.rgb( 135, 206, 235 ) );
	Color._color.put( "slateblue", Color.rgb( 106, 90, 205 ) );
	Color._color.put( "slategray", Color.rgb( 112, 128, 144 ) );
	Color._color.put( "slategrey", Color.rgb( 112, 128, 144 ) );
	Color._color.put( "snow", Color.rgb( 255, 250, 250 ) );
	Color._color.put( "springgreen", Color.rgb( 0, 255, 127 ) );
	Color._color.put( "steelblue", Color.rgb( 70, 130, 180 ) );
	Color._color.put( "tan", Color.rgb( 210, 180, 140 ) );
	Color._color.put( "teal", Color.rgb( 0, 128, 128 ) );
	Color._color.put( "thistle", Color.rgb( 216, 191, 216 ) );
	Color._color.put( "tomato", Color.rgb( 255, 99, 71 ) );
	Color._color.put( "turquoise", Color.rgb( 64, 224, 208 ) );
	Color._color.put( "violet", Color.rgb( 238, 130, 238 ) );
	Color._color.put( "wheat", Color.rgb( 245, 222, 179 ) );
	Color._color.put( "white", Color.rgb( 255, 255, 255 ) );
	Color._color.put( "whitesmoke", Color.rgb( 245, 245, 245 ) );
	Color._color.put( "yellow", Color.rgb( 255, 255, 0 ) );
	Color._color.put( "yellowgreen", Color.rgb( 154, 205, 50 ) );

    }

    private static Integer rgb( int red, int green, int blue )

    {

	return new Integer( Color.concatenateRGB( red, green, blue ) );

    }

    /**
     * Concatenates the <i>red</i>, <i>green</i>, and <i>blue</i>
     * values into a packed integer.  Out of range values (outside
     * 0..255) are clamped to modulo 256.
     * @param red The <i>red</i> component.
     * @param green The <i>green</i> component.
     * @param blue The <i>blue</i> component.
     * @return A packed integer value of the form 0x00rrggbb.
     */
    public static int concatenateRGB( int red, int green, int blue )

    {

	return ( ( red & 0x000000ff ) << 16 ) |
	    ( ( green & 0x000000ff ) << 8 ) |
	    ( blue & 0x000000ff );

    }

    /**
     * Parses a <code>String</code> either by name, a hexadecimal
     * string, a decimal string, or an CSS rgb() triplet, returning
     * packed integer. Handles hex strings of the form
     * &quot;#rgb&quot;, &quot;#rrggbb&quot;, or
     * &quot;#rrrrggggbbbb&quot;.  16 bit values are truncated to 8
     * bits by shifting.
     * @param color The string representation.
     * @return The packed integer value of the string.
     */
    public static int parseRGB( String color )

    {

	Integer rgb = Color._color.get( color );

	return ( rgb != null ) ? rgb.intValue() : Color.parse( color );

    }

    /**
     * Parses a <code>String</code> either by name, a hexadecimal
     * string, a decimal string, or an CSS rgb() triplet, returning
     * packed integer. Handles hex strings of the form
     * &quot;#rgb&quot;, &quot;#rrggbb&quot;, or
     * &quot;#rrrrggggbbbb&quot;.  16 bit values are truncated to 8
     * bits by shifting.
     * @param color The string representation.
     * @return An array of length 3 containing, in order, the
     * <i>red</i>, <i>green</i>, and <i>blue</i> components of the
     * color.  Values are constrained to be within the range 0..255.
     */
    public static int[] parseRGBToTriplet( String color )

    {

        int rgb = Color.parseRGB( color );

        return new int[] {
            ( rgb >> 16 ) & 0xff,
            ( rgb >> 8 ) & 0xff,
            rgb & 0xff
        };

    }

    private static final String HEX = "0123456789abcdef";

    private static int hexconv( String hex, int shift )

    {

	int hexval = 0;

	for ( int i = 0; i < hex.length(); i++ )
	    hexval =
		( hexval << 4 ) | HEX.indexOf( hex.substring( i, i + 1 ) );

	return hexval << shift;

    }

    private static int parse( String color )

    {

	int rgb = 0xffff00;

	if ( color != null ) {

	    color = color.toLowerCase();

	    if ( color.matches( "^#[0-9a-f]+$" ) ) {

		int hexlength = 0;
		int shift = 0;

		if ( color.length() == 4 ) {

		    hexlength = 1;
		    shift = 4;

		} else if ( color.length() == 7 )
		    hexlength = 2;
		else if ( color.length() == 13 ) {

		    hexlength = 4;
		    shift = -8;

		}

		rgb = 0;
		for ( int i = 1; i < color.length(); i += hexlength )
		    rgb = ( rgb << 8 ) +
			Color.hexconv( color.substring( i, i + hexlength ),
				       shift );

	    } else if ( color.matches( "^[0-9]+$" ) )
		rgb = Integer.parseInt( color );
            else if ( color.startsWith( "rgb" ) ) {

                String[] c = color.replaceAll( "[^0-9 ]+", "" ).split( " " );

		rgb = Color.concatenateRGB( Integer.parseInt( c[0] ),
					    Integer.parseInt( c[1] ),
					    Integer.parseInt( c[2] ) );

            }

	}
	return rgb;

    }

}
