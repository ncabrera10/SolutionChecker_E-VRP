package globalParameters;

public class GlobalParameters {


	public static final String INSTANCE_FOLDER = GlobalParametersReader.<String>get("INSTANCE_FOLDER", String.class);
	public static final String RESULT_FOLDER = GlobalParametersReader.<String>get("RESULT_FOLDER", String.class);
	public static final String SOLUTIONS_FOLDER = GlobalParametersReader.<String>get("SOLUTIONS_FOLDER", String.class);

	public static final int PRECISION = GlobalParametersReader.<Integer>get("PRECISION", Integer.class);
	public static final double DECIMAL_PRECISION = Math.pow(10, -PRECISION);

	public static final int SOLVER_PRECISION = GlobalParametersReader.<Integer>get("SOLVER_PRECISION", Integer.class);
	public static final int SOLVER_NB_THREADS = GlobalParametersReader.<Integer>get("SOLVER_NB_THREADS", Integer.class);
	public static final String SOLVER_LOG_FOLDER = GlobalParametersReader.<String>get("SOLVER_LOG_FOLDER",
			String.class);
	public static final String SOLVER_LP_FOLDER = GlobalParametersReader.<String>get("SOLVER_LP_FOLDER", String.class);
	
}
