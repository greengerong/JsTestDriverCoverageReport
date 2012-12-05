package jstestdriver.coveage.report;

import junit.framework.Assert;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class ResourceCopyTest {
    private ResourceCopy resourceCopy = new ResourceCopyImpl();

    @Test
    public void Test() throws IOException {
        resourceCopy.copy("report.zip","E:\\test\\");
    }
}
