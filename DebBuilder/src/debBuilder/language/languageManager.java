package debBuilder.language;

import debProjectTool.*;
import java.util.*;
import jAppHelper.*;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-22
 * Time: 下午1:02
 * To change this template use File | Settings | File Templates.
 */
public class languageManager
{
    public static ArrayList<languageModel> languageData = new ArrayList<languageModel>();
    public static final String languageSplit = "#";

    /**
     * 载入语言文件
     * @param loadpaths
     */
    public static void loadLanguageFile(String loadpaths) throws Exception {
        String[] texts = jDataRWHelper.readAllLines(loadpaths);
        languageData.clear();
        for(int k=0;k < texts.length;k++)
        {
            String[] splits = texts[k].split(languageSplit);
            languageData.add(new languageModel(splits[0],splits[1]));
        }
    }

    /**
     * 保存语言文件
     * @param savepaths
     * @throws Exception
     */
    public static void saveLanguageFile(String savepaths) throws Exception {
       String[] texts = new String[languageData.size()];
       for (int k =0;k < languageData.size();k++)
       {
          texts[k] = languageData.get(k).languageId + languageSplit + languageData.get(k).showText;
       }
       jDataRWHelper.writeAllLines(savepaths,texts);
    }

    /**
     * 获取显示文本
     * @param languageid
     * @return
     */
    public static String getShowText(String languageid)
    {
       String returns = "";
       for(int k = 0; k < languageData.size();k++)
       {
           if (languageData.get(k).languageId.equals(languageid))
           {
               returns = languageData.get(k).showText;
               break;
           }
       }
       return returns;
    }
}
