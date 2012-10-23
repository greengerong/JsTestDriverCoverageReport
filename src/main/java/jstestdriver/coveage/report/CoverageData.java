package jstestdriver.coveage.report;

import java.util.ArrayList;
import java.util.List;

public class CoverageData {
	private String file;
	private List<CoverageLine> lines;
	private String[] codes;

	public CoverageData() {

		lines = new ArrayList<CoverageLine>();
	}

	public CoverageData(String file) {

		this();
		this.file = file;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public List<CoverageLine> getLines() {
		return lines;
	}

	public String[] getCodes() {
		return codes;
	}

	public void setCodes(String[] codes) {
		this.codes = codes;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CoverageData)) {
			return false;
		}
		return this.file.equals(((CoverageData) obj).file);
	}

	@Override
	public int hashCode() {
		return this.file.hashCode();
	}

}
