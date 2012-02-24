package debProjectTool;

import debProjectModels.*;
import jAppHelper.*;
import wox.serial.*;

/**
 * Deb工程文件读写类
 * User: wcss
 * Date: 12-2-10
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class debProjectModelRW {
    /**
     * 序列化对象
     */
    public static Easy quickEasy = new Easy();

    public static boolean saveProject(debProjectModel project, String destfile) {
        try {
            quickEasy.savetofile(project, destfile);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static debProjectModel loadProject(String sourcefile) {
        try {
            return (debProjectModel) quickEasy.loadfromfile(sourcefile);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
