#define DSID_FMT							\
    "select dataset_id, sproject_id, dataset_name, zoomify_folder, "	\
    "resource_path from slash_dataset where dataset_id = %d"

#define TIMEOUT "TIMEOUT"
#define DEFAULT_TIMEOUT 300

#define SERVER "/var/tmp/SLASHdbServer.%d_%d"
#define CLIENT "/var/tmp/SLASHdbClient.%d"

#define OBJECT_REQ_FMT				\
    "select "					\
    "object_name, "				\
    "annotation_id "				\
    "from slash_annotation "			\
    "where dataset_id = %d"
#define OBJECT_MODEL_REQ_FMT			\
    "select "					\
    "object_name, "				\
    "annotation_id "				\
    "from slash_annotation "			\
    "where dataset_id = %d "			\
    "and version_number = %d"

#define ANNOTATION_Z_RANGE_FMT			\
    "select "					\
    "g.z_index "				\
    "from slash_geometry g "			\
    "LEFT JOIN slash_annot_geom_map map "	\
    "on g.geom_id = map.geometry_id "		\
    "where map.annotation_id = %d"

#define ANNOTATION_FROM_GEOMID_FMT		\
    "select "					\
    "annotation_id "				\
    "from slash_annot_geom_map "		\
    "where geometry_id = %d"

#define COUNT_GEOM_FMT				\
    "select "					\
    "count(*) "					\
    "from slash_annot_geom_map "		\
    "where annotation_id = %d"

#define COUNT_NAME_FMT				\
    "select "					\
    "count(*) "					\
    "from slash_annotation "			\
    "where object_name = '%s' and "		\
    "dataset_id = %d"

#define COUNT_NAME_VERSION_FMT			\
    "select "					\
    "count(*) "					\
    "from slash_annotation "			\
    "where object_name = '%s' and "		\
    "dataset_id = %d and "			\
    "version_number = %d"

#define GET_GEOM_FMT				\
    "select "					\
    "g.polyline, "				\
    "g.z_index, "				\
    "g.geometry_type "				\
    "from slash_geometry g "			\
    "LEFT JOIN slash_annot_geom_map map "	\
    "on g.geom_id = map.geometry_id "		\
    "where map.annotation_id = %d"

#define GET_CLOSED_FMT				\
    "select "					\
    "g.polyline, "				\
    "g.z_index, "				\
    "g.geometry_type "				\
    "from slash_geometry g "			\
    "LEFT JOIN slash_annot_geom_map map "	\
    "on g.geom_id = map.geometry_id and "	\
    "g.geometry_type = 'polygon' "		\
    "where map.annotation_id = %d"

typedef enum {
    Query,
    Update,
    KeepAlive,
} RequestType;

typedef struct {

    RequestType request_type;

    pid_t client_id;

    long ident;

    double scale;
    int dataset_id;
    int model_id;

    double x0;
    double y0;
    double z0;
    double x1;
    double y1;
    double z1;

} Request, *RequestP;

typedef struct {

    double min[2];
    double max[2];

} BoundingBox, *BoundingBoxP;

typedef struct _Entry Entry, *EntryP, **EntryList;

struct _Entry {

    char *object_name;
    char *object_uri;
    char *geometry_type;
    int color;
    int annotation_id;
    int geom_id;
    int user_id;
    char *application_value;
    char *md5;
    int model_id;

};

#define INSIDE(PA0,QA0,PA1,QA1,PB0,QB0,PB1,QB1)	\
    (!( ( PA1 < PB0 ) ||			\
	( PA0 > PB1 ) ||			\
	( QA1 < QB0 ) ||			\
	( QA0 > QB1 ) ) )
#define OVERLAPS(XA0,YA0,XA1,YA1,XB0,YB0,XB1,YB1)	\
    ( INSIDE(XA0,YA0,XA1,YA1,XB0,YB0,XB1,YB1) ||	\
      INSIDE(XB0,YB0,XB1,YB1,XA0,YA0,XA1,YA1) )			       

typedef struct {
    off_t offset;
    size_t buffer_size;
    char *buffer;
} TextBuffer, *TextBufferP;

typedef struct {
    float *v;
    int index;
    float nx;
    float ny;
    float nz;
} Vertex, *VertexP, **VertexPP, **VertexList;

typedef int Triangle[3];

#define MAX_VERTICES USHRT_MAX

typedef struct {
    size_t max_vertices;
    size_t n_vertices;
    VertexP vertex_list;
    HashTable *vertex_table;
    size_t max_triangles;
    size_t n_triangles;
    Triangle *triangle_list;
} GeometryBuffer, *GeometryBufferP;
