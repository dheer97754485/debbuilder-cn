package debBuilder.language;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-22
 * Time: 下午12:59
 * To change this template use File | Settings | File Templates.
 */
public class languageModel
{
    public languageModel(){}
    public languageModel(String id,String text)
    {
       this.languageId = id;
       this.showText = text;
    }
    public String languageId = "";
    public String showText = "";
}
