package jstestdriver.coveage.report;


public interface FileHelper {

	public abstract void WriteFile(String file, String[] text) throws Exception;

	public abstract String[] ReadLines(String file) throws Exception;

}
