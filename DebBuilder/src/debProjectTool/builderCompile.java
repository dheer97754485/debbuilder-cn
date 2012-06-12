package debProjectTool;

import debBuilder.builderConfig.configManager;
import debProjectModels.debProjectModel;
import JAppToolKit.*;
import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-3-8
 * Time: 下午9:24
 * To change this template use File | Settings | File Templates.
 */
public class builderCompile
{

    /**
     * 检查路径是否正确
     * @param projectbasedir
     * @param debfilepath
     */
    private static void checkCompilePath(String projectbasedir,String debfilepath)
    {
        File basedir = new File(projectbasedir);
        basedir.mkdirs();
        File debpath = new File(new File(debfilepath).getParent());
        debpath.mkdirs();
    }

    /**
     * 获取工程基本路径
     * @param bufferdirs
     * @return
     */
    private static String getProjectBasePath(String bufferdirs,String pkgtypename)
    {
        String projectbasedir = "";
        if (bufferdirs.endsWith("/")) {
            projectbasedir = bufferdirs.substring(0, bufferdirs.length() - 1);
        } else {
            projectbasedir = bufferdirs;
        }
        projectbasedir = projectbasedir + "/" + pkgtypename + "/time_" +(new Date().getTime());
        return projectbasedir;
    }

    /**
     * 获取包路径(无后缀)
     * @param sourcedebfilepath
     * @return
     */
    private static String getDebFilePathWithoutExtName(String sourcedebfilepath)
    {
       if (sourcedebfilepath.toLowerCase().endsWith(".deb") || sourcedebfilepath.toLowerCase().endsWith(".rpm"))
       {
          return new File(sourcedebfilepath).getParent() + "/" + new File(sourcedebfilepath).getName().substring(0,new File(sourcedebfilepath).getName().indexOf(".",new File(sourcedebfilepath).getName().length() - 4) > 0?new File(sourcedebfilepath).getName().indexOf(".",new File(sourcedebfilepath).getName().length() - 4):new File(sourcedebfilepath).getName().length() -1);
       }else
       {
          return sourcedebfilepath;
       }
    }

