package debProjectTool;

import debProjectModels.debDependsModel;
import debProjectModels.debFilesModel;
import debProjectModels.debProjectModel;
import debProjectModels.debStartupModel;
import jAppHelper.jCmdRunHelper;
import jAppHelper.jDataRWHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-3-24
 * Time: 下午12:10
 * To change this template use File | Settings | File Templates.
 */
public class ypkProjectCompile
{
    /**
     * 创建YPK包说明文件
     *
     * @return 执行结果
     */
    public static boolean buildPackageInfo(debProjectModel project, String projectbasedirp) throws Exception {
        File f = new File(projectbasedirp);
        if (f.exists()) {
            ArrayList controlcontent = new ArrayList();
            controlcontent.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            controlcontent.add("<PackageInfo>");
            controlcontent.add(" <Package name=\"" + project.packageName + "\">");
            controlcontent.add("   <genericname>");
            controlcontent.add("     <keyword lang=\"en\">" + project.packageName + "</keyword>");
            controlcontent.add("   </genericname>");
            controlcontent.add("   <category>" + project.packageSection + "</category>");
            controlcontent.add("   <arch>" + project.packageArchitecture + "</arch>");
            controlcontent.add("   <version>" + project.packageVersion + "</version>");
            controlcontent.add("   <priority>" + project.packagePriority + "</priority>");
            controlcontent.add("   <license>" + project.packageLicense + "</license>");
            controlcontent.add("   <packager>" + project.packageMaintainer + "</packager>");
            controlcontent.add("   <homepage>" + project.packageHomepage + "</homepage>");
            controlcontent.add("   <repo>" + project.packageRepo + "</repo>");
            controlcontent.add("   <install>" + project.packageName + ".install</install>");
            controlcontent.add("   <build_date></build_date>");
            controlcontent.add("   <uri></uri>");
            controlcontent.add("   <description>");
            controlcontent.add("     <keyword lang=\"en\">" + project.packageDescription + "</keyword>");
            controlcontent.add("   </description>");
            controlcontent.add("   <data_count>1</data_count>");
            controlcontent.add("   <data id=\"0\">");
            controlcontent.add("     <name>pkgdata</name>");
            controlcontent.add("     <format>xz</format>");
            controlcontent.add("     <size></size>");
            controlcontent.add("     <install_size></install_size>");
            controlcontent.add("     <depend>" + buildRequiresStr(project) + "</depend>");
            controlcontent.add("     <bdepend></bdepend>");
            controlcontent.add("     <recommended></recommended>");
            controlcontent.add("     <conflict></conflict>");
            controlcontent.add("   </data>");
            controlcontent.add(" </Package>");
            controlcontent.add("</PackageInfo>");

            jDataRWHelper.writeAllLines(projectbasedirp + "/" + "YLMFOS/control.xml", jDataRWHelper.convertTo(controlcontent.toArray()));

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
     * 添加到文件尾
     * @param project
     * @param projectbasedirape
     */
    public static void appendToConfigFileEnd(debProjectModel project,String projectbasedirape,String content) throws Exception {
        jDataRWHelper.appendLineToFileEnd(projectbasedirape + "/" + "YLMFOS/" + project.packageName + ".install", content);
    }

    /**
     * 创建安装后脚本
     *
     * @return 执行结果
     */
    public static boolean buildInstallScript(debProjectModel project, String projectbasediris) throws Exception
    {

        //写安装脚本头部
        ArrayList<String> scripthead = new ArrayList<String>();
        scripthead.add(". /usr/lib/ybs/funcs");
        scripthead.add("\n");
        scripthead.add("dbfile=\"/var/ypkg/db/package.db\"");
        scripthead.add("sqlfile=\"/usr/share/ypkg/db_create.sql\"");
        scripthead.add("sqlite3=\"$(which sqlite3)\"");
        scripthead.add("\n");
        jDataRWHelper.writeAllLines(projectbasediris + "/" + "YLMFOS/" + project.packageName + ".install",jDataRWHelper.convertTo(scripthead.toArray()));

        if (project.packagePostInstFile != null) {
            File fi = new File(project.packagePostInstFile);
            if (fi.exists())
            {
                String[] postinitstrs = jDataRWHelper.readAllLines(project.packagePostInstFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"post_install() {");
                for(String str:postinitstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
                appendToConfigFileEnd(project,projectbasediris,"}");
            }
        }

        if (project.packagePostRmFile != null) {
            File fr = new File(project.packagePostRmFile);
            if (fr.exists()) {
                String[] postrmstrs = jDataRWHelper.readAllLines(project.packagePostRmFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"post_remove() {");
                for(String str:postrmstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
                appendToConfigFileEnd(project,projectbasediris,"}");
            }
        }

        if (project.packagePreInstFile != null) {
            File fr = new File(project.packagePreInstFile);
            if (fr.exists()) {
                String[] preinststrs = jDataRWHelper.readAllLines(project.packagePreInstFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"pre_install() {");
                for(String str:preinststrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
                appendToConfigFileEnd(project,projectbasediris,"}");
            }
        }

        if (project.packagePreRmFile != null) {
            File fr = new File(project.packagePreRmFile);
            if (fr.exists()) {
                String[] prermstrs = jDataRWHelper.readAllLines(project.packagePreRmFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"pre_remove() {");
                for(String str:prermstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
                appendToConfigFileEnd(project,projectbasediris,"}");
            }
        }

        if (project.packagePostUpgradeFile != null) {
            File fr = new File(project.packagePostUpgradeFile);
            if (fr.exists()) {
                String[] prermstrs = jDataRWHelper.readAllLines(project.packagePostUpgradeFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"post_upgrade() {");
                for(String str:prermstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
                appendToConfigFileEnd(project,projectbasediris,"}");
            }
        }

        if (project.packagePostDowngradeFile != null) {
            File fr = new File(project.packagePostDowngradeFile);
            if (fr.exists()) {
                String[] prermstrs = jDataRWHelper.readAllLines(project.packagePostDowngradeFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"post_downgrade() {");
                for(String str:prermstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
                appendToConfigFileEnd(project,projectbasediris,"}");
            }
        }

        if (project.packagePreUpgradeFile != null) {
            File fr = new File(project.packagePreUpgradeFile);
            if (fr.exists()) {
                String[] prermstrs = jDataRWHelper.readAllLines(project.packagePreUpgradeFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"pre_upgrade() {");
                for(String str:prermstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
                appendToConfigFileEnd(project,projectbasediris,"}");
            }
        }

        if (project.packagePreDowngradeFile != null) {
            File fr = new File(project.packagePreDowngradeFile);
            if (fr.exists()) {
                String[] prermstrs = jDataRWHelper.readAllLines(project.packagePreDowngradeFile);
                appendToConfigFileEnd(project,projectbasediris,"\n");
                appendToConfigFileEnd(project,projectbasediris,"pre_downgrade() {");
                for(String str:prermstrs)
                {
                    appendToConfigFileEnd(project,projectbasediris,str);
                }
                appendToConfigFileEnd(project,projectbasediris,"}");
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
     * 创建YPK包初始化文件
     * @param project
     * @param projectbasediryi
     * @return
     */
    public static boolean buildYPKInitFile(debProjectModel project,String projectbasediryi) throws Exception {
        ArrayList<String> initfile = new ArrayList<String>();
        initfile.add("cd " + projectbasediryi);
        initfile.add("ypkg-gencontrol");
        jDataRWHelper.writeAllLines(jCmdRunHelper.getCmdRunScriptBufferDir() + "/ypkinit.sh",jDataRWHelper.convertTo(initfile.toArray()));
        jCmdRunHelper.runSysCmd("chmod +x " + jCmdRunHelper.getCmdRunScriptBufferDir() + "/ypkinit.sh");
        jCmdRunHelper.runSysCmd(jCmdRunHelper.getCmdRunScriptBufferDir() + "/ypkinit.sh");
        return true;
    }

    /**
     * 编译YPK包
     *
     * @return 执行结果
     */
    public static void compileYpkPackage(debProjectModel project, String bufferdir) throws Exception
    {
        buildStartupFiles(project, bufferdir);
        buildCopyInstallFiles(project, bufferdir);
        buildYPKInitFile(project,bufferdir);

        buildPackageInfo(project, bufferdir);
        buildInstallScript(project, bufferdir);
    }

}