/*******************************************************************************
 * Copyright (c) 2009, 2019 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Brock Janiczak - initial API and implementation
 *
 *******************************************************************************/
package org.jacoco.example.report;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.internal.analysis.BundleCoverageImpl;
import org.jacoco.core.internal.diff.GitAdapter;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This example creates a HTML report for eclipse like projects based on a
 * single execution data store called jacoco.exec. The report contains no
 * grouping information.
 *
 * The class files under test must be compiled with debug information, otherwise
 * source highlighting will not work.
 */
public class ReportGenerator {


	private final String title;

	private final File executionDataFile;
	private final File classesDirectory;
	private final File sourceDirectory;
	private final File reportDirectory;


	private  String gitPath;
	private ExecFileLoader execFileLoader;

	/**
	 * Create a new generator based for the given project.
	 *

	 */
	public ReportGenerator( String projectPath,String classPath,String targetDirectory,String srcPath) {
		File projectDirectory=new File(projectPath);
		this.title = projectDirectory.getName();
		this.executionDataFile = new File(targetDirectory, "jacoco.exec");
		this.classesDirectory = new File(projectDirectory, classPath);
		this.sourceDirectory = new File(projectDirectory, srcPath);
		this.reportDirectory = new File(targetDirectory+"//coveragereport");
		this.gitPath=projectPath;
	}

	/**
	 * Create the report.
	 *
	 * @throws IOException
	 */
	public void create() throws IOException {

		// Read the jacoco.exec file. Multiple data files could be merged
		// at this point
		loadExecutionData();

		// Run the structure analyzer on a single class folder to build up
		// the coverage model. The process would be similar if your classes
		// were in a jar file. Typically you would create a bundle for each
		// class folder and each jar you want in your report. If you have
		// more than one bundle you will need to add a grouping node to your
		// report
		final IBundleCoverage bundleCoverage = analyzeStructure();

		createReport(bundleCoverage);
		System.out.println("生成报告完毕");

	}

	private void createReport(final IBundleCoverage bundleCoverage)
			throws IOException {

		final HTMLFormatter htmlFormatter = new HTMLFormatter();
		final IReportVisitor visitor = htmlFormatter.createVisitor(new FileMultiReportOutput(reportDirectory));

		// Initialize the report with all of the execution and session
		// information. At this point the report doesn't know about the
		// structure of the report being created
		visitor.visitInfo(execFileLoader.getSessionInfoStore().getInfos(),execFileLoader.getExecutionDataStore().getContents());

		// Populate the report structure with the bundle coverage information.
		// Call visitGroup if you need groups in your report.
		visitor.visitBundle(bundleCoverage, new DirectorySourceFileLocator(sourceDirectory, "utf-8", 4));


//		//多源码路径
//        MultiSourceFileLocator sourceLocator = new MultiSourceFileLocator(4);
//        sourceLocator.add( new DirectorySourceFileLocator(sourceDirectory1, "utf-8", 4));
//        sourceLocator.add( new DirectorySourceFileLocator(sourceDirectory2, "utf-8", 4));
//        sourceLocator.add( new DirectorySourceFileLocator(sourceDirectoryN, "utf-8", 4));
//        visitor.visitBundle(bundleCoverage,sourceLocator);

		// Signal end of structure information to allow report to write all
		// information out
		visitor.visitEnd();

	}

	private void loadExecutionData() throws IOException {
		execFileLoader = new ExecFileLoader();
		execFileLoader.load(executionDataFile);
	}









	private IBundleCoverage analyzeStructure() throws IOException {

		//git登录授权
		GitAdapter.setCredentialsProvider("dili_shi@163.com", "s18942409712*");
		//全量覆盖
//		final CoverageBuilder coverageBuilder = new CoverageBuilder();

		//基于分支比较覆盖，参数1：本地仓库，参数2：开发分支（预发分支），参数3：基线分支(不传时默认为master)
		//本地Git路径，新分支 第三个参数不传时默认比较maser，传参数为待比较的基线分支
		final CoverageBuilder coverageBuilder =new CoverageBuilder(gitPath,"release","master");


		//基于Tag比较的覆盖 参数1：本地仓库，参数2：代码分支，参数3：新Tag(预发版本)，参数4：基线Tag（变更前的版本）
//		final CoverageBuilder coverageBuilder = new CoverageBuilder(gitPath,"uat","v9","v8");


		if(CoverageBuilder.classInfos!=null && CoverageBuilder.classInfos.size()>0){
			final Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
			analyzer.analyzeAll(classesDirectory);
			return coverageBuilder.getBundle(title);
		}else{
			//old和new分支一模一样情况，不生成任何代码
			return new BundleCoverageImpl("没有需要覆盖的文件",new ArrayList());
		}


	}




}
