static Logical add_annotation(
		ElementP root
		);

static Logical add_contour(
		ElementP root
		);

static void add_contour_trap(
		char *message,
		void *data
		);

static void append(
		TextBufferP tb,
		char *fmt,
		...
		);

static void build_indexed_triangle_list(
		ElementP surface,
		GeometryBuffer gb
		);

static TextBufferP create_text_buffer(
		void
		);

static Logical delete_annotation(
		ElementP root
		);

static Logical delete_contour(
		ElementP root
		);

static void delete_geometry(
		PGconn *conn,
		int geom_id
		);

static void dump_triangles(
		ElementP surface,
		Triangle *tarray,
		int nt,
		HashTable *vtable
		);

static char *encode(
		char *string
		);

static void exception_handler(
		char *message,
		void *data
		);

static void fail(
		char *message,
		void *data
		);

static char *generate_app_data(
		void
		);

static Logical get_all_contours(
		ElementP root
		);

static Logical get_annotation(
		ElementP root
		);

static Logical get_annotation_z_levels(
		ElementP root
		);

static Logical get_contour_data(
		ElementP root
		);

static Logical get_contours(
		ElementP root
		);

static Logical get_dataset(
		ElementP root
		);

static int get_next_sequence(
		PGconn *conn
		);

static Logical get_objects(
		ElementP root
		);

static char *get_points(
		void
		);

static Logical get_sequence(
		ElementP root
		);

static Logical get_surface(
		ElementP root
		);

static int get_user_id(
		PGconn *conn
		);

static Logical make_surface(
		ElementP root,
		int color,
		int annotation_id,
		int tuples,
		PGresult *result
		);

static void parse_hex(
		void *value,
		char *s,
		int l
		);

static void parse_points(
		size_t *n,
		double **x,
		double **y,
		char *string
		);

static char *sanitize(
		char *s
		);

static Logical set_geometry_type(
		ElementP root
		);

static void signal_handler(
		int sig
		);

static void simplify_points(
		size_t *n,
		double **x,
		double **y
		);

static void smooth(
		size_t *n,
		double **x,
		double **y
		);

static void triangle_handler(
		float t[3][3],
		void *data
		);

static Logical update_annotation(
		ElementP root
		);

static Logical update_contour(
		ElementP root
		);

static Logical validate_name(
		ElementP root
		);

static int vindex_compare(
		const void *p1,
		const void *p2
		);


