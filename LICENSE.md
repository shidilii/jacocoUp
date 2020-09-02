此功能主要是基于jacoco0.8.4增加了git分支对比，欢迎大家一起提pr改进
使用此功能注意事项如下：
1、对于要监控代码覆盖率的jar包启动方式增加监听，启动方式如下（注意D:\bgy_idea\jacocolib下面要有官网的的jacocoagent.jar）
java -javaagent:D:\bgy_idea\jacocolib\jacocoagent.jar=output=tcpserver,port=18089,append=true,address=127.0.0.1,includes=com.shidili.demo.* -jar D:\bgy-git\demo\target\demo-0.0.1.jar 

2、项目配置属性描述
allExecPath=C://jaco-exec   --存放合并后的dump文件
execPath=C://jaco-exec//execfiles  --每次生成的dump文件
jacocoipadress=localhost --需要监控覆盖率的服务器ip，记得必须是ip
jacocoport=18089  --监控端口
projectPath=C://jaco-exec//demo   --监控的项目，需要用git拉取这个监控的项目，并切换分支为需要监控的，比如release
classDirectory=/target  --项目的class文件，gradle是build
srcPath=/src/main/java  --java路径
remoteBranch=https://github.com/shidilii/demo.git --监控项目的git地址
pullbat=C://jaco-exec//pull2.bat --拉取项目的bat文件

3、注意跑监控服务的服务器和jacoco服务器是一致的，要么都是windows，要么都是linux，否则可能因为不同服务器编译不一致，导致某一些代码覆盖率是错误的

4、整个项目运行过程
a、服务器上跑demo服务，记得增加agent
b、手动执行代码覆盖率接口,浏览器输入这个地址即可，http://localhost:18090/jacoco/generateReporte

5、整个功能使用过程中，有一个问题一致无法解决，就是在调试过程中，有的时候因为jgit插件文件，会一直包拉取代码冲突，
所以如果有冲突问题，可以先删掉整个项目，再拉取一次，然后不要用debug模式跑项目一般是没有问题的