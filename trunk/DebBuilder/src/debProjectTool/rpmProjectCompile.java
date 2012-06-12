package debProjectTool;

import debProjectModels.*;
import java.io.*;
import java.util.*;
import JAppToolKit.*;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-3-8
 * Time: 下午10:12
 * To change this template use File | Settings | File Templates.
 */
public class rpmProjectCompile
{
    /**
     * 创建Deb包说明文件
     *
     * @return 执行结果
     */
    public static boolean buildPackageInfo(debProjectModel project, String projectbasedirp) throws Exception {
        File f = new File(projectbasedirp);
        if (f.exists()) {
            ArrayList controlcontent = new ArrayList();
            controlcontent.add("BuildRoot:%{_tmppath}/%{name}-%{version}-%{release}-root");
            controlcontent.add("Name: " + project.packageName);
            controlcontent.add("Version: " + project.packageVersion);
            controlcontent.add("Release: " + project.packageVersion);
            controlcontent.add("ExclusiveArch:" + project.packageArchitecture);
            controlcontent.add("Packager: " + project.packageMaintainer);
            //controlcontent.add("Installed-Size: " + project.packageInstalledSize);
            controlcontent.add("Requires: " + buildRequiresStr(project));
            controlcontent.add("Group: " + project.packageSection);
            //controlcontent.add("Priority: " + project.packagePriority);
            controlcontent.add("License:" + project.packageLicense);
            controlcontent.add("Summary: none");
            controlcontent.add("URL: " + project.packageHomepage);
            controlcontent.add("\n");
            controlcontent.add("%define _rpmdir ../");
            controlcontent.add("%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.%%{ARCH}.rpm");
            controlcontent.add("\n");
            controlcontent.add("%description");
            controlcontent.add(project.packageDescription + "\n");

            controlcontent.add("# Fix rpath");
            controlcontent.add("export NO_BRP_CHECK_RPATH=true");

            JDataHelper.writeAllLines(projectbasedirp + "/" + "pkgbuild.spec", JDataHelper.convertTo(controlcontent.toArray()));

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
    public static String buildRequiresStr(debProjectModel project) {
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
                        result += ddm.packageName + " <= " + ddm.packageVersion + "" + ",";
                    } else {
                        result += ddm.packageName + " >= " + ddm.packageVersion + "" + ",";
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
     * 添加到文件尾
     * @param project
     * @param projectbasedirape
     */
    public static void appendToConfigFileEnd(debProjectModel project,String projectbasedirape,String content) throws Exception {
        JDataHelper.appendLineToFileEnd(projectbasedirape + "/" + "pkgbuild.spec", content);
    }

    /**
     * 创建安装后脚本
     *
     * @return 执行结果
     */
    public static boolean buildInstallScript(debProjectModel project, String projectbasediris) throws Exception {
        if (project.packagePostInstFile != null) {
            File fi = new File(project.packagePostInstFile);
            if (fi.exists())
            {
                String[] postinitstrs = JDataHelper.readAllLines(project.packagePostInstFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"%post");
                for(String str:postinitstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
            }
        }

        if (project.packagePostRmFile != null) {
            File fr = new File(project.packagePostRmFile);
            if (fr.exists()) {
                String[] postrmstrs = JDataHelper.readAllLines(project.packagePostRmFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"%postun");
                for(String str:postrmstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
            }
        }

        if (project.packagePreInstFile != null) {
            File fr = new File(project.packagePreInstFile);
            if (fr.exists()) {
                String[] preinststrs = JDataHelper.readAllLines(project.packagePreInstFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"%pre");
                for(String str:preinststrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
            }
        }

        if (project.packagePreRmFile != null) {
            File fr = new File(project.packagePreRmFile);
            if (fr.exists()) {
                String[] prermstrs = JDataHelper.readAllLines(project.packagePreRmFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"%preun");
                for(String str:prermstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
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
                JRunHelper.runSysCmd("cp " + startup.iconSourceFile + " " + projectbasedirss + "/usr/share/icons/" + iconsource.getName());
                JDataHelper.writeAllLines(projectbasedirss + "/usr/share/applications/" + startup.startupFileName + ".desktop", JDataHelper.convertTo(content.toArray()));
                JRunHelper.runSysCmd("chmod +x " + projectbasedirss + "/usr/share/applications/" + startup.startupFileName + ".desktop");
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
            JDataHelper.writeAllLines(JRunHelper.getCmdRunScriptBufferDir() + "/copyinstallfile_" + project.packageName + ".sh", JDataHelper.convertTo(content.toArray()));
            JRunHelper.runSysCmd("chmod +x " + JRunHelper.getCmdRunScriptBufferDir() + "/copyinstallfile_" + project.packageName + ".sh");
            JRunHelper.runSysCmd(JRunHelper.getCmdRunScriptBufferDir() + "/copyinstallfile_" + project.packageName + ".sh", true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取目录列表
     * @param basedirs
     * @return
     * @throws Exception
     */
    public static ArrayList<String> getPathList(String basedirs) throws Exception
    {
        ArrayList<String> al = new ArrayList<String>();
        try {
            File f = new File(basedirs);
            if (f.isDirectory()) {
                File[] fList = f.listFiles();
                for (int j = 0; j < fList.length; j++) {
                    if (fList[j].isDirectory())
                    {
                        al.add("%dir \"" + fList[j].getAbsolutePath() + "\"");
                        al.addAll(getPathList(fList[j].getAbsolutePath())); // 在getDir函数里面又调用了getDir函数本身
                    }
                }
                for (int j = 0; j < fList.length; j++) {

                    if (fList[j].isFile())
                    {
                        //if (fList[j].getAbsolutePath().contains(".spec"))
                        //{
                        //   //.spec是配置文件不能在列表中
                        //}else
                        //{
                          al.add("\"" + fList[j].getAbsolutePath() + "\"");
                        //}
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("Error： " + e);
        }
        return al;
    }
    
    /**
     * 生成安装文件记录
     * @param project
     * @param projectbasedirrec
     * @return
     * @throws Exception
     */
    public static boolean buildInstallFilesRecords(debProjectModel project,String projectbasedirrec) throws Exception
    {
        ArrayList<String> filedatas = new ArrayList<String>();
        filedatas.add("\n");
        filedatas.add("%files");
        filedatas.add("%dir \"/\"");
        filedatas.addAll(getPathList(projectbasedirrec));
        for(Object str: filedatas.toArray())
        {
           appendToConfigFileEnd(project,projectbasedirrec,str.toString().replace(projectbasedirrec,""));
        }
        return true;
    }
    
    /**
     * 编译Rpm包
     *
     * @return 执行结果
     */
    public static void compileRpmPackage(debProjectModel project, String bufferdir) throws Exception {
        buildPackageInfo(project, bufferdir);
        buildInstallScript(project, bufferdir);
        buildStartupFiles(project, bufferdir);
        buildCopyInstallFiles(project, bufferdir);
        buildInstallFilesRecords(project,bufferdir);
    }

}
