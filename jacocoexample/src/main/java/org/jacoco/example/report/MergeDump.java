package org.jacoco.example.report;

import org.jacoco.core.tools.ExecFileLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MergeDump {
    @Value("${allExecPath}")
    private  String allExecPath ;
    @Value("${execPath}")
    private  String execPath ;
    private  File destFile ;

    private List<File> fileSets(String dir){
        System.out.println(dir);
        List<File> fileSetList = new ArrayList<File>();
        File path = new File(dir);
        if ( ! path.exists() ){
            System.out.println("No path name is :" + dir);
            return null;
        }
        File[] files = path.listFiles();
        try {
            if (files == null || files.length == 0) {
                return null;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        for (File file : files) {
            if (file.getName().contains(".exec")) {
                System.out.println("文件:" + file.getAbsolutePath());
                fileSetList.add(file);
            } else {
                System.out.println("非exec文件:" + file.getAbsolutePath());
            }
        }
        return fileSetList;
    }

    public void executeMerge() throws Exception {
        this.destFile= new File(allExecPath + "//jacoco.exec");

        final ExecFileLoader loader = new ExecFileLoader();
        load(loader);
        save(loader);
        System.out.println("mergedump完成");
        //删除2个月以前的jacoco
        new DeleteFilesSimple().moveFileToReady(allExecPath);


    }




    /**
     * 加载dump文件
     * @param loader
     * @throws Exception
     */
    public void load(final ExecFileLoader loader) throws Exception {
        for (final File fileSet : fileSets(execPath)) {
            System.out.println(fileSet.getAbsoluteFile());
            final File inputFile = new File(this.execPath, fileSet.getName());
            if (inputFile.isDirectory()) {
                continue;
            }
            try {
                System.out.println("Loading execution data file " + inputFile.getAbsolutePath());
                loader.load(inputFile);
                System.out.println(loader.getExecutionDataStore().getContents());
            } catch (final IOException e) {

            }
        }
    }

    /**
     * 执行合并文件
     * @param loader
     * @throws Exception
     */
    public void save(final ExecFileLoader loader) throws Exception {
        if (loader.getExecutionDataStore().getContents().isEmpty()) {
            System.out.println("Skipping JaCoCo merge execution due to missing execution data files");
            return;
        }
        System.out.println("Writing merged execution data to " + this.destFile.getAbsolutePath());
        try {
            loader.save(this.destFile, false);
        } catch (final IOException e) {
            throw new Exception("Unable to write merged file "
                    + this.destFile.getAbsolutePath(), e);
        }
    }
}