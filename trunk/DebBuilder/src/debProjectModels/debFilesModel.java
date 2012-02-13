package debProjectModels;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-10
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
public class debFilesModel 
{
    public static final int copyFile = 10;
    public static final int copyDir = 20;
    public debFilesModel() {
    }

    public debFilesModel(String source, String dest, int type) {
        this.sourcePath = source;
        this.destPath = dest;
        this.copyType = type;
    }

    /**
     * 源文件位置
     */
    public String sourcePath = "";

    /**
     * 目标文件位置
     */
    public String destPath = "";

    /**
     * 操作说明
     */
    public String copyInfo = "";

    /**
     * 操作类型
     */
    public int copyType = 0;
    
    public String toString()
    {
        if (destPath != null)
        {
           File f = new File(destPath);
           if (copyType == debFilesModel.copyFile)
           {
               return f.getName() + "(文件)";
           }else
           {
               return f.getName() + "(目录)";
           }
        }else
        {
            return "未知路径";
        }
    }

}
