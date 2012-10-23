package jstestdriver.coveage.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jstestdriver.coveage.report.CoveageReportAnalysis;
import jstestdriver.coveage.report.CoverageData;
import jstestdriver.coveage.report.CoverageLine;
import jstestdriver.coveage.report.FileHelper;
import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

public class CoveageReportAnalysisTest {

	private CoveageReportAnalysis coveageReportAnalysis;
	private FileHelper fileHelper;

	@Before()
	public void setUp() {
		coveageReportAnalysis = new CoveageReportAnalysis(initFileHelperMock());
	}

	@Test
	public void test() throws Exception {
		coveageReportAnalysis.execute("fileCoverage2.dat", "outputfile.js",
				new String[] { "\\w*2\\w*.js" }, 0);
		String output = this.fileHelper.ReadLines("outputfile.js")[0];
		// System.out.print(output);
		Assert.assertEquals(
				"window.coverageData =[{\"file\":\"coverage.data.js\",\"codes\":[\"/*Created by JetBrains RubyMine.*/\""
						+ ",\"window.coverageData = [\",\"    {\",\"        file:\\\"src/spec1.js\\\", coverage:[\",\"       "
						+ "{line:2, hit:3},\",\"        {line:4, hit:1},\",\"        {line:12, hit:0}\",\"    ],"
						+ " codes:[\",\"         \\\"/** Created by JetBrains RubyMine.*/\\\",\",\"    ]\",\"    }\",\"]"
						+ ";\",\"window.coverageLimteRate = 50;\"],\"lines\":[{\"hit\":1,\"line\":1},{\"hit\":0,\"line\":3},"
						+ "{\"hit\":3,\"line\":4},{\"hit\":1,\"line\":8},{\"hit\":0,\"line\":11}]}];"
						+ "window.coverageLimteRate = 0;", output);
	}

	@Test
	public void should_get_coveage_data_from_file() {
		CoverageData data = coveageReportAnalysis
				.getCoverageData(createCoverageFile1Lines());
		Assert.assertEquals("coverage.data.js", data.getFile());
		Assert.assertEquals(5, data.getLines().size());

		Assert.assertEquals(1, data.getLines().get(0).getLine());
		Assert.assertEquals(1, data.getLines().get(0).getHit());

		Assert.assertEquals(11, data.getLines().get(4).getLine());
		Assert.assertEquals(0, data.getLines().get(4).getHit());

	}

	@Test
	public void should_split_js_file_from_file() {
		String[] fileLines = joinCoverageFile();
		List<String[]> data = coveageReportAnalysis.splitFile(fileLines);

		Assert.assertEquals(2, data.size());
		Assert.assertEquals("SF:coverage.data.js", data.get(0)[0]);
		Assert.assertEquals("DA: 1,1", data.get(0)[1]);
		Assert.assertEquals("SF:coverage.data2.js", data.get(1)[0]);
		Assert.assertEquals("DA: 1,1", data.get(1)[1]);

	}

	@Test
	public void should_covert_CoverageData_object_to_json_()
			throws JSONException {
		CoverageData coverageData = new CoverageData();
		coverageData.setFile("coverage.data.js");
		CoverageLine coverageLine = new CoverageLine();
		coverageLine.setHit(0);
		coverageLine.setLine(1);
		coverageData.getLines().add(coverageLine);
		List<CoverageData> list = new ArrayList<CoverageData>();
		list.add(coverageData);
		String json = coveageReportAnalysis.toJson(list);
		Assert.assertEquals(
				"[{\"file\":\"coverage.data.js\",\"lines\":[{\"hit\":0,\"line\":1}]}]",
				json);
	}

	private List<String> toList(String[] lines) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < lines.length; i++) {
			list.add(lines[i]);
		}
		return list;
	}

	private FileHelper initFileHelperMock() {
		String[] fileLines = joinCoverageFile();
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("fileCoverage.dat", createCoverageFile1Lines());
		map.put("fileCoverage2.dat", fileLines);
		map.put("outputfile.js", null);
		map.put("coverage.data.js", createCoverageDataJsFile());
		fileHelper = new FileHelperMock(map);
		return fileHelper;
	}

	private String[] joinCoverageFile() {
		List<String> list = toList(createCoverageFile1Lines());
		list.addAll(toList(createCoverageFile2Lines()));
		String[] fileLines = (String[]) (list.toArray(new String[0]));
		return fileLines;
	}

	private String[] createCoverageDataJsFile() {
		return new String[] { "/*Created by JetBrains RubyMine.*/",
				"window.coverageData = [", "    {",
				"        file:\"src/spec1.js\", coverage:[",
				"       {line:2, hit:3},", "        {line:4, hit:1},",
				"        {line:12, hit:0}", "    ], codes:[",
				"         \"/** Created by JetBrains RubyMine.*/\",", "    ]",
				"    }", "];", "window.coverageLimteRate = 50;" };
	}

	private String[] createCoverageFile1Lines() {
		return new String[] { "SF:coverage.data.js", "DA: 1,1", "DA: 3,0",
				"DA: 4,3", "DA: 8,1", "DA: 11,0", "record_end" };
	}

	private String[] createCoverageFile2Lines() {
		return new String[] { "SF:coverage.data2.js", "DA: 1,1", "DA: 3,0",
				"DA: 4,3", "DA: 8,1", "DA: 11,0", "record_end" };
	}
}
