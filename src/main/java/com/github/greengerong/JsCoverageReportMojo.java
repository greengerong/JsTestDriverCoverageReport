package com.github.greengerong;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;

import jstestdriver.coveage.report.CoverageReportAnalysis;
import jstestdriver.coveage.report.DefaultFileHelper;

import jstestdriver.coveage.report.ResourceCopyImpl;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal test
 * @phase process-sources
 */
public class JsCoverageReportMojo extends AbstractMojo {
    /**
     * out put directory.
     *
     * @parameter
     * @required
     */
    private File outputDirectory;
    /**
     * coverage file path
     *
     * @parameter
     * @required
     */
    private String coverageFile;
    /**
     * limit default 0.
     *
     * @parameter
     */
    private int limit = 0;
    /**
     * excludes js file.
     *
     * @parameter
     */
    private String[] excludes;

    public void execute() throws MojoExecutionException {
        String outPutFile;
        try {
//            getLog().info("JsCoverageReport maven execute:");
            CoverageReportAnalysis coverageReportAnalysis = new CoverageReportAnalysis(
                    new DefaultFileHelper(),new ResourceCopyImpl());
            coverageReportAnalysis.execute(coverageFile, outputDirectory, excludes,
                    limit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }

        getLog().info(String.format("%s already finished(%s).", CoverageReportAnalysis.COVERAGE_FILE_NAME,
                new File(outputDirectory, CoverageReportAnalysis.COVERAGE_FILE_NAME).getAbsolutePath()));

    }
}
