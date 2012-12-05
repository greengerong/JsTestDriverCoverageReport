package jstestdriver.coveage.report;

public class Main {
	public static void main(String[] args) throws Exception {
		// String coverageFile = args[0];
		// String outPutFile = args[1];
		String coverageFile = "C:\\Users\\wolf\\Desktop\\jstdcoverage.txt";
		String outPutFile = "C:\\Users\\wolf\\Desktop\\Green.JsTestDriver.Coveage.Report\\coverage.data.js";
		int limit = 60;
		String[] excludes = null;

		CoverageReportAnalysis coveageReportAnalysis = new CoverageReportAnalysis(
				new DefaultFileHelper(), new ResourceCopyImpl());
		coveageReportAnalysis
				.execute(coverageFile, null, excludes, limit);
	}

	public Main() {
		super();
	}

}