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
        String pkgtype = project.packageMakerType;
        
        String debfilepath = getDebFilePathWithoutExtName(debfilepaths) + ".deb";
        String rpmfilepath = getDebFilePathWithoutExtName(debfilepaths) + ".rpm";

        System.out.println("deb file:" + debfilepath);
        System.out.println("rpm file:" + rpmfilepath);

        checkCompilePath(debbasedirs, debfilepath);
        checkCompilePath(rpmbasedirs,rpmfilepath);

        //编译软件包
        if (pkgtype != null && pkgtype.toLowerCase().equals("deb"))
        {
            debProjectCompile.compileDebPackage(project,debbasedirs);
        }else if (pkgtype != null && pkgtype.toLowerCase().equals("rpm"))
        {
            rpmProjectCompile.compileRpmPackage(project,rpmbasedirs);
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
               jDataRWHelper.writeAllLines(jCmdRunHelper.getCmdRunScriptBufferDir() + "/rpmbuild.sh",jDataRWHelper.convertTo(al.toArray()));
               jCmdRunHelper.runSysCmd("chmod +x " + jCmdRunHelper.getCmdRunScriptBufferDir() + "/rpmbuild.sh");
               makePackageFile(project, jCmdRunHelper.getCmdRunScriptBufferDir() + "/rpmbuild.sh", rpmbasedirs, rpmfilepath);
           }
        }else
        {
            if (pkgtype != null && pkgtype.toLowerCase().equals("deb"))
            {
                makePackageFile(project,configManager.config.compileCmd,debbasedirs,debfilepath);
            }else if (pkgtype != null && pkgtype.toLowerCase().equals("rpm"))
            {
                makePackageFile(project,configManager.config.compileCmd,rpmbasedirs,rpmfilepath);
            }

        }
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
            if (errorprint.toLowerCase().contains("error") || errorprint.contains("错误"))
            {
               throw new Exception(errorprint);
            }
        }

    }

}
