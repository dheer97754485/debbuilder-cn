package debProjectTool;

import debProjectModels.*;
import jAppHelper.*;

import java.awt.datatransfer.FlavorEvent;
import java.io.*;
import java.util.*;

/**
 * deb工程编译类
 * User: wcss
 * Date: 12-2-10
 * Time: 上午10:37
 * To change this template use File | Settings | File Templates.
 */
public class debProjectCompile
{
    /**
     * 创建Deb包说明文件
     *
     * @return 执行结果
     */
    public static boolean buildPackageInfo(debProjectModel project, String projectbasedirp) throws Exception {
        File f = new File(projectbasedirp);
        if (f.exists()) {
            File controlfile = new File(projectbasedirp + "/DEBIAN");
            controlfile.mkdirs();
            ArrayList controlcontent = new ArrayList();
            controlcontent.add("Package: " + project.packageName);
            controlcontent.add("Version: " + project.packageVersion);
            controlcontent.add("Architecture: " + project.packageArchitecture);
            controlcontent.add("Maintainer: " + project.packageMaintainer);
            //controlcontent.add("Original-Maintainer: " + project.packageOriginalMaintainer);
            controlcontent.add("Installed-Size: " + project.packageInstalledSize);
            controlcontent.add("Depends: " + buildDependsStr(project));
            controlcontent.add("Section: " + project.packageSection);
            controlcontent.add("Priority: " + project.packagePriority);
            controlcontent.add("Description: " + project.packageDescription);
            controlcontent.add("Homepage: " + project.packageHomepage);

            jDataRWHelper.writeAllLines(projectbasedirp + "/DEBIAN/control", jDataRWHelper.convertTo(controlcontent.toArray()));

            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建依赖软件包字符串
     *
     * @return 执行结果
     */
    public static String buildDependsStr(debProjectModel project) {
        ArrayList data = project.packageDepends;
        if (data != null && data.size() > 0) {
            String result = "";
            debDependsModel ddm = null;
            for (int k = 0; k < data.size(); k++) {
                ddm = (debDependsModel) data.get(k);
                if (ddm.packageVersionType == debDependsModel.allVersion) {
                    result += ddm.packageName + ",";
                } else {
                    if (ddm.packageVersionType == debDependsModel.lessThanVersion) {
                        result += ddm.packageName + "(<= " + ddm.packageVersion + ")" + ",";
                    } else {
                        result += ddm.packageName + "(>= " + ddm.packageVersion + ")" + ",";
                    }
                }
            }
            result = result.substring(0, result.length() - 1);
            return result;
        } else {
            return "";
        }
    }

    /**
     * 创建安装后脚本
     *
     * @return 执行结果
     */
    public static boolean buildInstallScript(debProjectModel project, String projectbasediris) throws Exception {
        if (project.packagePostInstFile != null) {
            File fi = new File(project.packagePostInstFile);
            if (fi.exists()) {
                jCmdRunHelper.runSysCmd("cp " + project.packagePostInstFile + " " + projectbasediris + "/DEBIAN/postinst", true);
                jCmdRunHelper.runSysCmd("chmod +x " + projectbasediris + "/DEBIAN/postinst", true);
            }
        }

        if (project.packagePostRmFile != null) {
            File fr = new File(project.packagePostRmFile);
            if (fr.exists()) {
                jCmdRunHelper.runSysCmd("cp " + project.packagePostRmFile + " " + projectbasediris + "/DEBIAN/postrm", true);
                jCmdRunHelper.runSysCmd("chmod +x " + projectbasediris + "/DEBIAN/postrm");
            }
        }

        if (project.packagePreInstFile != null) {
            File fr = new File(project.packagePreInstFile);
            if (fr.exists()) {
                jCmdRunHelper.runSysCmd("cp " + project.packagePreInstFile + " " + projectbasediris + "/DEBIAN/preinst", true);
                jCmdRunHelper.runSysCmd("chmod +x " + projectbasediris + "/DEBIAN/preinst");
            }
        }

        if (project.packagePreRmFile != null) {
            File fr = new File(project.packagePreRmFile);
            if (fr.exists()) {
                jCmdRunHelper.runSysCmd("cp " + project.packagePreRmFile + " " + projectbasediris + "/DEBIAN/prerm", true);
                jCmdRunHelper.runSysCmd("chmod +x " + projectbasediris + "/DEBIAN/prerm");
            }
        }

        return true;
    }

    /**
     * 创建菜单启动器
     *
     * @return 执行结果
     */
    public static boolean buildStartupFiles(debProjectModel project, String projectbasedirs) throws Exception {
        if (project.packageStartupList != null && project.packageStartupList.size() > 0) {
            debStartupModel dsm = null;
            for (int k = 0; k < project.packageStartupList.size(); k++) {
                dsm = (debStartupModel) project.packageStartupList.get(k);
                buildStartupFile(project,dsm, projectbasedirs);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建启动器文件
     *
     * @param startup
     * @param projectbasedirss
     * @return
     * @throws Exception
     */
    public static boolean buildStartupFile(debProjectModel project,debStartupModel startup, String projectbasedirss) throws Exception {
        File f = new File(projectbasedirss);
        if (f.exists()) {
            ArrayList content = new ArrayList();
            File iconsource = new File(startup.iconSourceFile);
            if (iconsource.exists())
            {
              String iconname = iconsource.getName();
              if (iconname.contains("."))
              {
                 iconname = iconname.substring(0,iconname.indexOf("."));
              }
              content.add("#!/usr/bin/env xdg-open");
              content.add("[Desktop Entry]");
              content.add("Name=" + startup.startupName);
              content.add("GenericName=" + startup.startupGenericName);
              content.add("Comment=" + startup.startupComment);
              content.add("Exec=" + startup.startupExec);
              content.add("Terminal=" + startup.startupTerminal);
              content.add("Type=" + startup.startupType);
              content.add("StartupNotify=" + startup.startupNotify);
              content.add("Icon=" + iconname);
              content.add("Categories=" + startup.startupCategories);
              File startupfile = new File(projectbasedirss + "/usr/share/applications/");
              startupfile.mkdirs();
              File startupicon = new File(projectbasedirss + "/usr/share/icons/");
              startupicon.mkdirs();
              jCmdRunHelper.runSysCmd("cp " + startup.iconSourceFile + " " + projectbasedirss + "/usr/share/icons/" + iconsource.getName());
              jDataRWHelper.writeAllLines(projectbasedirss + "/usr/share/applications/" + startup.startupFileName + ".desktop", jDataRWHelper.convertTo(content.toArray()));
              jCmdRunHelper.runSysCmd("chmod +x " + projectbasedirss + "/usr/share/applications/" + startup.startupFileName + ".desktop");
              return true;

            }else
            {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * 复制安装文件到临时目录
     *
     * @return 执行结果
     */
    public static boolean buildCopyInstallFiles(debProjectModel project, String projectbasedircc) throws Exception {
        if (project.packageFiles != null && project.packageFiles.size() > 0) {
            debFilesModel dfm = null;
            File ff = null;
            ArrayList content = new ArrayList();
            for (int k = 0; k < project.packageFiles.size(); k++)
            {
                dfm = (debFilesModel) project.packageFiles.get(k);
                if (dfm.copyType == debFilesModel.copyFile)
                {
                  ff = new File(new File(projectbasedircc + dfm.destPath).getParent());
                  ff.mkdirs();
                  content.add("cp " + dfm.sourcePath + " " + projectbasedircc + dfm.destPath);
                }else
                {
                  ff = new File(projectbasedircc + dfm.destPath);
                  ff.mkdirs();
                  content.add("cp -a " + dfm.sourcePath + "/. " + projectbasedircc + dfm.destPath);
                }
            }
            jDataRWHelper.writeAllLines(jCmdRunHelper.getCmdRunScriptBufferDir() + "/copyinstallfile_" + project.packageName + ".sh", jDataRWHelper.convertTo(content.toArray()));
            jCmdRunHelper.runSysCmd("chmod +x " + jCmdRunHelper.getCmdRunScriptBufferDir() + "/copyinstallfile_" + project.packageName + ".sh");
            jCmdRunHelper.runSysCmd(jCmdRunHelper.getCmdRunScriptBufferDir() + "/copyinstallfile_" + project.packageName + ".sh", true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 编译deb包
     *
     * @return 执行结果
     */
    public static void compileProject(debProjectModel project, String bufferdir,String debfilepath,Boolean makedebpkg) throws Exception {
        String projectbasedir = "";
        if (bufferdir.endsWith("/")) {
            projectbasedir = bufferdir.substring(0, bufferdir.length() - 1);
        } else {
            projectbasedir = bufferdir;
        }
        File basedir = new File(projectbasedir);
        basedir.mkdirs();
        File debpath = new File(new File(debfilepath).getParent());
        debpath.mkdirs();
        buildPackageInfo(project, projectbasedir);
        buildInstallScript(project, projectbasedir);
        buildStartupFiles(project, projectbasedir);
        buildCopyInstallFiles(project, projectbasedir);
        if (makedebpkg)
        {
            makeDebPackageFile(projectbasedir, debfilepath);
        }

    }

    /**
     * 编译Deb包(不生成Deb包)
     * @param project
     * @param bufferdir
     */
    public static void compileProjectWithoutMakeDeb(debProjectModel project,String bufferdir) throws Exception
    {
        compileProject(project,bufferdir,project.resultDir + "/" + project.debPackagename,false);
    }

    /**
     * 生成Deb包自定义包路径
     * @param project
     * @param bufferdir
     */
    public static void compileProjectWithMakeDeb(debProjectModel project,String bufferdir,String debfile) throws Exception
    {
       compileProject(project,bufferdir,debfile,true);
    }

    /**
     * 生成Deb包使用默认路径
     * @param project
     * @param bufferdir
     * @throws Exception
     */
    public static void compileProjectWithMakeDeb(debProjectModel project,String bufferdir) throws Exception {
        compileProjectWithMakeDeb(project, bufferdir, project.resultDir + "/" + project.debPackagename);
    }

    /**
     * 生成Deb文件
     */
    public static void makeDebPackageFile(String debresourcedir,String debresultpath) throws Exception
    {
       Process pro = jCmdRunHelper.runSysCmd("dpkg -b " + debresourcedir + " " + debresultpath,false);
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
