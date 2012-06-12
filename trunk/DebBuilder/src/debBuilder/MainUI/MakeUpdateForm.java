package debBuilder.MainUI;

import debBuilder.language.languageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import JAppToolKit.*;

public class MakeUpdateForm extends JDialog {
    private JPanel contentPane;
    private JButton btnMake;
    private JButton btnClose;
    private JTextField textUIndex;
    private JTextField textScript;
    private JTextField textFiles;
    private JButton btnSelect;
    private JLabel txt127;
    private JLabel txt128;
    private JLabel txt129;
    private JLabel txt130;
    private JTextField textRemoteBefore;
    private JLabel txt136;
    private JTextField textLocalBefore;
    private JLabel txt137;
    private JFileChooser fc = new JFileChooser();
    private int flag;

    public MakeUpdateForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnMake);
        txt127.setText(languageManager.getShowText("127"));
        txt128.setText(languageManager.getShowText("128"));
        txt129.setText(languageManager.getShowText("129"));
        txt130.setText(languageManager.getShowText("130"));
        btnSelect.setText(languageManager.getShowText("131"));
        btnMake.setText(languageManager.getShowText("132"));
        btnClose.setText(languageManager.getShowText("133"));

        btnMake.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textFiles.setText(selectSaveProjectFor(languageManager.getShowText("135")));
            }
        });
    }

    /**
     * 获取目录列表
     * @param remotedir
     * @param basedir
     * @param currentdir
     * @return
     * @throws Exception
     */
    public static ArrayList<String> getPathList(String remotedir,String localdir,String basedir,String currentdir) throws Exception
    {
        ArrayList<String> al = new ArrayList<String>();
        try {
            File f = new File(currentdir);
            if (f.isDirectory()) {
                File[] fList = f.listFiles();
                for (int j = 0; j < fList.length; j++) {
                    if (fList[j].isDirectory())
                    {
                        al.addAll(getPathList(remotedir,localdir,basedir,fList[j].getAbsolutePath())); // 在getDir函数里面又调用了getDir函数本身
                    }
                }
                for (int j = 0; j < fList.length; j++) {

                    if (fList[j].isFile())
                    {
                        if (fList[j].getAbsolutePath().endsWith("~"))
                        {
                           //忽视临时文件
                        }else
                        {
                           String spath = fList[j].getAbsolutePath().replace(basedir,"");
                           al.add(remotedir + "/" + spath + "," + localdir + "/" + spath);
                        }
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("Error： " + e);
        }
        return al;
    }

    /**
     * 保存工程文件
     */
    private String selectSaveProjectFor(String title) //保存文件
    {
        String fileName;

        //设置保存文件对话框的标题
        fc.setDialogTitle(title);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileFilter(projectFilter);

        //这里将显示保存文件的对话框
        try {
            flag = fc.showSaveDialog(null);
        } catch (HeadlessException he) {
            System.out.println("Save File Dialog ERROR!");
        }

        //如果按下确定按钮，则获得该文件。
        if (flag == JFileChooser.APPROVE_OPTION) {
            //获得你输入要保存的文件
            return fc.getSelectedFile().getAbsolutePath();
        }
        return "";
    }

    private void onOK() {
        // add your code here

        try
        {
            //生成更新列表文件
            ArrayList<String> list = new ArrayList<String>();
            list.add(this.textRemoteBefore.getText() + "/" + "update1.xml");
            JAppToolKit.JDataHelper.writeAllLines(MainForm.currentProjectFile.getParent() + "/" + "updatelist.xml",JAppToolKit.JDataHelper.convertTo(list.toArray()));

            //生成更新补丁配置文件
            ArrayList<String> item = new ArrayList<String>();
            item.add("[[Base]]");
            item.add("versionindex=" + this.textUIndex.getText());
            item.add("updatelogurl=http://192.168.0.1/log.txt");
            item.add("finishscriptpath=" + this.textScript.getText());
            item.add("[[Files]]");
            item.addAll(getPathList(this.textRemoteBefore.getText(),this.textLocalBefore.getText(),this.textFiles.getText(),this.textFiles.getText()));
            JAppToolKit.JDataHelper.writeAllLines(MainForm.currentProjectFile.getParent() + "/" + "update1.xml",JAppToolKit.JDataHelper.convertTo(item.toArray()));
            JOptionPane.showMessageDialog(null,languageManager.getShowText("138"));
            dispose();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
