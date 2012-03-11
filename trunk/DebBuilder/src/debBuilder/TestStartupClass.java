package debBuilder;

import java.io.*;

import debBuilder.builderConfig.configManager;
import debBuilder.builderConfig.configModel;
import debProjectModels.*;
import debProjectTool.*;
import sun.net.idn.StringPrep;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-10
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
public class TestStartupClass {
    public static void ffff(String[] args) {
        debProjectModel dpm = new debProjectModel();
        dpm.packageName = "testsoftware";
        dpm.packageVersion = "1.0";
        dpm.packageHomepage = "http://www.baidu.com";
        dpm.packageArchitecture = "i386";
        dpm.packageSection = "utils";
        dpm.packageInstalledSize = "0";
        dpm.debPackagename = "testpackage.deb";
        dpm.resultDir = "/home/wcss/buildtestproject/dest/";
        dpm.projectName = "测试软件工程";
        dpm.packageDescription = "这是一个测试工程！";
        dpm.packageMaintainer = "wcss";
        dpm.packageOriginalMaintainer = "李文龙";
        dpm.packagePriority = "最高";
        dpm.packagePostInstFile = "/home/wcss/buildtestproject/source/inst.sh";
        dpm.packagePostRmFile = "/home/wcss/buildtestproject/source/rm.sh";
        dpm.packagePreInstFile = "/home/wcss/buildtestproject/source/pi.sh";
        dpm.packagePreRmFile = "/home/wcss/buildtestproject/source/pm.sh";

        dpm.packageDepends.add(new debDependsModel("openjdk-6-jdk", "", debDependsModel.allVersion));
        dpm.packageDepends.add(new debDependsModel("wine", "1.2", debDependsModel.moreThanVersion));
        dpm.packageDepends.add(new debDependsModel("ffmpeg", "2.0", debDependsModel.lessThanVersion));

        debStartupModel dsm = new debStartupModel();
        dsm.startupCategories = "system";
        dsm.startupFileName = "teststartup";
        dsm.startupIcon = "testicon";
        dsm.startupNotify = true;
        dsm.isDesktopStartup = false;
        dsm.isStartMenuStartup = true;
        dsm.startupComment = "见解";
        dsm.startupExec = "startup命令";
        dsm.startupGenericName = "测试包";
        dsm.startupTerminal = false;
        dsm.startupName = "启动测试";
        dsm.startupType = "程序";
        dsm.startupVersion = "1.0";
        dsm.iconSourceFile = "/home/wcss/buildtestproject/source/deepindebmaker.png";
        dpm.packageStartupList.add(dsm);

        dpm.packageFiles.add(new debFilesModel("/home/wcss/buildtestproject/source/要复制的文件.txt", "/opt/testsoft/要复制的文件.txt", debFilesModel.copyFile));

        dpm.packageFiles.add(new debFilesModel("/home/wcss/buildtestproject/source/被复制的内容.txt", "/opt/testsoft/创建目录测试/被复制的内容.txt", debFilesModel.copyFile));

        dpm.packageFiles.add(new debFilesModel("/home/wcss/buildtestproject/source/cpdir/","/usr/lib/debbuilder/",debFilesModel.copyDir));

        try {
            builderCompile.compilePackageWithoutMakeDeb(dpm, dpm.resultDir);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    public static void dddd(String[] args)
    {
        debProjectModel dpm = debProjectModelRW.loadProject("/home/wcss/IDEAIC11.02.dpro");
        try {
            builderCompile.compilePackageWithMakeDeb(dpm,"/home/wcss/debBuilderBuffer/ideaic1102");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    public static void fffffffffff(String[] args)
    {
        configModel cm = new configModel();
        try {
            debProjectModelRW.quickEasy.savetofile(cm,"/home/wcss/config.cfg");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
