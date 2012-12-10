package jstestdriver.coveage.report;

import java.io.*;
import java.nio.charset.Charset;

public class ResourceCopyImpl implements ResourceCopy {

    @Override
    public void copy(String destination) throws IOException {
        copy(this.getClass(), destination);
    }

    @Override
    public void copy(Class resourceClass, String destination) throws IOException {
        createAllDirs(destination);
        createFiles(resourceClass, destination);
    }

    private void createFiles(Class resourceClass, String destination) throws IOException {
        createFile(resourceClass, destination, "report/css/bootstrap.min.css");
        createFile(resourceClass, destination, "report/css/style.css");
        createFile(resourceClass, destination, "report/img/glyphicons-halflings-white.png");
        createFile(resourceClass, destination, "report/img/glyphicons-halflings.png");
        createFile(resourceClass, destination, "report/script/angular.min.js");
        createFile(resourceClass, destination, "report/script/bootstrap.min.js");
        createFile(resourceClass, destination, "report/script/jquery.min.js");
        createFile(resourceClass, destination, "report/script/coverage_report.js");
        createFile(resourceClass, destination, "report/script/underscore-min.js");
        createFile(resourceClass, destination, "report/Report.html");
    }

    private void createFile(Class resourceClass, String destination, String resource) throws IOException {
        InputStream resourceAsStream = resourceClass.getClassLoader().getResourceAsStream(resource);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream,
                Charset.forName("UTF-8")));
        File file = new File(destination, resource);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        String content = "";
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content += line + System.getProperty("line.separator");
        }
        bufferedReader.close();
        resourceAsStream.close();

        fileOutputStream.write(content.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private void createAllDirs(String destination) {
        mkdirs(destination, "report/css");
        mkdirs(destination, "report/img");
        mkdirs(destination, "report/script");
    }

    private void mkdirs(String destination, String dir) {
        File file = new File(destination, dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}