package debBuilder.builderConfig;

import debProjectTool.*;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-25
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class configManager
{
    public static configModel config = new configModel();

    public static void loadConfig()
    {
        if (new File("/etc/builderconfig.cfg").exists())
        {
            try {
                config = (configModel)debProjectModelRW.quickEasy.loadfromfile("/etc/builderconfig.cfg");
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        saveConfig();
    }
    public static void saveConfig()
    {
        try {
            debProjectModelRW.quickEasy.savetofile(config, jAppHelper.jCmdRunHelper.getUserHomeDirPath() + "/builderconfig.cfg");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
