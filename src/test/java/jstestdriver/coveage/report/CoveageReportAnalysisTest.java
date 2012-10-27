package jstestdriver.coveage.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CoveageReportAnalysisTest {

	private CoveageReportAnalysis coveageReportAnalysis;
	private FileHelper fileHelper;

	@Before()
	public void setUp() {
		coveageReportAnalysis = new CoveageReportAnalysis(initFileHelperMock());
	}

	@Test
	@Ignore
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
		List<CoverageData> list = new ArrayList<CoverageData>();
		list.add(getCoverageData("coverage.data.js"));
		String json = coveageReportAnalysis.toJson(list);
		Assert.assertEquals(
				"[{\"hitCount\":0,\"file\":\"coverage.data.js\",\"lineCount\":1,\"lines\":[{\"hit\":0,\"beHit\":false,\"line\":1}]}]",
				json);
	}

	@Test
	public void should_get_right_pack_coverage_rate() {
		List<CoverageData> datas = new ArrayList<CoverageData>();
		CoverageData coverageData1 = getCoverageData("coverage.data.js");
		CoverageData coverageData2 = getCoverageData("coverage.data2.js");
		coverageData2.getLines().get(0).setHit(1);
		datas.addAll(Arrays.asList(coverageData1, coverageData2));
		double rate = coveageReportAnalysis.getPackageCoverageRate(datas);
		Assert.assertEquals(rate, 50.0);
	}

	@Test
	public void should_no_exception_when_build_scuess() throws Exception {
		List<CoverageData> datas = new ArrayList<CoverageData>();
		CoverageData coverageData1 = getCoverageData("coverage.data.js");
		CoverageData coverageData2 = getCoverageData("coverage.data2.js");
		coverageData2.getLines().get(0).setHit(1);
		datas.addAll(Arrays.asList(coverageData1, coverageData2));
		coveageReportAnalysis.assertBuild(datas, 30);
	}

	@Test(expected = Exception.class)
	public void should_exception_when_build_failed() throws Exception {
		List<CoverageData> datas = new ArrayList<CoverageData>();
		CoverageData coverageData1 = getCoverageData("coverage.data.js");
		CoverageData coverageData2 = getCoverageData("coverage.data2.js");
		coverageData2.getLines().get(0).setHit(1);
		datas.addAll(Arrays.asList(coverageData1, coverageData2));
		coveageReportAnalysis.assertBuild(datas, 60);
	}

	private CoverageData getCoverageData(String file) {
		CoverageData coverageData = new CoverageData();
		coverageData.setFile(file);
		CoverageLine coverageLine = new CoverageLine();
		coverageLine.setHit(0);
		coverageLine.setLine(1);
		coverageData.getLines().add(coverageLine);
		return coverageData;
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
