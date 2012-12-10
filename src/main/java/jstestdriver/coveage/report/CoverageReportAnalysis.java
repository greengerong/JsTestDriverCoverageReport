package jstestdriver.coveage.report;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;

public class CoverageReportAnalysis {

    private static final String END_OF_RECORD = "end_of_record";
    public static final String COVERAGE_FILE_NAME = "report" + File.separator + "coverage.data.js";
    public static final String REPORT_RESOURCE_NAME = "report.zip";
    private final FileHelper fileHelper;
    private ResourceCopyImpl resourceCopy;

    public CoverageReportAnalysis(FileHelper fileHelper, ResourceCopyImpl resourceCopy) {
        this.fileHelper = fileHelper;
        this.resourceCopy = resourceCopy;
    }

    public void execute(String coverageFile, File outputDirectory,
                        String[] excludes, int limit) throws Exception {
        String[] lines = this.fileHelper.ReadLines(coverageFile);
        List<String[]> splitFile = splitFile(lines);
        List<CoverageData> datas = getAllCoverageData(splitFile);
        exculdesFiles(datas, excludes);
        double rate = this.getPackageCoverageRate(datas);
        writeCoverageDataFile(outputDirectory, toJson(datas), limit, rate);
        assertBuild(datas, limit);
    }

    public void assertBuild(List<CoverageData> datas, int limit)
            throws Exception {
        double rate = this.getPackageCoverageRate(datas);
        if (rate < limit) {
            String message = String
                    .format("Total javaScipt coverage is only %s%% on your project. It is less than requestment %s%%¡£ ",
                            rate, limit);
            System.out.println(message);
            throw new Exception(message);
        }
    }

    private void exculdesFiles(List<CoverageData> datas, String[] excludes) {
        if (excludes == null) {
            return;
        }

        for (int i = 0; i < excludes.length; i++) {
            for (int j = datas.size() - 1; j > 0; j--) {
                Pattern pattern = Pattern.compile(excludes[i],
                        Pattern.CASE_INSENSITIVE);
                if (pattern.matcher(datas.get(j).getFile()).find()) {
                    datas.remove(j);
                }
            }
        }

    }

    private void writeCoverageDataFile(File outputDirectory, String json, int limit,
                                       double rate) throws Exception {
        String source = getCoverageDataSource(json, limit, rate);
        if (this.resourceCopy != null) {
            this.resourceCopy.copy(outputDirectory.getAbsolutePath());
        }
        this.fileHelper.WriteFile(new File(outputDirectory, COVERAGE_FILE_NAME).getAbsolutePath(), new String[]{source});
    }

    private String getCoverageDataSource(String json, int limit, double rate) {
        StringBuilder sb = new StringBuilder();
        sb.append("window.coverageData =");
        sb.append(json);
        sb.append(";");
        sb.append(String.format("window.coverageLimteRate = %s;", limit));
        sb.append(String.format("window.totalRate = %s;", rate));
        return sb.toString();
    }

    private List<CoverageData> getAllCoverageData(List<String[]> splitFile)
            throws Exception {
        List<CoverageData> datas = new ArrayList<CoverageData>();
        for (int i = 0; i < splitFile.size(); i++) {
            CoverageData coverageData = getCoverageData(splitFile.get(i));
            setCoverageCodes(coverageData);
            datas.add(coverageData);
        }

        return datas;
    }

    private void setCoverageCodes(CoverageData coverageData) throws Exception {
        String file = coverageData.getFile();
        coverageData.setCodes(this.fileHelper.ReadLines(file));
    }

    public CoverageData getCoverageData(String[] coverageLines) {
        if (coverageLines == null || coverageLines.length < 1) {
            return null;
        }
        CoverageData coverageData = new CoverageData();
        coverageData
                .setFile(coverageLines[0].replaceAll("^SF\\s*:", "").trim());
        for (int i = 1; i < coverageLines.length - 1; i++) {
            CoverageLine coverageLine = new CoverageLine();
            String[] sp = coverageLines[i].split(":")[1].split(",");
            coverageLine.setLine(Integer.parseInt(sp[0].trim()));
            coverageLine.setHit(Integer.parseInt(sp[1].trim()));
            coverageData.getLines().add(coverageLine);
        }
        return coverageData;
    }

    public List<String[]> splitFile(String[] fileLines) {

        List<String[]> list = new ArrayList<String[]>();
        List<String> file = new ArrayList<String>();
        for (int i = 0; i < fileLines.length; i++) {
            String line = fileLines[i];
            file.add(line);
            if (line.startsWith(END_OF_RECORD)) {
                if (file.size() > 0) {
                    list.add(file.toArray(new String[0]));
                }
                file = new ArrayList<String>();
            }
        }
        return list;
    }

    public String toJson(List<CoverageData> list) throws JSONException {
        JSONArray jsonObject = new JSONArray(list);
        return jsonObject.toString();
    }

    public double getPackageCoverageRate(List<CoverageData> datas) {
        int lineCount = 0;
        int hitCount = 0;
        for (CoverageData data : datas) {
            lineCount += data.getLineCount();
            hitCount += data.getHitCount();
        }
        return hitCount * 1.0 / lineCount * 100;
    }
}