    /**
     * 编译包
     *
     * @return 执行结果
     */
    public static void compilePackage(debProjectModel project, String bufferdir,String debfilepaths,Boolean makedebpkg) throws Exception
    {
        String debbasedirs = getProjectBasePath(bufferdir,"deb");
        String rpmbasedirs = getProjectBasePath(bufferdir,"rpm");
        String ypkbasedirs = getProjectBasePath(bufferdir,"ypk");
        String pkgtype = project.packageMakerType;
        
        String debfilepath = getDebFilePathWithoutExtName(debfilepaths) + ".deb";
        String rpmfilepath = getDebFilePathWithoutExtName(debfilepaths) + ".rpm";
        String ypkfilepath = getDebFilePathWithoutExtName(debfilepaths) + ".ypk";

        System.out.println("deb file:" + debfilepath);
        System.out.println("rpm file:" + rpmfilepath);
        System.out.println("ypk file:" + ypkfilepath);

        checkCompilePath(debbasedirs, debfilepath);
        checkCompilePath(rpmbasedirs,rpmfilepath);
        checkCompilePath(ypkbasedirs,ypkfilepath);

        clearBufferDirs(project, debbasedirs, rpmbasedirs, ypkbasedirs);

        if (project.autoUpdate != null && project.autoUpdate.equals("ok"))
        {
            addUpdateSupport(project,debbasedirs,rpmbasedirs,ypkbasedirs);
        }

        //编译软件包
        if (pkgtype != null && pkgtype.toLowerCase().equals("deb"))
        {
            debProjectCompile.compileDebPackage(project,debbasedirs);
        }else if (pkgtype != null && pkgtype.toLowerCase().equals("rpm"))
        {
            rpmProjectCompile.compileRpmPackage(project,rpmbasedirs);
        }else if (pkgtype != null && pkgtype.toLowerCase().equals("ypk"))
        {
            ypkProjectCompile.compileYpkPackage(project,ypkbasedirs);
        }

        //生成包文件
        if (configManager.config.compileType != null && configManager.config.compileType.toLowerCase().equals("system"))
        {
           if (pkgtype != null && pkgtype.toLowerCase().equals("deb"))
           {
               makePackageFile(project,"dpkg -b (source) (dest)",debbasedirs,debfilepath);
           }else if (pkgtype != null && pkgtype.toLowerCase().equals("rpm"))
           {
               ArrayList al = new ArrayList();
               al.add("cd " + rpmbasedirs);
               al.add("rpmbuild -bb " + "pkgbuild.spec" + " --buildroot=" + rpmbasedirs);
               al.add("cp " + new File(rpmbasedirs).getParent() + "/*.rpm " + project.resultDir);
               al.add("rm -rf " + new File(rpmbasedirs).getParent() + "/*.rpm");
               JDataHelper.writeAllLines(JRunHelper.getCmdRunScriptBufferDir() + "/rpmbuild.sh",JDataHelper.convertTo(al.toArray()));
               JRunHelper.runSysCmd("chmod +x " + JRunHelper.getCmdRunScriptBufferDir() + "/rpmbuild.sh");
               makePackageFile(project, JRunHelper.getCmdRunScriptBufferDir() + "/rpmbuild.sh", rpmbasedirs, rpmfilepath);
           }else if (pkgtype != null && pkgtype.toLowerCase().equals("ypk"))
           {
               ArrayList al = new ArrayList();
               al.add("cd " + ypkbasedirs);
               al.add("cd ..");
               al.add("ypkg -b " + new File(ypkbasedirs).getName());
               al.add("cp " + new File(ypkbasedirs).getParent() + "/*.ypk " + project.resultDir);
               al.add("rm -rf " + new File(rpmbasedirs).getParent() + "/*.ypk");
               JDataHelper.writeAllLines(JRunHelper.getCmdRunScriptBufferDir() + "/ypkbuild.sh",JDataHelper.convertTo(al.toArray()));
               JRunHelper.runSysCmd("chmod +x " + JRunHelper.getCmdRunScriptBufferDir() + "/ypkbuild.sh");
               makePackageFile(project, JRunHelper.getCmdRunScriptBufferDir() + "/ypkbuild.sh", rpmbasedirs, rpmfilepath);

           }
        }else
        {
            if (pkgtype != null && pkgtype.toLowerCase().equals("deb"))
            {
                makePackageFile(project,configManager.config.compileCmd,debbasedirs,debfilepath);
            }else if (pkgtype != null && pkgtype.toLowerCase().equals("rpm"))
            {
                makePackageFile(project,configManager.config.compileCmd,rpmbasedirs,rpmfilepath);
            }else if (pkgtype != null && pkgtype.toLowerCase().equals("ypk"))
            {
                makePackageFile(project,configManager.config.compileCmd,ypkbasedirs,ypkfilepath);
            }

        }
    }

    /**
     * 清理缓存目录
     * @param projects
     * @param debdir
     * @param rpmdir
     * @param ypkdir
     * @throws Exception
     */
    private static void clearBufferDirs(debProjectModel projects, String debdir,String rpmdir,String ypkdir) throws Exception {
        String bufdir = "";
        if (projects.packageMakerType.toLowerCase().contains("deb"))
        {
            bufdir = debdir;
        }if (projects.packageMakerType.toLowerCase().contains("rpm"))
        {
            bufdir = rpmdir;
        }if (projects.packageMakerType.toLowerCase().contains("ypk"))
        {
            bufdir = ypkdir;
        }
        ArrayList<String> clearbuffers = new ArrayList<String>();
        clearbuffers.add("rm -rf " + new File(bufdir).getParent() + "/*.*");
        JDataHelper.writeAllLines(JRunHelper.getCmdRunScriptBufferDir() + "/clearbuffers.sh",JDataHelper.convertTo(clearbuffers.toArray()));
        JRunHelper.runSysCmd("chmod +x " + JRunHelper.getCmdRunScriptBufferDir() + "/clearbuffers.sh");
        JRunHelper.runSysCmd(JRunHelper.getCmdRunScriptBufferDir() + "/clearbuffers.sh");
    }

