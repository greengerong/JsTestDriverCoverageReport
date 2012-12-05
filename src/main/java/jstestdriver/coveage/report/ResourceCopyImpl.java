package jstestdriver.coveage.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourceCopyImpl implements ResourceCopy {

    @Override
    public void copy(String resource, String destination) throws IOException {
        copy(this.getClass(), resource, destination);
    }

    @Override
    public void copy(Class resourceClass, String resource, String destination) throws IOException {
        byte[] bytes = readReSources(resourceClass, resource);
        System.out.println("--------"+bytes.length);
        File destinationZipFile = writeResources(resource, destination, bytes);
        unZipResources(destination, destinationZipFile);
        System.gc();//if remove,it can not delete zip fie;
        destinationZipFile.delete();
    }

    private void unZipResources(String destination, File destinationZipFile) throws IOException {
        ZipFile zf = new ZipFile(destinationZipFile.getPath());
        Enumeration entity = zf.entries();
        while (entity.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) entity.nextElement();
            if (ze.isDirectory()) {
                new File(destination, ze.getName()).mkdirs();
            } else {
                writeUnZipEntry(destination, zf, ze);
            }
        }
    }

    private void writeUnZipEntry(String destination, ZipFile zf, ZipEntry ze) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(destination, ze.getName()));
        InputStream inputStream = zf.getInputStream(ze);
        fileOutputStream.write(getFileBytes(inputStream));
        inputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private File writeResources(String resource, String destination, byte[] bytes) throws IOException {
        File destinationZipFile = new File(destination, resource);
        FileOutputStream zipOutPut = new FileOutputStream(destinationZipFile);
        zipOutPut.write(bytes);
        zipOutPut.flush();
        zipOutPut.close();
        return destinationZipFile;
    }

    private byte[] readReSources(Class resourceClass, String resource) throws IOException {
        InputStream resourceAsStream = resourceClass.getClassLoader().getResourceAsStream(resource);
        return getFileBytes(resourceAsStream);
    }

    private byte[] getFileBytes(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
    }
}