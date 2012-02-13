package debBuilder.MainUI;

import debProjectModels.*;
import debProjectTool.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class makeInstallPackage extends JDialog implements Runnable{
    private JPanel contentPane;
    private JButton btnClose;
    private JLabel lblWelcome;
    private JProgressBar pbMakeBar;
    private JTextArea textStatus;
    private debProjectModel projectModel;
    private File projectFile;
    private String compileBufferDir;
    private String compileResultFile;
    private Boolean enableCloseApp;
    private Thread timerThread;
    private compileProjectThread compileHelper;
    private Thread makeThread;
    private boolean waitCompileFinish = false;

    public makeInstallPackage(debProjectModel project,File pFile,String bufferdir,String debfilepath,Boolean enableClose) {
        setContentPane(contentPane);
        setModal(true);
        this.projectModel = project;
        this.projectFile = pFile;
        this.compileBufferDir = bufferdir;
        this.compileResultFile = debfilepath;
        this.enableCloseApp = enableClose;
        this.lblWelcome.setText("正在生成安装包......");
        this.textStatus.setText("正在生成安装包，包名：" + project.packageName);
        this.waitCompileFinish = false;
        MainForm.setFormToCenter(this);
        this.pbMakeBar.setMaximum(250);
        this.pbMakeBar.setValue(0);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (enableCloseApp)
                {
                    System.exit(0);
                }else
                {
                    setVisible(false);
                }
            }
        });

        timerThread = new Thread(this);
        timerThread.start();

        compileHelper = new compileProjectThread(this.projectFile,this.compileBufferDir,this.compileResultFile);
        makeThread = new Thread(compileHelper);
        makeThread.start();
    }

    @Override
    public void run()
    {
        //To change body of implemented methods use File | Settings | File Templates.
        while(true)
        {
            try
            {
               Thread.sleep(1200);
                
               if (waitCompileFinish)
               {

               }else
               {
                  this.pbMakeBar.setValue(this.pbMakeBar.getValue() + 2);
                  if (this.pbMakeBar.getValue() > (this.pbMakeBar.getMaximum() - 30))
                  {
                      waitCompileFinish = true;
                  }
               }

               if (compileHelper.makeFinish)
               {
                    pbMakeBar.setValue(pbMakeBar.getMaximum());
                    if (compileHelper.haveError)
                    {
                        textStatus.setText("软件包" + this.projectModel.debPackagename + "编译失败！错误：" + compileHelper.makeResult);
                    }else
                    {
                        textStatus.setText("软件包" + this.projectModel.debPackagename + "编译完成！");
                    }
                    break;
               }
            }catch(Exception ex)
            {
                ex.printStackTrace();
                break;
            }
        }
    }
}
