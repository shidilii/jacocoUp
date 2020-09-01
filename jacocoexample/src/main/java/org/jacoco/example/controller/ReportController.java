package org.jacoco.example.controller;

import org.jacoco.example.report.ExecutionDataClient;
import org.jacoco.example.report.GitUtil;
import org.jacoco.example.report.MergeDump;
import org.jacoco.example.report.ReportGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/jacoco")
public class ReportController {

//    Logger logger = LoggerFactory.getLogger(Logger.class);
    @Resource
    private ExecutionDataClient executionDataClient;
    @Resource
    private MergeDump mergeDump;



    @Value("${projectPath}")
    private String projectPath;

    @Value("${classDirectory}")
    private String classDirectory;
    @Value("${srcPath}")
    private String srcPath;

    @Value("${allExecPath}")
    private String allExecPath;
    @Value("${remoteBranch}")
    private String remoteBranch;

    @Value("${pullbat}")
    private String pullbat;
    @Value("${server.port}")
    private String port;

//    @RequestMapping("/delProject")
//    public void delProject() throws Exception {
//
//        GitUtil gitUtil=new GitUtil();
//        //删除项目
//        gitUtil.deleteFile(new  File(projectPath));
//
//
//    }

    @RequestMapping("/generateReporte")
    public void generateReporte() throws Exception {

        GitUtil gitUtil=new GitUtil();
        //重新获取项目
//        gitUtil.gtiClone(remoteBranch,projectPath);
//        gitUtil.gitPull(projectPath,"shidili","s18942409712**","uat");
        //拉取最新项目，且编译生成class文件
        gitUtil.callCmd(pullbat);

        System.out.println("拉取项目完成");

        //合并exec文件
        executionDataClient.exportJacoco();
        mergeDump.executeMerge();

        //生成覆盖率报告
        ReportGenerator generator = new ReportGenerator(projectPath,classDirectory,allExecPath,srcPath);
        generator.create();

//        logger.info("over generateReporte............."+new Date());

    }


//    @RequestMapping("/gitpull")
//    public void gitpull() throws Exception {
//        GitUtil gitUtil=new GitUtil();
//        //重新获取项目
////        gitUtil.gtiClone(remoteBranch,projectPath);
////        gitUtil.gitPull(projectPath,"shidili","s18942409712**","uat");
//        //拉取最新项目，且编译生成class文件
//        gitUtil.callCmd(pullbat);
//        //项目编译
////        gitUtil.callCmd(compilebat);
//        System.out.println("拉取项目完成");
//    }
//
//
//    @RequestMapping("/generateReporte")
//    public void generateReporte() throws Exception {
//        ReportGenerator generator = new ReportGenerator(projectPath,classDirectory,allExecPath);
//        generator.create();
//        System.out.println("报告生成完毕");
//    }

    @Scheduled(cron="0 0 0/2 * * ?")
    public void test1() throws Exception {
//        如果是服务器2，则延时10分钟执行
        if("18091".equals(port)){
            Thread.sleep(600000);
        }
        System.out.println(port);
        this.generateReporte();
    }


}
