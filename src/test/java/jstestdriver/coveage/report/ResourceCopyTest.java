package jstestdriver.coveage.report;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class ResourceCopyTest {
    private ResourceCopy resourceCopy = new ResourceCopyImpl();

    @Test
    @Ignore
    public void Test() throws IOException {
        resourceCopy.copy("E:\\test\\");
    }
}
