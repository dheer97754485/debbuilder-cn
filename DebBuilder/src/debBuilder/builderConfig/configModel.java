package debBuilder.builderConfig;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-25
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
public class configModel
{
    public configModel()
    {
        this.languageName = "zh-cn.lang";
        this.compileType = "deb";
        this.workDir = "/opt/debBuilderApp";
        this.compileCmd = "dpkg -b " + "(source)" + " " + "(dest)";
    }
    public configModel(String language,String type,String dir,String cmd)
    {
        this.languageName = language;
        this.compileType = type;
        this.workDir = dir;
        this.compileCmd = cmd;
    }
    
    public String languageName = "";
    public String compileType = "";
    public String workDir = "";
    public String compileCmd = "";
}
