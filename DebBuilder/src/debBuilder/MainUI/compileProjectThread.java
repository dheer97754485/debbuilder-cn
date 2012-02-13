package debBuilder.MainUI;

import debProjectModels.*;
import debProjectTool.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: wcss
 * Date: 12-2-11
 * Time: 下午9:47
 * To change this template use File | Settings | File Templates.
 */
public class compileProjectThread implements Runnable
{
    private File projectFile;
    private String compileBufferDir;
    private String compileResultFile;
    public boolean makeFinish;
    public boolean haveError;
    public String makeResult;
    public compileProjectThread(File project,String bufferdir,String debfilepath)
    {
        this.projectFile = project;
        this.compileBufferDir = bufferdir;
        this.compileResultFile = debfilepath;
        makeFinish = false;
    }

    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
        try
        {
            debProjectModel dpm = debProjectModelRW.loadProject(this.projectFile.getAbsolutePath());
            System.out.println("缓冲目录：" + this.compileBufferDir);
            System.out.println("Deb文件：" + this.compileResultFile);
            debProjectCompile.compileProjectWithMakeDeb(dpm,this.compileBufferDir,this.compileResultFile);
            this.makeFinish = true;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            this.makeFinish = true;
            this.haveError = true;
            e.printStackTrace();
            this.makeResult = e.toString();
        }
    }
}
