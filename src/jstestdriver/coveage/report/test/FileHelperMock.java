package jstestdriver.coveage.report.test;

import java.util.Map;

import jstestdriver.coveage.report.FileHelper;

public class FileHelperMock implements FileHelper {

	private final Map<String, String[]> map;

	public FileHelperMock(Map<String, String[]> map) {
		this.map = map;
	}

	@Override
	public String[] ReadLines(String file) {
		return map.get(file);
	}

	@Override
	public void WriteFile(String file, String[] text) {
		if (map.containsKey(file)) {
			map.remove(file);
		}
		map.put(file, text);
	}
}
