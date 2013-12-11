#ifdef GL_ES
precision highp float;
#endif
varying vec4 vTransformedNormal;
varying vec4 vPosition;

uniform vec4 uColor;
uniform float uMaterialShininess;
uniform vec3 eyeDirection;

uniform vec3 uPointLightingLocation;

void main(void)

{

    vec3 pointLightingSpecularColor = vec3( 1, 1, 1 );
    vec3 ambientColor =
	vec3( uColor.r / 2., uColor.g / 2., uColor.b / 2. );
    vec3 pointLightingDiffuseColor = vec3( uColor.r * 0.2,
					   uColor.g * 0.2,
					   uColor.b * 0.2 );

    vec3 lightDirection = normalize(uPointLightingLocation - vPosition.xyz);
    vec3 normal = normalize(vTransformedNormal.xyz);

    float nl = length( normal );

    if ( nl > 0.0 ) {

	vec3 reflectionDirection1 = reflect(-lightDirection, normal);

	float specularLightWeighting1 =
	    pow(max(dot(reflectionDirection1, normalize( eyeDirection ) ), 0.0),
		5.0 );

	vec3 reflectionDirection2 = reflect(-lightDirection, -normal);

	float specularLightWeighting2 =
	    pow(max(dot(reflectionDirection2, normalize( eyeDirection ) ), 0.0),
		5.0 );

	float specularLightWeighting;
	if ( specularLightWeighting1 > specularLightWeighting2 )
	    specularLightWeighting = specularLightWeighting1;
	else
	    specularLightWeighting = specularLightWeighting2;

	float diffuseLightWeighting = max(dot(normal, lightDirection), 0.0);

	vec3 lightWeighting = ambientColor
	    + pointLightingSpecularColor * specularLightWeighting
	    + pointLightingDiffuseColor * diffuseLightWeighting;

	vec4 fragmentColor = uColor;

	gl_FragColor =
	    vec4(fragmentColor.rgb * lightWeighting, fragmentColor.a);

    } else
	gl_FragColor = uColor;

}
