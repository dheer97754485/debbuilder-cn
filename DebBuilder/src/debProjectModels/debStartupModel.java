package debProjectModels;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-10
 * Time: 上午10:42
 * To change this template use File | Settings | File Templates.
 */
public class debStartupModel
{

    public debStartupModel()
    {
        this.isDesktopStartup = false;
        this.isStartMenuStartup = false;
        this.startupNotify = true;
        this.startupTerminal = false;
        this.startupIcon = "";
    }
    /**
     * 启动器文件名
     */
    public String startupFileName ="";
    /**
     * 版本
     */
    public String startupVersion = "";

    /**
     * 类型
     */
    public String startupType = "";

    /**
     * 用途
     */
    public String startupGenericName = "";

    /**
     * 介绍
     */
    public String startupComment = "";

    /**
     * 是否需要在终端运行
     */
    public Boolean startupTerminal = false;

    /**
     * 命令
     */
    public String startupExec = "";

    /**
     * 名称
     */
    public String startupName = "";

    /**
     * 图标
     */
    public String startupIcon = "";

    /**
     * 允许开启托盘图标
     */
    public Boolean startupNotify = false;

    /**
     * 分类
     */
    public String startupCategories = "";

    /**
     * 桌面启动器
     */
    public boolean isDesktopStartup = false;

    /**
     * 菜单启动器
     */
    public boolean isStartMenuStartup = true;

    /**
     * 图标源文件
     */
    public String iconSourceFile = "";

    public String toString()
    {
        return startupName;
    }

}
