package debProjectTool;

import debBuilder.builderConfig.configManager;
import debProjectModels.debProjectModel;
import jAppHelper.jCmdRunHelper;
import jAppHelper.jDataRWHelper;

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
        projectbasedir = projectbasedir + "_" + pkgtypename + "_time_" +(new Date().getTime());
        return projectbasedir;
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
        
        String debfilepath = debfilepaths + ".deb";
        String rpmfilepath = debfilepaths + ".rpm";

        checkCompilePath(debbasedirs,debfilepath);
        checkCompilePath(rpmbasedirs,rpmfilepath);

        String pkgtype = project.packageMakerType;

        if (pkgtype != null && pkgtype.toLowerCase().equals("deb"))
        {
            debProjectCompile.compileDebPackage(project,debbasedirs);
        }else if (pkgtype != null && pkgtype.toLowerCase().equals("rpm"))
        {
            rpmProjectCompile.compileRpmPackage(project,rpmbasedirs);
        }

//        buildPackageInfo(project, projectbasedir);
//        buildInstallScript(project, projectbasedir);
//        buildStartupFiles(project, projectbasedir);
//        buildCopyInstallFiles(project, projectbasedir);
//        if (makedebpkg)
//        {
//            makeDebPackageFile(projectbasedir, debfilepath);
//        }

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
    public static void makePackageFile(String debresourcedir,String debresultpath) throws Exception
    {
        String makecmd = configManager.config.compileCmd.replace("(source)",debresourcedir).replace("(dest)",debresultpath);
        Process pro = jCmdRunHelper.runSysCmd(makecmd, false);
        pro.waitFor();
        InputStream is = pro.getErrorStream();
        String[] error = jDataRWHelper.readFromInputStream(is);
        is.close();
        String errorprint="";
        if (error != null && error.length > 0)
        {
            for(int k=0;k<error.length;k++)
            {
                errorprint+=error[k];
            }
            throw new Exception(errorprint);
        }

    }

}
