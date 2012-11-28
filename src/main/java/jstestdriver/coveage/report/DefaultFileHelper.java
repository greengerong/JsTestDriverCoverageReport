package jstestdriver.coveage.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DefaultFileHelper implements FileHelper {

	public void WriteFile(String file, String[] text) throws Exception {
		innerWriteLines(file, text);
	}

	private void innerWriteLines(String file, String[] text) throws Exception {
		String source = StringUtils.join(text, "");
		OutputStream fos = new FileOutputStream(file);
		fos.write(source.getBytes());
		fos.close();
	}

	public String[] ReadLines(String file) throws Exception {
		return innerReadLines(file);
	}

	private String[] innerReadLines(String file) throws Exception {
		File fileHandler = new File(file);
		if (!fileHandler.exists()) {
			return new String[0];
		}
		InputStream fis;
		BufferedReader br;
		String line;
		fis = new FileInputStream(file);
		br = new BufferedReader(new InputStreamReader(fis,
				Charset.forName("UTF-8")));
		List<String> list = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		fis.close();
		br.close();
		return list.toArray(new String[0]);
	}
}
