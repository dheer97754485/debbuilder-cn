package debProjectModels;

import java.util.ArrayList;

/**
 * Deb包制作工具工程记录模块
 * User: wcss
 * Date: 12-2-10
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */
public class debProjectModel {

    /**
     * 工程名
     */
    public String projectName;

    /**
     * 编译结果目录
     */
    public String resultDir;

    /**
     * 生成结果文件名
     */
    public String debPackagename;

    /**
     * 软件包生成器类型
     */
    public String packageMakerType = "deb";

    /**
     * 包名
     */
    public String packageName;

    /**
     * 版本
     */
    public String packageVersion;

    /**
     * 目标机架构
     */
    public String packageArchitecture;

    /**
     * 许可证
     */
    public String packageLicense;

    /**
     * 维护者
     */
    public String packageMaintainer;

    /**
     * 原维护者
     */
    public String packageOriginalMaintainer;

    /**
     * 安装后大小
     */
    public String packageInstalledSize;

    /**
     * 依赖软件包
     */
    public ArrayList packageDepends = new ArrayList();

    /**
     * 包分类
     */
    public String packageSection;

    /**
     * 优先级
     */
    public String packagePriority;

    /**
     * 包描述
     */
    public String packageDescription;

    /**
     * 软件主页
     */
    public String packageHomepage;

    /**
     * 软件包类型(stable和testing)
     */
    public String packageRepo;

    /**
     * 文件列表
     */
    public ArrayList packageFiles = new ArrayList();

    /**
     * 该脚本的主要任务是完成安装包时的配置工作
     */
    public String packagePostInstFile;

    /**
     * 该脚本负责修改软件包链接或文件关联，或删除由它创建的文件
     */
    public String packagePostRmFile;

    /**
     * 在Deb包文件解包之前，将会运行该脚本。许多“preinst”脚本的任务是停止作用于待升级软件包的服务，直到软件包安装或升级完成
     */
    public String packagePreInstFile;

    /**
     * 该脚本负责停止与软件包相关联的daemon服务。它在删除软件包关联文件之前执行
     */
    public String packagePreRmFile;

    /**
     * 更新前执行
     */
    public String packagePreUpgradeFile;

    /**
     * 更新后执行
     */
    public String packagePostUpgradeFile;

    /**
     *　删除更新之前
     */
    public String packagePreDowngradeFile;

    /**
     *  删除更新之后
     */
    public String packagePostDowngradeFile;

    /**
     * 启动器列表
     */
    public ArrayList packageStartupList = new ArrayList();

}
