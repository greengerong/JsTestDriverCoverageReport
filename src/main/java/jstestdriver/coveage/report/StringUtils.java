package jstestdriver.coveage.report;

public class StringUtils {

	public static String join(String[] text, String spe) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length - 1; i++) {
			sb.append(text[i] + spe);
		}
		sb.append(text[text.length - 1]);
		return sb.toString();
	}

}
