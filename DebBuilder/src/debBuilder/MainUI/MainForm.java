package debBuilder.MainUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import debBuilder.makeDesktopStartup;
import debProjectModels.*;
import debProjectTool.*;

public class MainForm extends JDialog {
    private JPanel contentPane;
    private JLabel lblStatus;
    private JTabbedPane tabmain;
    private JPanel tabpackageinfo;
    private JButton btnNewProject;
    private JButton btnOpenProject;
    private JButton btnSaveProject;
    private JButton btnCompileProject;
    private JTextField textProjectName;
    private JTextArea textDescription;
    private JRadioButton rbAll;
    private JRadioButton rbOnlyI386;
    private JRadioButton rbOnlyAmd64;
    private JButton btn_SelectResultDir;
    private JTextField textPackageName;
    private JTextField textHomePage;
    private JTextField textVersion;
    private JTextField textArchitecture;
    private JTextField textInstalledSize;
    private JTextField textSection;
    private JTextField textMaintainer;
    //private JTextField textOriginalMaintainer;
    private JTextField textPriority;
    private JTextField textDebName;
    private JTextField textResultDir;
    private JTextField textProjectFile;
    private JRadioButton rbOnlyPowerPC;
    private JButton btnDesktopCompile;
    private JList listDepends;
    private JButton btnNewDepends;
    private JButton btnDelDepends;
    private JButton btnSaveDepends;
    private JTextField textDependName;
    private JRadioButton rbmorethan;
    private JRadioButton rblessthan;
    private JTextField textDependVersion;
    private JButton btnCancelNew;
    private JRadioButton rbAllVersion;
    private JTextField textPostInst;
    private JButton btnSelectPostInst;
    private JButton btnSelectPostRm;
    private JTextField textPreInst;
    private JTextField textPreRm;
    private JButton btnSelectPreInst;
    private JButton btnSelectPreRm;
    private JTextField textPostRm;
    private JList listStartup;
    private JButton btnNewStartup;
    private JButton btnDelStartup;
    private JButton btnSaveStartup;
    private JButton btnCancelSave;
    private JTextField textStartupName;
    private JButton btnSelectIcon;
    private JTextField textStartupFileName;
    private JTextField textStartupIcon;
    private JTextField textStartupCategories;
    private JTextField textStartupType;
    private JTextField textStartupVersion;
    private JTextField textStartupGenericName;
    private JTextField textStartupComment;
    private JTextField textStartupExec;
    private JList listFiles;
    private JButton btnNewFiles;
    private JButton btnDelFiles;
    private JButton btnSaveFiles;
    private JButton btnCancelFiles;
    private JTextField textSourcePath;
    private JButton btnSelectSourcePath;
    private JTextField textDestPath;
    private JButton btnSelectDestPath;
    private JButton buttonOK;
    private JFileChooser fc = new JFileChooser();
    private int flag;
    public static File currentProjectFile;
    public static debProjectModel currentProject;
    public static debDependsModel currentDepend;
    public static debFilesModel currentFiles;
    public static debStartupModel currentStartup;
    //private ListModel dependLists = new DefaultListModel();
    private int dependVersionType = 0;
    private static makeInstallPackage makeDialog = null;

