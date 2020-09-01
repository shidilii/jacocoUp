package org.jacoco.example.report;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;

public class GitUtil {
//    public  void delFile(File directory) {
//        if (!directory.isDirectory()){
//            directory.delete();
//        } else{
//            File [] files = directory.listFiles();
//
//            // 空文件夹
//            if (files.length == 0){
//                directory.delete();
//                System.out.println("删除" + directory.getAbsolutePath());
//                return;
//            }
//
//            // 删除子文件夹和子文件
//            for (File file : files){
//                if (file.isDirectory()){
//                    delFile(file);
//                } else {
//                    file.delete();
//                    System.out.println("删除" + file.getAbsolutePath());
//                }
//            }
//
//            // 删除文件夹本身
//            directory.delete();
//            System.out.println("删除" + directory.getAbsolutePath());
//        }
//
//    }

    public boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        }
        boolean result=file.delete();
        System.out.println(result);
        return result;
    }

    /**
     * 拉取远程仓库内容到本地,需要账号和密码的
     */

    public void gitPull(String localPath, String username, String password, String branch) throws IOException, GitAPIException {
        UsernamePasswordCredentialsProvider provider = new
                UsernamePasswordCredentialsProvider(username, password);
        //git仓库地址
        Repository repository = new FileRepository(localPath + "/.git");

        Git git = new Git(repository);
        git.reset();
        git.pull().setRemoteBranchName(branch).setCredentialsProvider(provider).call();
        git.close();
    }


    public void gtiClone(String remotePath,String localPath) throws IOException, GitAPIException {
        //设置远程服务器上的用户名和密码
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("shidili","s18942409712**");

        //克隆代码库命令
        CloneCommand cloneCommand = Git.cloneRepository();

        Git git= cloneCommand.setURI(remotePath) //设置远程URI
                .setBranch("uat") //设置clone下来的分支
                .setDirectory(new File(localPath)) //设置下载存放路径
                .setCredentialsProvider(usernamePasswordCredentialsProvider) //设置权限验证
                .call();
        git.close();
        System.out.print(git.tag());
    }


    public  void  callCmd(String locationCmd){
        Runtime r=Runtime.getRuntime();
        Process p=null;
        try{
            p = r.exec(locationCmd);
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            errorGobbler.start();
             StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");
             outGobbler.start();
            p.waitFor();
        }catch(Exception e){
            System.out.println("运行错误:"+e.getMessage());
            e.printStackTrace();
        }
    }
}
