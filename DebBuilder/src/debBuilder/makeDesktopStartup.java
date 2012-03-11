package debBuilder;

import debBuilder.builderConfig.configManager;
import debBuilder.language.languageManager;
import debProjectModels.debProjectModel;
import debProjectModels.debStartupModel;
import jAppHelper.jCmdRunHelper;
import jAppHelper.jDataRWHelper;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-12
 * Time: 上午1:23
 * To change this template use File | Settings | File Templates.
 */
public class makeDesktopStartup {

    public static void buildDesktopStartup(debProjectModel project,File projectfile,String iconname) throws Exception {
        //java -jar DebBuilder.jar -compile -project=/home/wcss/IDEAIC11.02.dpro -debfile=/home/wcss/创建目录/cmdlinemake.deb
        String desktoppath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "/Desktop";
        String shownames = languageManager.getShowText("100").replace("(x)",project.projectName + "@" + project.packageMakerType);
        String startupfull = desktoppath + "/" + shownames + ".desktop";
        String debpkgfile = project.resultDir + "/" + project.debPackagename;
        String jarfilepath = configManager.config.workDir + "/DebBuilder.jar";
        String cmdlines = "java -jar " + jarfilepath + " -compile -project=" + projectfile.getAbsolutePath() + " -debfile=" + debpkgfile;
        buildStartupFile(shownames,cmdlines,startupfull,iconname);

    }

    /**
     * 创建启动器文件
     * @param showname
     * @param cmdline
     * @param startupfullpath
     * @throws Exception
     */
    public static void buildStartupFile(String showname, String cmdline, String startupfullpath,String iconname) throws Exception {
        ArrayList content = new ArrayList();
        content.add("#!/usr/bin/env xdg-open");
        content.add("[Desktop Entry]");
        content.add("Name=" + showname);
        content.add("GenericName=" + showname);
        content.add("Comment=" + showname);
        content.add("Exec=" + cmdline);
        content.add("Terminal=" + "false");
        content.add("Type=" + "Application");
        content.add("StartupNotify=" + "false");
        content.add("Icon=" + iconname);
        content.add("Categories=" + "system");
        jDataRWHelper.writeAllLines(startupfullpath, jDataRWHelper.convertTo(content.toArray()));
        jCmdRunHelper.runSysCmd("chmod +x " + startupfullpath);
    }
}
