package debProjectModels;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-10
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */
public class debDependsModel {
    public static final int moreThanVersion = 1;
    public static final int lessThanVersion = 2;
    public static final int allVersion = 3;

    public debDependsModel() {
    }

    public debDependsModel(String name, String version, int lessthan) {
        this.packageName = name;
        this.packageVersion = version;
        this.packageVersionType = lessthan;
    }

    /**
     * 包名
     */
    public String packageName;

    /**
     * 版本号
     */
    public String packageVersion;

    /**
     * 版本类型
     */
    public int packageVersionType;

    public String toString() {
        if (packageVersionType == allVersion) {
            return this.packageName + "(All)";
        } else
        {
            if (packageVersionType == lessThanVersion)
            {
                return this.packageName + "(<=" + packageVersion + ")";
            }else
            {
                return this.packageName + "(>=" + packageVersion + ")";
            }
        }
    }
}
