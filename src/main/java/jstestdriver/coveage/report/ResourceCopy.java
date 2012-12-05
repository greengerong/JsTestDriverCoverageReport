package jstestdriver.coveage.report;

import java.io.IOException;

public interface ResourceCopy {
    public abstract void copy(String resource, String destination) throws IOException;
    public abstract void copy(Class resourceClass,String resource, String destination) throws IOException;
}