    public MainForm() {
        setContentPane(contentPane);
        setModal(true);
        //getRootPane().setDefaultButton(buttonOK);
        clearProjectData();
        this.listDepends.setListData(new Object[]{});

        //基本信息
        rbAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                textArchitecture.setText("all");
                rbAll.setSelected(true);
                rbOnlyI386.setSelected(false);
                rbOnlyAmd64.setSelected(false);
                rbOnlyPowerPC.setSelected(false);
            }
        });
        rbOnlyI386.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                textArchitecture.setText("i386");
                rbAll.setSelected(false);
                rbOnlyI386.setSelected(true);
                rbOnlyAmd64.setSelected(false);
                rbOnlyPowerPC.setSelected(false);
            }
        });
        rbOnlyAmd64.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                textArchitecture.setText("amd64");
                rbAll.setSelected(false);
                rbOnlyI386.setSelected(false);
                rbOnlyAmd64.setSelected(true);
                rbOnlyPowerPC.setSelected(false);
            }
        });
        rbOnlyPowerPC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                textArchitecture.setText("powerpc");
                rbAll.setSelected(false);
                rbOnlyI386.setSelected(false);
                rbOnlyAmd64.setSelected(false);
                rbOnlyPowerPC.setSelected(true);
            }
        });
        btnNewProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                saveProjectFile();
            }


        });
        btn_SelectResultDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                fc.setMultiSelectionEnabled(false);
                fc.setDialogTitle("选择编译输出目录！");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //int option = ch.showOpenDialog(this);
                flag = fc.showSaveDialog(null);
                if (flag == JFileChooser.APPROVE_OPTION) {
                    String path = fc.getSelectedFile().getAbsolutePath();
                    textResultDir.setText(path);

                }
            }
        });
        btnSaveProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onsaveproject();
            }
        });

        //软件包依赖
        listDepends.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                btnSaveDepends.setEnabled(true);
                if (listDepends.getSelectedValue() != null) {
                    debDependsModel ddm = (debDependsModel) listDepends.getSelectedValue();
                    textDependName.setText(ddm.packageName);
                    textDependVersion.setText(ddm.packageVersion);
                    rbmorethan.setSelected(false);
                    rblessthan.setSelected(false);
                    rbAllVersion.setSelected(false);
                    switch (ddm.packageVersionType) {
                        case debDependsModel.moreThanVersion: {
                            rbmorethan.setSelected(true);
                        }
                        break;
                        case debDependsModel.lessThanVersion: {
                            rblessthan.setSelected(true);
                        }
                        break;
                        case debDependsModel.allVersion: {
                            rbAllVersion.setSelected(true);
                        }
                        break;
                    }
                }
            }
        });
        rbmorethan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                rbmorethan.setSelected(true);
                rblessthan.setSelected(false);
                rbAllVersion.setSelected(false);
                textDependVersion.setEnabled(true);
                textDependVersion.setEditable(true);
                dependVersionType = 1;
            }
        });
        rblessthan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                rbmorethan.setSelected(false);
                rblessthan.setSelected(true);
                rbAllVersion.setSelected(false);
                textDependVersion.setEnabled(true);
                textDependVersion.setEditable(true);
                dependVersionType = 2;
            }
        });
        btnNewDepends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainForm.currentDepend = new debDependsModel();
                clearDependEditor();
                btnNewDepends.setEnabled(false);
                btnCancelNew.setEnabled(true);
                btnSaveDepends.setEnabled(true);
            }
        });
        btnDelDepends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (listDepends.getSelectedValue() != null) {
                    MainForm.currentProject.packageDepends.remove(listDepends.getSelectedValue());
                    listDepends.setListData(MainForm.currentProject.packageDepends.toArray());
                }
            }
        });
        btnSaveDepends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (MainForm.currentDepend != null) {
                    MainForm.currentDepend.packageName = textDependName.getText();
                    MainForm.currentDepend.packageVersion = textDependVersion.getText();
                    MainForm.currentDepend.packageVersionType = dependVersionType;
                    MainForm.currentProject.packageDepends.add(MainForm.currentDepend);
                    MainForm.currentDepend = null;
                    btnNewDepends.setEnabled(true);
                    btnCancelNew.setEnabled(false);
                    listDepends.setListData(MainForm.currentProject.packageDepends.toArray());

                } else {
                    if (listDepends.getSelectedValue() != null) {
                        debDependsModel ddm = (debDependsModel) listDepends.getSelectedValue();
                        ddm.packageName = textDependName.getText();
                        ddm.packageVersion = textDependVersion.getText();
                        ddm.packageVersionType = dependVersionType;
                        int index = MainForm.currentProject.packageDepends.indexOf(ddm);
                        MainForm.currentProject.packageDepends.set(index, ddm);
                        btnNewDepends.setEnabled(true);
                        btnCancelNew.setEnabled(false);
                        listDepends.setListData(MainForm.currentProject.packageDepends.toArray());
                    }
                }
                lblStatus.setText("添加/修改软件包依赖完成");
                btnSaveDepends.setEnabled(false);
                MainForm.currentDepend = null;
            }
        });
        btnCancelNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                clearDependEditor();
                btnCancelNew.setEnabled(false);
                btnNewDepends.setEnabled(true);
                MainForm.currentDepend = null;
                btnSaveDepends.setEnabled(false);
                listDepends.setSelectedIndex(-1);
            }
        });
        rbAllVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                rbmorethan.setSelected(false);
                rblessthan.setSelected(false);
                rbAllVersion.setSelected(true);
                textDependVersion.setText("");
                textDependVersion.setEnabled(false);
                dependVersionType = 3;

            }
        });

        //安装脚本
        btnSelectPostInst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File posti = selectScriptFile("选择PostInst脚本！");
                if (posti != null) {
                    textPostInst.setText(posti.getAbsolutePath());
                }
            }
        });
        btnSelectPostRm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                File postr = selectScriptFile("选择PostRm脚本！");
                if (postr != null) {
                    textPostRm.setText(postr.getAbsolutePath());
                }
            }
        });
        btnSelectPreInst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                File prei = selectScriptFile("选择PreInst脚本！");
                if (prei != null) {
                    textPreInst.setText(prei.getAbsolutePath());
                }
            }
        });
        btnSelectPreRm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                File prer = selectScriptFile("选择PreRm脚本！");
                if (prer != null) {
                    textPreRm.setText(prer.getAbsolutePath());
                }
            }
        });

        //菜单启动器
        btnNewStartup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                MainForm.currentStartup = new debStartupModel();
                clearStartupEditor();
                btnCancelSave.setEnabled(true);
                btnNewStartup.setEnabled(false);
                btnSaveStartup.setEnabled(true);
            }
        });
        btnDelStartup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (listStartup.getSelectedValue() != null) {
                    MainForm.currentProject.packageStartupList.remove(listStartup.getSelectedValue());
                    listStartup.setListData(MainForm.currentProject.packageStartupList.toArray());
                }
            }
        });
        btnSaveStartup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                debStartupModel editor = null;
                if (MainForm.currentStartup != null) {
                    //新建
                    editor = MainForm.currentStartup;
                } else {
                    //修改
                    if (listStartup.getSelectedValue() != null) {
                        editor = (debStartupModel) listStartup.getSelectedValue();
                    }
                }
                if (editor != null) {
                    editor.startupCategories = textStartupCategories.getText();
                    editor.startupFileName = textStartupFileName.getText();
                    editor.iconSourceFile = textStartupIcon.getText();
                    editor.startupIcon = new File(editor.iconSourceFile).getName();
                    editor.startupComment = textStartupComment.getText();
                    editor.startupExec = textStartupExec.getText();
                    editor.startupGenericName = textStartupGenericName.getText();
                    editor.startupName = textStartupName.getText();
                    editor.startupType = textStartupType.getText();
                    editor.startupVersion = textStartupVersion.getText();

                    if (MainForm.currentStartup != null) {
                        //添加
                        MainForm.currentProject.packageStartupList.add(editor);
                        btnNewStartup.setEnabled(true);
                    } else {
                        int index = MainForm.currentProject.packageStartupList.indexOf(listStartup.getSelectedValue());
                        MainForm.currentProject.packageStartupList.set(index, editor);
                    }
                    listStartup.setListData(MainForm.currentProject.packageStartupList.toArray());
                }
                lblStatus.setText("添加/修改菜单启动器完成");
                btnSaveStartup.setEnabled(false);
                MainForm.currentStartup = null;
            }
        });
        btnCancelSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                btnSaveStartup.setEnabled(false);
                btnCancelSave.setEnabled(false);
                btnNewStartup.setEnabled(true);
                MainForm.currentStartup = null;
                listStartup.setSelectedIndex(-1);
                clearStartupEditor();
            }
        });
        btnSelectIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                File icon = selectScriptFile("请选择启动器图片文件！");
                if (icon != null) {
                    textStartupIcon.setText(icon.getAbsolutePath());
                }
            }
        });
        listStartup.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (listStartup.getSelectedValue() != null) {
                    debStartupModel startup = (debStartupModel) listStartup.getSelectedValue();
                    textStartupCategories.setText(startup.startupCategories);
                    textStartupFileName.setText(startup.startupFileName);
                    textStartupIcon.setText(startup.iconSourceFile);
                    textStartupComment.setText(startup.startupComment);
                    textStartupExec.setText(startup.startupExec);
                    textStartupGenericName.setText(startup.startupGenericName);
                    textStartupName.setText(startup.startupName);
                    textStartupType.setText(startup.startupType);
                    textStartupVersion.setText(startup.startupVersion);
                    btnSaveStartup.setEnabled(true);
                }
            }
        });

        //安装文件
        btnNewFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                MainForm.currentFiles = new debFilesModel();
                btnNewFiles.setEnabled(false);
                btnCancelFiles.setEnabled(true);
                btnSaveFiles.setEnabled(true);
                clearFilesEditor();
            }
        });
        btnDelFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (listFiles.getSelectedValue() != null) {
                    MainForm.currentProject.packageFiles.remove(listFiles.getSelectedValue());
                    listFiles.setListData(MainForm.currentProject.packageFiles.toArray());
                }
            }
        });
        btnSaveFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                debFilesModel dfm = null;
                if (MainForm.currentFiles != null) {
                    dfm = MainForm.currentFiles;
                } else {
                    if (listFiles.getSelectedValue() != null) {
                        dfm = (debFilesModel) listFiles.getSelectedValue();
                    }
                }

                if (dfm != null) {
                    dfm.sourcePath = textSourcePath.getText();
                    dfm.destPath = textDestPath.getText();
                    dfm.copyInfo = "dir";
                    dfm.copyType = debFilesModel.copyDir;

                    if (MainForm.currentFiles != null) {
                        MainForm.currentProject.packageFiles.add(dfm);
                    } else {
                        int index = MainForm.currentProject.packageFiles.indexOf(listFiles.getSelectedValue());
                        MainForm.currentProject.packageFiles.set(index, dfm);
                    }
                    listFiles.setListData(MainForm.currentProject.packageFiles.toArray());
                }
                lblStatus.setText("添加/修改安装目录完成！");
                btnSaveFiles.setEnabled(false);
                btnNewFiles.setEnabled(true);
                MainForm.currentFiles = null;
            }
        });
        btnCancelFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                MainForm.currentFiles = null;
                btnCancelFiles.setEnabled(false);
                btnSaveFiles.setEnabled(false);
                btnNewFiles.setEnabled(true);
                listFiles.setSelectedIndex(-1);
                clearFilesEditor();
            }
        });
        btnSelectSourcePath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                fc.setMultiSelectionEnabled(false);
                fc.setDialogTitle("选择源目录！");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //int option = ch.showOpenDialog(this);
                flag = fc.showOpenDialog(null);
                if (flag == JFileChooser.APPROVE_OPTION) {
                    String path = fc.getSelectedFile().getAbsolutePath();
                    textSourcePath.setText(path);

                }
            }
        });
        btnSelectDestPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                fc.setMultiSelectionEnabled(false);
                fc.setDialogTitle("选择目标目录！");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //int option = ch.showOpenDialog(this);
                flag = fc.showOpenDialog(null);
                if (flag == JFileChooser.APPROVE_OPTION) {
                    String path = fc.getSelectedFile().getAbsolutePath();
                    textDestPath.setText(path);

                }
            }
        });
        listFiles.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (listFiles.getSelectedValue() != null) {
                    debFilesModel dff = (debFilesModel) listFiles.getSelectedValue();
                    textSourcePath.setText(dff.sourcePath);
                    textDestPath.setText(dff.destPath);
                    btnSaveFiles.setEnabled(true);
                }
            }
        });

        //打开编译工程
        btnOpenProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                File open = selectScriptFile("选择DebBuilder工程文件!");
                if (open != null) {
                    changeEditorStatus(true, open.getAbsolutePath());
                }
            }
        });
        btnCompileProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                saveprojectdata();
                if (MainForm.currentProject.debPackagename == null || MainForm.currentProject.resultDir == null)
                {
                   JOptionPane.showMessageDialog(null,"对不起，编译文件名或编译输出目录不能为空！");
                }else if (MainForm.currentProject.debPackagename.isEmpty() || MainForm.currentProject.resultDir.isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"对不起，编译文件名或编译输出目录不能为空！");
                }else
                {
                   makeInstallPkg(MainForm.currentProject, MainForm.currentProject.resultDir + "/" + MainForm.currentProject.debPackagename, false);
                }
            }
        });
        btnDesktopCompile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                try {
                    makeDesktopStartup.buildDesktopStartup(MainForm.currentProject, MainForm.currentProjectFile, "debbuilderico");
                    JOptionPane.showMessageDialog(null,"生成成功！");
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

    /**
     * 设置窗口到屏幕中心
     *
     * @param form
     */
    public static void setFormToCenter(JDialog form) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int formx = (screenSize.width - form.getSize().width) / 2;
        int formy = (screenSize.height - form.getSize().height) / 2;
        form.setLocation(formx, formy);
    }

    /**
     * 启动生成安装包窗口
     *
     * @param project
     * @param debfile
     * @param closeapps
     */
    public static void makeInstallPkg(debProjectModel project, String debfile, boolean closeapps) {
        makeDialog = new makeInstallPackage(project, jAppHelper.jCmdRunHelper.getUserHomeDirPath() + "/debBuilderWorkSpace/" + project.packageName, debfile, closeapps);
        makeDialog.pack();
        makeDialog.setTitle("DebBuilder软件包生成器-编译进度");
        makeDialog.setSize(new Dimension(400, 240));
        MainForm.setFormToCenter(makeDialog);
        makeDialog.setVisible(true);
    }

    /**
     * 清理安装目录编辑框
     */
    private void clearFilesEditor() {
        textSourcePath.setText("");
        textDestPath.setText("");
    }

    /**
     * 清理菜单启动器编辑框
     */
    private void clearStartupEditor() {
        textStartupCategories.setText("system");
        textStartupFileName.setText("teststartup");
        textStartupIcon.setText("图标文件");
        textStartupComment.setText("简单介绍");
        textStartupExec.setText("java -jar test.jar");
        textStartupGenericName.setText("一个编辑器程序");
        textStartupName.setText("启动测试");
        textStartupType.setText("Application");
        textStartupVersion.setText("1.0");
    }

    /**
     * 清理依赖编辑框
     */
    private void clearDependEditor() {
        textDependName.setText("未知");
        textDependVersion.setText("1.0");
        textDependVersion.setEditable(false);
        rbmorethan.setSelected(false);
        rblessthan.setSelected(false);
        rbAllVersion.setSelected(true);
        dependVersionType = 3;
    }

    /**
     * 保存工程数据
     */
    private void saveprojectdata() {
        if (MainForm.currentProject == null) {
            MainForm.currentProject = new debProjectModel();
        }

        MainForm.currentProject.projectName = this.textProjectName.getText().trim();
        MainForm.currentProject.debPackagename = this.textDebName.getText().trim();
        MainForm.currentProject.resultDir = this.textResultDir.getText().trim();
        MainForm.currentProject.packageName = this.textPackageName.getText().trim();
        MainForm.currentProject.packageVersion = this.textVersion.getText().trim();
        MainForm.currentProject.packageArchitecture = this.textArchitecture.getText().trim();
        MainForm.currentProject.packageInstalledSize = this.textInstalledSize.getText().trim();
        MainForm.currentProject.packageSection = this.textSection.getText().trim();
        MainForm.currentProject.packageHomepage = this.textHomePage.getText().trim();
        MainForm.currentProject.packageDescription = this.textDescription.getText().trim();
        MainForm.currentProject.packageMaintainer = this.textMaintainer.getText().trim();
        //MainForm.currentProject.packageOriginalMaintainer = this.textOriginalMaintainer.getText().trim();
        MainForm.currentProject.packagePriority = this.textPriority.getText().trim();
        MainForm.currentProject.packagePostInstFile = textPostInst.getText();
        MainForm.currentProject.packagePostRmFile = textPostRm.getText();
        MainForm.currentProject.packagePreInstFile = textPreInst.getText();
        MainForm.currentProject.packagePreRmFile = textPreRm.getText();

        debProjectModelRW.saveProject(MainForm.currentProject, this.currentProjectFile.getAbsolutePath());

    }

    /**
     * 保存按钮
     */
    private void onsaveproject() {
        saveprojectdata();
        JOptionPane.showMessageDialog(null,"保存完成！");
    }

    /**
     * 保存工程文件
     */
    private void saveProjectFile() //保存文件
    {
        String fileName;

        //设置保存文件对话框的标题
        fc.setDialogTitle("选择工程文件保存位置！");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
            MainForm.currentProjectFile = fc.getSelectedFile();
            MainForm.currentProject = new debProjectModel();
            clearProjectData();
            this.textProjectFile.setText(currentProjectFile.getAbsolutePath());
            this.tabmain.setEnabled(true);
            this.tabmain.setSelectedIndex(1);
        }
    }

    /**
     * 选择工程文件
     */
    private File selectScriptFile(String title) //保存文件
    {
        String fileName;
        File result = null;

        //设置保存文件对话框的标题
        fc.setDialogTitle(title);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //fc.setFileFilter(projectFilter);

        //这里将显示保存文件的对话框
        try {
            flag = fc.showOpenDialog(null);
        } catch (HeadlessException he) {
            System.out.println("Save File Dialog ERROR!");
        }

        //如果按下确定按钮，则获得该文件。
        if (flag == JFileChooser.APPROVE_OPTION) {
            //获得你输入要保存的文件
            result = fc.getSelectedFile();
            return result;
        } else {
            return result;
        }
    }

    /**
     * 新建工程
     */
    private void clearProjectData() {
        textArchitecture.setText("all");
        rbAll.setSelected(true);
        rbOnlyI386.setSelected(false);
        rbOnlyAmd64.setSelected(false);
        rbOnlyPowerPC.setSelected(false);
        this.textProjectName.setText("未命名");
        this.textDescription.setText("无");
        this.textPackageName.setText("empty");
        this.textHomePage.setText("http://www.linuxdeepin.com");
        this.textVersion.setText("1.0");
        this.textInstalledSize.setText("0");
        this.textSection.setText("utils");
        this.textMaintainer.setText("无人维护");
        //this.textOriginalMaintainer.setText("无人维护");
        this.textPriority.setText("optional");
        this.textDebName.setText("empty.deb");
        this.textResultDir.setText("");
        this.textProjectFile.setText("");
        this.clearDependEditor();
        this.listDepends.setListData(new Object[]{});
        this.textPostInst.setText("");
        this.textPostRm.setText("");
        this.textPreInst.setText("");
        this.textPreRm.setText("");
    }

    /**
     * 显示工程数据
     */
    private void showProjectData() {
        textArchitecture.setText(MainForm.currentProject.packageArchitecture);
        if (MainForm.currentProject.packageArchitecture != null) {
            rbAll.setSelected(false);
            rbOnlyI386.setSelected(false);
            rbOnlyAmd64.setSelected(false);
            rbOnlyPowerPC.setSelected(false);

            if (MainForm.currentProject.packageArchitecture.contains("all")) {
                rbAll.setSelected(true);
            } else if (MainForm.currentProject.packageArchitecture.contains("i386")) {
                rbOnlyI386.setSelected(true);
            } else if (MainForm.currentProject.packageArchitecture.contains("amd64")) {
                rbOnlyAmd64.setSelected(true);
            } else if (MainForm.currentProject.packageArchitecture.contains("powerpc")) {
                rbOnlyPowerPC.setSelected(true);
            }
        }
        this.textProjectName.setText(MainForm.currentProject.projectName);
        this.textDescription.setText(MainForm.currentProject.packageDescription);
        this.textPackageName.setText(MainForm.currentProject.packageName);
        this.textHomePage.setText(MainForm.currentProject.packageHomepage);
        this.textVersion.setText(MainForm.currentProject.packageVersion);
        this.textInstalledSize.setText(MainForm.currentProject.packageInstalledSize);
        this.textSection.setText(MainForm.currentProject.packageSection);
        this.textMaintainer.setText(MainForm.currentProject.packageMaintainer);
        //this.textOriginalMaintainer.setText(MainForm.currentProject.packageOriginalMaintainer);
        this.textPriority.setText(MainForm.currentProject.packagePriority);
        this.textDebName.setText(MainForm.currentProject.debPackagename);
        this.textResultDir.setText(MainForm.currentProject.resultDir);
        this.clearDependEditor();
        this.clearStartupEditor();
        this.clearFilesEditor();
        this.listDepends.setListData(MainForm.currentProject.packageDepends.toArray());
        this.listStartup.setListData(MainForm.currentProject.packageStartupList.toArray());
        this.listFiles.setListData(MainForm.currentProject.packageFiles.toArray());
        this.textPostInst.setText(MainForm.currentProject.packagePostInstFile);
        this.textPostRm.setText(MainForm.currentProject.packagePostRmFile);
        this.textPreInst.setText(MainForm.currentProject.packagePreInstFile);
        this.textPreRm.setText(MainForm.currentProject.packagePreRmFile);
    }

    /**
     * 改变编辑器状态
     *
     * @param isopen
     * @param projectfile
     */
    public void changeEditorStatus(Boolean isopen, String projectfile) {
        if (isopen) {
            MainForm.currentProjectFile = new File(projectfile);
            MainForm.currentProject = debProjectModelRW.loadProject(projectfile);
            showProjectData();
            this.tabmain.setEnabled(true);
            this.tabmain.setSelectedIndex(1);
            this.textProjectFile.setText(MainForm.currentProjectFile.getAbsolutePath());
        } else {
            this.tabmain.setEnabled(false);
            this.tabmain.setSelectedIndex(0);
        }
    }

    /**
     * 入口函数
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args != null && args.length >= 1)
        {
           String cmd = null;
           String project = null;
           String debfile = null;
           String temp = null;
           for(int k=0;k < args.length;k++)
           {
               temp = args[k] == null?"":args[k].trim();
               if (temp.startsWith("--help"))
               {
                  System.out.println("DebBuilder软件包生成器V1.5 QQ707519239");
                  System.out.println(" 参数(注意大小写和空格!)：");
                  System.out.println(" --help    帮助。");
                  System.out.println(" -compile  编译软件包工程文件。");
                  System.out.println(" -open 打开工程文件");
                  System.out.println(" -project= 指向软件包工程文件全路径。");
                  System.out.println(" -debfile= 编译完成后软件包保存位置。");

               }else
               {
                   if (temp.startsWith("-compile"))
                   {
                      cmd = "compile";
                   }else if (temp.startsWith("-open"))
                   {
                      cmd = "open";
                   }else if (temp.startsWith("-project="))
                   {
                       project = temp.replace("-project=","");
                   }else if (temp.startsWith("-debfile="))
                   {
                       debfile = temp.replace("-debfile=","");
                   }
               }
           }
           if (cmd != null && cmd.startsWith("compile") && project != null && debfile != null)
           {
              debProjectModel dpmm = debProjectModelRW.loadProject(project);
              makeInstallPkg(dpmm,debfile,true);
           }else if (cmd != null && cmd.startsWith("open") && project != null)
           {
               MainForm dialog = new MainForm();
               dialog.pack();
               dialog.setTitle("DebBuilder软件包生成器 V1.5 QQ707519239");
               //dialog.setSize(new Dimension(900,650));
               dialog.changeEditorStatus(true, project);
               MainForm.setFormToCenter(dialog);
               dialog.setVisible(true);
               System.exit(0);
           }
            
        } else {
            MainForm dialog = new MainForm();
            dialog.pack();
            dialog.setTitle("DebBuilder软件包生成器 V1.5 QQ707519239");
            //dialog.setSize(new Dimension(900,650));
            dialog.changeEditorStatus(false, "/home/wcss/测试工程.dpro");
            MainForm.setFormToCenter(dialog);
            dialog.setVisible(true);
            System.exit(0);
        }
    }
}