    /**
     * 增加自动更新支持
     * @param projects
     * @param debdir
     * @param rpmdir
     * @param ypkdir
     */
    private static void addUpdateSupport(debProjectModel projects, String debdir,String rpmdir,String ypkdir) throws Exception {
        String bufdir = "";
        if (projects.packageMakerType.toLowerCase().contains("deb"))
        {
            bufdir = debdir;
        }if (projects.packageMakerType.toLowerCase().contains("rpm"))
        {
            bufdir = rpmdir;
        }if (projects.packageMakerType.toLowerCase().contains("ypk"))
        {
            bufdir = ypkdir;
        }

        File etcf = new File(bufdir + "/etc");
        etcf.mkdirs();
        File appf = new File(bufdir + new File(projects.updateConfig.localAppPath).getParent() + "/updateapp/lib");
        appf.mkdirs();
        ArrayList<String> configwrite = new ArrayList<String>();
        configwrite.add("listurl=" + projects.updateConfig.updateListUrl);
        configwrite.add("currentversion=" + projects.updateConfig.currentVersion);
        configwrite.add("softname=" + projects.updateConfig.softName);
        configwrite.add("homepage=" + projects.updateConfig.homepage);
        configwrite.add("managerinfo=" + projects.updateConfig.managerInfo);
        configwrite.add("updateinfourl=" + projects.updateConfig.updateInfoUrl);
        configwrite.add("softWorkPath=" + projects.updateConfig.localAppPath);
        configwrite.add("mainAppName=" + "");
        JDataHelper.writeAllLines(bufdir + "/etc/" + projects.packageName + "_update.cfg",JDataHelper.convertTo(configwrite.toArray()));
        ArrayList<String> copyupdates = new ArrayList<String>();
        copyupdates.add("cp " + configManager.config.workDir + "/updateapp/*.* " + appf.getParent());
        copyupdates.add("cp " + configManager.config.workDir + "/updateapp/lib/*.* " + appf.getAbsolutePath());
        JDataHelper.writeAllLines(JRunHelper.getCmdRunScriptBufferDir() + "/copyupdate.sh",JDataHelper.convertTo(copyupdates.toArray()));
        JRunHelper.runSysCmd("chmod +x " + JRunHelper.getCmdRunScriptBufferDir() + "/copyupdate.sh");
        JRunHelper.runSysCmd(JRunHelper.getCmdRunScriptBufferDir() + "/copyupdate.sh");
    }

    /**
     * 编译包(不生成Deb包)
     * @param project
     * @param bufferdir
     */
    public static void compilePackageWithoutMakeDeb(debProjectModel project,String bufferdir) throws Exception
    {
        compilePackage(project,bufferdir,project.resultDir + "/" + project.debPackagename,false);
    }

    /**
     * 生成包自定义包路径
     * @param project
     * @param bufferdir
     */
    public static void compilePackageWithMakeDeb(debProjectModel project,String bufferdir,String debfile) throws Exception
    {
        compilePackage(project,bufferdir,debfile,false);
    }

    /**
     * 生成包使用默认路径
     * @param project
     * @param bufferdir
     * @throws Exception
     */
    public static void compilePackageWithMakeDeb(debProjectModel project,String bufferdir) throws Exception {
        compilePackageWithMakeDeb(project, bufferdir, project.resultDir + "/" + project.debPackagename);
    }

    /**
     * 生成包文件
     */
    public static void makePackageFile(debProjectModel project,String compileCmd,String debresourcedir,String debresultpath) throws Exception
    {
        String makecmd = compileCmd.replace("(source)",debresourcedir).replace("(dest)",debresultpath).replace("(output)",project.resultDir).replace("(pkgtype)",project.packageMakerType).replace("(pkgname)",project.packageName);
        Process pro = JRunHelper.runSysCmd(makecmd, false);
        pro.waitFor();
        InputStream is = pro.getErrorStream();
        String[] error = JDataHelper.readFromInputStream(is);
        is.close();
        String errorprint="";
        if (error != null && error.length > 0)
        {
            for(int k=0;k<error.length;k++)
            {
                errorprint+=error[k];
            }
            if (errorprint.toLowerCase().contains("error") || errorprint.contains("错误"))
            {
               throw new Exception(errorprint);
            }
        }

    }

}
