package debBuilder;

import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-4-21
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
public class DListModel extends DefaultListModel
{
    public Object[] data = null;
    
    public DListModel(Object[] datas)
    {
        this.data = datas;
        for(int k =0; k < this.data.length;k++)
        {
            this.add(k,this.data[k]);
        }
    }
}
