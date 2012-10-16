package jstestdriver.coveage.report;

public class Main {
	public static void main(String[] args) throws Exception {
		String coverageFile = args[0];
		String outPutFile = args[1];
		int limit = args.length > 2 ? Integer.parseInt(args[3]) : 0;
		String[] excludes = args.length > 3 ? args[4].split(",") : null;

		CoveageReportAnalysis coveageReportAnalysis = new CoveageReportAnalysis(
				new DefaultFileHelper());
		coveageReportAnalysis
				.execute(coverageFile, outPutFile, excludes, limit);
	}

	public Main() {
		super();
	}

}