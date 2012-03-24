package debBuilder.MainUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import debBuilder.builderConfig.configManager;
import debBuilder.language.*;
import debBuilder.makeDesktopStartup;
import debProjectModels.*;
import debProjectTool.*;
import jAppHelper.jCmdRunHelper;

public class MainForm extends JDialog {
    private JPanel contentPane;
    private JLabel lblStatus;
    private JTabbedPane tabmain;
    private JPanel tab1;
    private JButton btnNewProject;
    private JButton btnOpenProject;
    private JButton btnSaveProject;
    private JButton btnCompileProject;
    private JTextField textProjectName;
    private JTextArea textDescription;
    private JRadioButton rbAll;
    private JRadioButton rbOnlyI386;
    private JRadioButton rbOnlyAmd64;
    private JButton btnSelectResultDir;
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
    private JComboBox cboxSection;
    private JComboBox cboxStartup;
    private JButton btnSaveFor;
    private JLabel txt1;
    private JLabel txt2;
    private JLabel txt3;
    private JLabel txt4;
    private JLabel txt5;
    private JLabel txt6;
    private JLabel txt7;
    private JLabel txt8;
    private JLabel txt9;
    private JLabel txt10;
    private JLabel txt11;
    private JLabel txt12;
    private JLabel txt13;
    private JLabel txt14;
    private JLabel txt15;
    private JLabel txt16;
    private JLabel txt17;
    private JLabel txt18;
    private JLabel txt19;
    private JLabel txt20;
    private JLabel txt21;
    private JLabel txt22;
    private JLabel txt23;
    private JLabel txt24;
    private JLabel txt25;
    private JLabel txt26;
    private JLabel txt27;
    private JLabel txt28;
    private JLabel txt29;
    private JLabel txt30;
    private JLabel txt31;
    private JLabel txt32;
    private JLabel txt33;
    private JLabel txt34;
    private JLabel txt35;
    private JLabel txt36;
    private JLabel txt37;
    private JLabel txt38;
    private JLabel txt39;
    private JLabel txt40;
    private JPanel tab0;
    private JPanel tab2;
    private JPanel tab3;
    private JPanel panel4;
    private JPanel tab4;
    private JPanel tab5;
    private JLabel txt102;
    private JLabel txt103;
    private JComboBox cbbCompileType;
    private JTextField textLicense;
    private JComboBox cbbLicense;
    private JLabel txt104;
    private JScrollPane psjPane;
    private JComboBox cbbArchList;
    private JTextField textPreUpgrade;
    private JButton btnSelectPreUpgrade;
    private JTextField textPostUpgrade;
    private JTextField textPreDowngrade;
    private JTextField textPostDowngrade;
    private JLabel txt105;
    private JLabel txt106;
    private JLabel txt107;
    private JLabel txt108;
    private JButton btnSelectPostUpgrade;
    private JButton btnSelectPreDowngrade;
    private JButton btnSelectPostDowngrade;
    private JLabel txt117;
    private JTextField textRepo;
    private JComboBox cbbRepoList;
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

    public String getCurrentArch()
    {
        String arch = "";
        try {
            InputStream is = jCmdRunHelper.runSysCmd("uname -i", false).getInputStream();
            arch = jAppHelper.jDataRWHelper.readFromInputStream(is)[0].trim();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return arch;
    }

    public MainForm() {
        setContentPane(contentPane);
        setModal(true);
        //getRootPane().setDefaultButton(buttonOK);
        //makeLanguageFile(jAppHelper.jCmdRunHelper.getUserHomeDirPath() + "/language.template");
        loadMakerTypeList();
        clearProjectData();
        this.setUILanguage();
        lblStatus.setText(languageManager.getShowText("99"));
        this.listDepends.setListData(new Object[]{});

        //基本信息
        cbbArchList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (cbbArchList.getSelectedItem().toString().equals(getCurrentArch()))
                {
                   textArchitecture.setText(cbbArchList.getSelectedItem().toString());
                }else
                {
                    if (cbbCompileType.getSelectedItem().toString().equals("rpm"))
                    {
                        textArchitecture.setText(cbbArchList.getSelectedItem().toString() + " " + getCurrentArch());
                    }else
                    {
                        textArchitecture.setText(cbbArchList.getSelectedItem().toString());
                    }

                }
            }
        });

        btnNewProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                saveProjectFile();
            }


        });
        btnSelectResultDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                fc.setMultiSelectionEnabled(false);
                fc.setDialogTitle(languageManager.getShowText("89"));
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
                if (textDependName.getText() == null || textDependName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, languageManager.getShowText("78"));
                } else {
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
                    lblStatus.setText(languageManager.getShowText("88"));
                    btnSaveDepends.setEnabled(false);
                    MainForm.currentDepend = null;
                }
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
                File posti = selectScriptFile(languageManager.getShowText("90"));
                if (posti != null) {
                    textPostInst.setText(posti.getAbsolutePath());
                }
            }
        });
        btnSelectPostRm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                File postr = selectScriptFile(languageManager.getShowText("91"));
                if (postr != null) {
                    textPostRm.setText(postr.getAbsolutePath());
                }
            }
        });
        btnSelectPreInst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                File prei = selectScriptFile(languageManager.getShowText("92"));
                if (prei != null) {
                    textPreInst.setText(prei.getAbsolutePath());
                }
            }
        });
        btnSelectPreRm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                File prer = selectScriptFile(languageManager.getShowText("93"));
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
                if (textStartupName.getText() == null || textStartupName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, languageManager.getShowText("84"));
                } else {
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
                    lblStatus.setText(languageManager.getShowText("87"));
                    btnSaveStartup.setEnabled(false);
                    MainForm.currentStartup = null;
                }
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
                File icon = selectScriptFile(languageManager.getShowText("94"));
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
                if (textSourcePath.getText() == null || textSourcePath.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, languageManager.getShowText("82"));
                } else if (textDestPath.getText() == null || textDestPath.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, languageManager.getShowText("83"));
                } else {
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
                    lblStatus.setText(languageManager.getShowText("86"));
                    btnSaveFiles.setEnabled(false);
                    btnNewFiles.setEnabled(true);
                    MainForm.currentFiles = null;
                }
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
                fc.setDialogTitle(languageManager.getShowText("95"));
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
                fc.setDialogTitle(languageManager.getShowText("96"));
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
                File open = selectScriptFile(languageManager.getShowText("97"));
                if (open != null) {
                    changeEditorStatus(true, open.getAbsolutePath());
                }
            }
        });
        btnCompileProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                saveprojectdata(MainForm.currentProjectFile.getAbsolutePath());
                if (MainForm.currentProject.debPackagename == null || MainForm.currentProject.resultDir == null) {
                    JOptionPane.showMessageDialog(null, languageManager.getShowText("81"));
                } else if (MainForm.currentProject.debPackagename.isEmpty() || MainForm.currentProject.resultDir.isEmpty()) {
                    JOptionPane.showMessageDialog(null, languageManager.getShowText("81"));
                } else {
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
                    JOptionPane.showMessageDialog(null, languageManager.getShowText("80"));
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });

        //下拉列表
        cboxSection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textSection.setText(cboxSection.getSelectedItem().toString());
            }
        });
        cboxStartup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textStartupCategories.setText(cboxStartup.getSelectedItem().toString());
            }
        });

        //界面
        btnSaveFor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                onsaveprojectfor();
            }
        });

        //变换平台支持列表
        cbbCompileType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                changeArchList(cbbCompileType.getSelectedItem().toString());
            }
        });

        //变换许可证
        cbbLicense.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textLicense.setText(cbbLicense.getSelectedItem().toString());
            }
        });

        //针对ypk包增加的选项
        btnSelectPreUpgrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File posti = selectScriptFile(languageManager.getShowText("113"));
                if (posti != null) {
                    textPreUpgrade.setText(posti.getAbsolutePath());
                }

            }
        });
        btnSelectPostUpgrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File posti = selectScriptFile(languageManager.getShowText("114"));
                if (posti != null) {
                    textPostUpgrade.setText(posti.getAbsolutePath());
                }

            }
        });
        btnSelectPreDowngrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File posti = selectScriptFile(languageManager.getShowText("115"));
                if (posti != null) {
                    textPreDowngrade.setText(posti.getAbsolutePath());
                }

            }
        });
        btnSelectPostDowngrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File posti = selectScriptFile(languageManager.getShowText("116"));
                if (posti != null) {
                    textPostDowngrade.setText(posti.getAbsolutePath());
                }

            }
        });

        //版本类型
        cbbRepoList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
                textRepo.setText(cbbRepoList.getSelectedItem().toString());
            }
        });
    }

    /**
     * 处理编译器类型
     */
    private void loadMakerTypeList()
    {
        if (configManager.config.hideCompileType != null)
        {
           String[] hideteam = configManager.config.hideCompileType.split(",");
           ArrayList showlist = new ArrayList();
           showlist.add("deb");
           showlist.add("rpm");
           showlist.add("ypk");
           for(String str:hideteam)
           {
               showlist.remove(str);
           }
           ComboBoxModel cbm = new DefaultComboBoxModel(showlist.toArray());
           cbbCompileType.setModel(cbm);
        }else
        {
            ArrayList showlists = new ArrayList();
            showlists.add("deb");
            showlists.add("rpm");
            showlists.add("ypk");
            ComboBoxModel cbms = new DefaultComboBoxModel(showlists.toArray());
            cbbCompileType.setModel(cbms);

        }
    }

    /**
     *　改变目标平台选项
     * @param arch
     */
    public void changeArchList(String arch)
    {
        if (arch.equals("deb"))
        {
            ComboBoxModel cbm1 = new DefaultComboBoxModel(new Object[]{ "All","i386","i586","i686","amd64","powserpc" });
            cbbArchList.setModel(cbm1);
        }else if (arch.equals("rpm"))
        {
            ComboBoxModel cbm2 = new DefaultComboBoxModel(new Object[]{ "noarch","i386","i586","i686","x86_64"});
            cbbArchList.setModel(cbm2);
        }else if (arch.equals("ypk"))
        {
            ComboBoxModel cbm3 = new DefaultComboBoxModel(new Object[]{ "any","x86_64","i686"});
            cbbArchList.setModel(cbm3);
        }
    }

    /**
     * 保存工程文件
     */
    private String selectSaveProjectFor(String title) //保存文件
    {
        String fileName;

        //设置保存文件对话框的标题
        fc.setDialogTitle(title);
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
            return fc.getSelectedFile().getAbsolutePath();
        }
        return "";
    }

    /**
     * 工程另存为
     */
    private void onsaveprojectfor() {
        //To change body of created methods use File | Settings | File Templates.
        String savepath = selectSaveProjectFor(languageManager.getShowText("85"));
        if (savepath == "") {
            //没有文件要保存
        } else {
            saveprojectdata(savepath);
            JOptionPane.showMessageDialog(null, languageManager.getShowText("79"));
        }
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
        makeDialog.setTitle(languageManager.getShowText("72"));
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
        textStartupIcon.setText("ico file");
        textStartupComment.setText("readme");
        textStartupExec.setText("java -jar test.jar");
        textStartupGenericName.setText("editor app");
        textStartupName.setText("boot test");
        textStartupType.setText("Application");
        textStartupVersion.setText("1.0");
    }

    /**
     * 清理依赖编辑框
     */
    private void clearDependEditor() {
        textDependName.setText("none");
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
    private void saveprojectdata(String projectsave) {
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
        MainForm.currentProject.packageOriginalMaintainer = "none";
        MainForm.currentProject.packagePriority = this.textPriority.getText().trim();
        MainForm.currentProject.packagePostInstFile = textPostInst.getText();
        MainForm.currentProject.packagePostRmFile = textPostRm.getText();
        MainForm.currentProject.packagePreInstFile = textPreInst.getText();
        MainForm.currentProject.packagePreRmFile = textPreRm.getText();
        MainForm.currentProject.packagePreUpgradeFile = textPreUpgrade.getText();
        MainForm.currentProject.packagePostUpgradeFile = textPostUpgrade.getText();
        MainForm.currentProject.packagePreDowngradeFile = textPreDowngrade.getText();
        MainForm.currentProject.packagePostDowngradeFile = textPostDowngrade.getText();
        MainForm.currentProject.packageMakerType = cbbCompileType.getSelectedItem().toString();
        MainForm.currentProject.packageLicense = textLicense.getText();
        MainForm.currentProject.packageRepo = textRepo.getText();

        if (projectsave.endsWith(".dproject")) {
            //后缀名正确无需修改
        } else {
            projectsave += ".dproject";
        }

        debProjectModelRW.saveProject(MainForm.currentProject, projectsave);

    }

    /**
     * 保存按钮
     */
    private void onsaveproject() {
        saveprojectdata(this.currentProjectFile.getAbsolutePath());
        JOptionPane.showMessageDialog(null, languageManager.getShowText("79"));
    }

    /**
     * 保存工程文件
     */
    private void saveProjectFile() //保存文件
    {
        String fileName;

        //设置保存文件对话框的标题
        fc.setDialogTitle(languageManager.getShowText("98"));
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
        cbbArchList.setSelectedIndex(0);
        this.textProjectName.setText("none");
        this.textDescription.setText("none");
        this.textPackageName.setText("empty");
        this.textHomePage.setText("http://www.linuxdeepin.com");
        this.textVersion.setText("1.0");
        this.textInstalledSize.setText("0");
        this.textSection.setText("utils");
        this.textMaintainer.setText("none");
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
        this.textLicense.setText("GPL");
        this.textRepo.setText("stable");
        if (configManager.config.defaultNewProjectCompileType < cbbCompileType.getItemCount())
        {
          this.cbbCompileType.setSelectedIndex(configManager.config.defaultNewProjectCompileType);
        }
        changeArchList(this.cbbCompileType.getSelectedItem().toString());
    }

    /**
     * 显示工程数据
     */
    private void showProjectData() {
        textArchitecture.setText(MainForm.currentProject.packageArchitecture);
        if (MainForm.currentProject.packageArchitecture != null && MainForm.currentProject.packageMakerType != null)
        {
            changeArchList(MainForm.currentProject.packageMakerType);
            cbbArchList.setSelectedItem(MainForm.currentProject.packageArchitecture);
        }
        this.textProjectName.setText(MainForm.currentProject.projectName);
        this.textDescription.setText(MainForm.currentProject.packageDescription);
        this.textPackageName.setText(MainForm.currentProject.packageName);
        this.textHomePage.setText(MainForm.currentProject.packageHomepage);
        this.textVersion.setText(MainForm.currentProject.packageVersion);
        this.textInstalledSize.setText(MainForm.currentProject.packageInstalledSize);
        this.textSection.setText(MainForm.currentProject.packageSection);
        this.textMaintainer.setText(MainForm.currentProject.packageMaintainer);
        this.textLicense.setText(MainForm.currentProject.packageLicense);
        //this.textOriginalMaintainer.setText(MainForm.currentProject.packageOriginalMaintainer);
        this.textPriority.setText(MainForm.currentProject.packagePriority);
        this.textDebName.setText(MainForm.currentProject.debPackagename);
        this.textResultDir.setText(MainForm.currentProject.resultDir);
        this.textRepo.setText(MainForm.currentProject.packageRepo);
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
        this.textPreUpgrade.setText(MainForm.currentProject.packagePreUpgradeFile);
        this.textPostUpgrade.setText(MainForm.currentProject.packagePostUpgradeFile);
        this.textPreDowngrade.setText(MainForm.currentProject.packagePreDowngradeFile);
        this.textPostDowngrade.setText(MainForm.currentProject.packagePostDowngradeFile);
        this.cbbCompileType.setSelectedItem(MainForm.currentProject.packageMakerType);
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
     * 载入语言数据
     */
    public static void loadLanguage() {
        //makeLanguageFile(jAppHelper.jCmdRunHelper.getUserHomeDirPath() + "/language.template");

        if (new File(configManager.config.workDir + "/" + configManager.config.languageName).exists())
        {
            try
            {
                languageManager.languageData = new ArrayList<languageModel>();
                languageManager.loadLanguageFile(configManager.config.workDir + "/" + configManager.config.languageName);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    /**
     *　显示语言数据
     */
    private void setUILanguage() {
        if (languageManager.languageData != null && languageManager.languageData.size() > 90)
        {
            txt1.setText(languageManager.getShowText("1"));
            txt2.setText(languageManager.getShowText("2"));
            txt3.setText(languageManager.getShowText("3"));
            txt4.setText(languageManager.getShowText("4"));
            txt5.setText(languageManager.getShowText("5"));
            txt6.setText(languageManager.getShowText("6"));
            txt7.setText(languageManager.getShowText("7"));
            txt8.setText(languageManager.getShowText("8"));
            txt9.setText(languageManager.getShowText("9"));
            txt10.setText(languageManager.getShowText("10"));
            txt11.setText(languageManager.getShowText("11"));
            txt12.setText(languageManager.getShowText("12"));
            txt13.setText(languageManager.getShowText("13"));
            txt14.setText(languageManager.getShowText("14"));
            txt15.setText(languageManager.getShowText("15"));
            txt16.setText(languageManager.getShowText("16"));
            txt17.setText(languageManager.getShowText("17"));
            txt18.setText(languageManager.getShowText("18"));
            txt19.setText(languageManager.getShowText("19"));
            txt20.setText(languageManager.getShowText("20"));
            txt21.setText(languageManager.getShowText("21"));
            txt22.setText(languageManager.getShowText("22"));
            txt23.setText(languageManager.getShowText("23"));
            txt24.setText(languageManager.getShowText("24"));
            txt25.setText(languageManager.getShowText("25"));
            txt26.setText(languageManager.getShowText("26"));
            txt27.setText(languageManager.getShowText("27"));
            txt28.setText(languageManager.getShowText("28"));
            txt29.setText(languageManager.getShowText("29"));
            txt30.setText(languageManager.getShowText("30"));
            txt31.setText(languageManager.getShowText("31"));
            txt32.setText(languageManager.getShowText("32"));
            txt33.setText(languageManager.getShowText("33"));
            txt34.setText(languageManager.getShowText("34"));
            txt35.setText(languageManager.getShowText("35"));
            txt36.setText(languageManager.getShowText("36"));
            txt37.setText(languageManager.getShowText("37"));
            txt38.setText(languageManager.getShowText("38"));
            txt39.setText(languageManager.getShowText("39"));
            txt40.setText(languageManager.getShowText("40"));
            txt102.setText(languageManager.getShowText("102"));
            txt103.setText(languageManager.getShowText("103"));
            txt104.setText(languageManager.getShowText("104"));

            txt105.setText(languageManager.getShowText("105"));
            txt106.setText(languageManager.getShowText("106"));
            txt107.setText(languageManager.getShowText("107"));
            txt108.setText(languageManager.getShowText("108"));
            txt117.setText(languageManager.getShowText("117"));


            //输出标签页名字
            tabmain.setTitleAt(0,languageManager.getShowText("41"));
            tabmain.setTitleAt(1,languageManager.getShowText("42"));
            tabmain.setTitleAt(2,languageManager.getShowText("43"));
            tabmain.setTitleAt(3,languageManager.getShowText("44"));
            tabmain.setTitleAt(4,languageManager.getShowText("45"));
            tabmain.setTitleAt(5,languageManager.getShowText("101"));

            //输出按钮
            btnSelectResultDir.setText(languageManager.getShowText("46"));
            btnCancelFiles.setText(languageManager.getShowText("47"));
            btnCancelNew.setText(languageManager.getShowText("48"));
            btnCancelSave.setText(languageManager.getShowText("49"));
            btnCompileProject.setText(languageManager.getShowText("50"));
            btnDelDepends.setText(languageManager.getShowText("51"));
            btnDelFiles.setText(languageManager.getShowText("52"));
            btnDelStartup.setText(languageManager.getShowText("53"));
            btnDesktopCompile.setText(languageManager.getShowText("54"));
            btnNewDepends.setText(languageManager.getShowText("55"));
            btnNewFiles.setText(languageManager.getShowText("56"));
            btnNewProject.setText(languageManager.getShowText("57"));
            btnNewStartup.setText(languageManager.getShowText("58"));
            btnOpenProject.setText(languageManager.getShowText("59"));
            btnSaveDepends.setText(languageManager.getShowText("60"));
            btnSaveFiles.setText(languageManager.getShowText("61"));
            btnSaveFor.setText(languageManager.getShowText("62"));
            btnSaveProject.setText(languageManager.getShowText("63"));
            btnSaveStartup.setText(languageManager.getShowText("64"));
            btnSelectDestPath.setText(languageManager.getShowText("65"));
            btnSelectIcon.setText(languageManager.getShowText("66"));
            btnSelectPostInst.setText(languageManager.getShowText("67"));
            btnSelectPostRm.setText(languageManager.getShowText("68"));
            btnSelectPreInst.setText(languageManager.getShowText("69"));
            btnSelectPreRm.setText(languageManager.getShowText("70"));
            btnSelectSourcePath.setText(languageManager.getShowText("71"));

            btnSelectPreUpgrade.setText(languageManager.getShowText("109"));
            btnSelectPostUpgrade.setText(languageManager.getShowText("110"));
            btnSelectPreDowngrade.setText(languageManager.getShowText("111"));
            btnSelectPostDowngrade.setText(languageManager.getShowText("112"));

        }

    }

    /**
     * 临时生成语言文件模板
     */
    public void makeLanguageFile(String savepaths) {
        //输出标签
        //languageManager.languageData.add(new languageModel("0","DebBuilder软件包生成器"));
        languageManager.languageData.add(new languageModel("1", txt1.getText()));
        languageManager.languageData.add(new languageModel("2", txt2.getText()));
        languageManager.languageData.add(new languageModel("3", txt3.getText()));
        languageManager.languageData.add(new languageModel("4", txt4.getText()));
        languageManager.languageData.add(new languageModel("5", txt5.getText()));
        languageManager.languageData.add(new languageModel("6", txt6.getText()));
        languageManager.languageData.add(new languageModel("7", txt7.getText()));
        languageManager.languageData.add(new languageModel("8", txt8.getText()));
        languageManager.languageData.add(new languageModel("9", txt9.getText()));
        languageManager.languageData.add(new languageModel("10", txt10.getText()));
        languageManager.languageData.add(new languageModel("11", txt11.getText()));
        languageManager.languageData.add(new languageModel("12", txt12.getText()));
        languageManager.languageData.add(new languageModel("13", txt13.getText()));
        languageManager.languageData.add(new languageModel("14", txt14.getText()));
        languageManager.languageData.add(new languageModel("15", txt15.getText()));
        languageManager.languageData.add(new languageModel("16", txt16.getText()));
        languageManager.languageData.add(new languageModel("17", txt17.getText()));
        languageManager.languageData.add(new languageModel("18", txt18.getText()));
        languageManager.languageData.add(new languageModel("19", txt19.getText()));
        languageManager.languageData.add(new languageModel("20", txt20.getText()));
        languageManager.languageData.add(new languageModel("21", txt21.getText()));
        languageManager.languageData.add(new languageModel("22", txt22.getText()));
        languageManager.languageData.add(new languageModel("23", txt23.getText()));
        languageManager.languageData.add(new languageModel("24", txt24.getText()));
        languageManager.languageData.add(new languageModel("25", txt25.getText()));
        languageManager.languageData.add(new languageModel("26", txt26.getText()));
        languageManager.languageData.add(new languageModel("27", txt27.getText()));
        languageManager.languageData.add(new languageModel("28", txt28.getText()));
        languageManager.languageData.add(new languageModel("29", txt29.getText()));
        languageManager.languageData.add(new languageModel("30", txt30.getText()));
        languageManager.languageData.add(new languageModel("31", txt31.getText()));
        languageManager.languageData.add(new languageModel("32", txt32.getText()));
        languageManager.languageData.add(new languageModel("33", txt33.getText()));
        languageManager.languageData.add(new languageModel("34", txt34.getText()));
        languageManager.languageData.add(new languageModel("35", txt35.getText()));
        languageManager.languageData.add(new languageModel("36", txt36.getText()));
        languageManager.languageData.add(new languageModel("37", txt37.getText()));
        languageManager.languageData.add(new languageModel("38", txt38.getText()));
        languageManager.languageData.add(new languageModel("39", txt39.getText()));
        languageManager.languageData.add(new languageModel("40", txt40.getText()));
        languageManager.languageData.add(new languageModel("102", txt102.getText()));
        languageManager.languageData.add(new languageModel("103",txt103.getText()));
        languageManager.languageData.add(new languageModel("104",txt104.getText()));
        languageManager.languageData.add(new languageModel("105",txt105.getText()));
        languageManager.languageData.add(new languageModel("106",txt106.getText()));
        languageManager.languageData.add(new languageModel("107",txt107.getText()));
        languageManager.languageData.add(new languageModel("108",txt108.getText()));
        languageManager.languageData.add(new languageModel("117",txt117.getText()));

        //输出标签页名字
        languageManager.languageData.add(new languageModel("41", tabmain.getTitleAt(0)));
        languageManager.languageData.add(new languageModel("42", tabmain.getTitleAt(1)));
        languageManager.languageData.add(new languageModel("43", tabmain.getTitleAt(2)));
        languageManager.languageData.add(new languageModel("44", tabmain.getTitleAt(3)));
        languageManager.languageData.add(new languageModel("45", tabmain.getTitleAt(4)));
        languageManager.languageData.add(new languageModel("101",tabmain.getTitleAt(5)));

        //输出按钮
        languageManager.languageData.add(new languageModel("46", btnSelectResultDir.getText()));
        languageManager.languageData.add(new languageModel("47", btnCancelFiles.getText()));
        languageManager.languageData.add(new languageModel("48", btnCancelNew.getText()));
        languageManager.languageData.add(new languageModel("49", btnCancelSave.getText()));
        languageManager.languageData.add(new languageModel("50", btnCompileProject.getText()));
        languageManager.languageData.add(new languageModel("51", btnDelDepends.getText()));
        languageManager.languageData.add(new languageModel("52", btnDelFiles.getText()));
        languageManager.languageData.add(new languageModel("53", btnDelStartup.getText()));
        languageManager.languageData.add(new languageModel("54", btnDesktopCompile.getText()));
        languageManager.languageData.add(new languageModel("55", btnNewDepends.getText()));
        languageManager.languageData.add(new languageModel("56", btnNewFiles.getText()));
        languageManager.languageData.add(new languageModel("57", btnNewProject.getText()));
        languageManager.languageData.add(new languageModel("58", btnNewStartup.getText()));
        languageManager.languageData.add(new languageModel("59", btnOpenProject.getText()));
        languageManager.languageData.add(new languageModel("60", btnSaveDepends.getText()));
        languageManager.languageData.add(new languageModel("61", btnSaveFiles.getText()));
        languageManager.languageData.add(new languageModel("62", btnSaveFor.getText()));
        languageManager.languageData.add(new languageModel("63", btnSaveProject.getText()));
        languageManager.languageData.add(new languageModel("64", btnSaveStartup.getText()));
        languageManager.languageData.add(new languageModel("65", btnSelectDestPath.getText()));
        languageManager.languageData.add(new languageModel("66", btnSelectIcon.getText()));
        languageManager.languageData.add(new languageModel("67", btnSelectPostInst.getText()));
        languageManager.languageData.add(new languageModel("68", btnSelectPostRm.getText()));
        languageManager.languageData.add(new languageModel("69", btnSelectPreInst.getText()));
        languageManager.languageData.add(new languageModel("70", btnSelectPreRm.getText()));
        languageManager.languageData.add(new languageModel("71", btnSelectSourcePath.getText()));

        languageManager.languageData.add(new languageModel("109",btnSelectPreUpgrade.getText()));
        languageManager.languageData.add(new languageModel("110",btnSelectPostUpgrade.getText()));
        languageManager.languageData.add(new languageModel("111",btnSelectPreDowngrade.getText()));
        languageManager.languageData.add(new languageModel("112",btnSelectPostDowngrade.getText()));

        //进度条窗口
        languageManager.languageData.add(new languageModel("72", "DebBuilder软件包生成器-编译进度"));
        languageManager.languageData.add(new languageModel("73", "正在生成安装包......"));
        languageManager.languageData.add(new languageModel("74", "正在生成安装包，包名：(x)"));
        languageManager.languageData.add(new languageModel("75", "关闭"));
        languageManager.languageData.add(new languageModel("76", "软件包(x)编译失败！错误：(y)"));
        languageManager.languageData.add(new languageModel("77", "软件包(x)编译完成！"));

        //提示信息
        languageManager.languageData.add(new languageModel("78", "请输入软件包名！"));
        languageManager.languageData.add(new languageModel("79", "保存完成！"));
        languageManager.languageData.add(new languageModel("80", "生成成功！"));
        languageManager.languageData.add(new languageModel("81", "对不起，编译文件名或编译输出目录不能为空！"));
        languageManager.languageData.add(new languageModel("82", "源路径不能为空！"));
        languageManager.languageData.add(new languageModel("83", "目标路径不能为空！"));
        languageManager.languageData.add(new languageModel("84", "请输入启动器显示名称！"));
        languageManager.languageData.add(new languageModel("85", "工程另存为！"));
        languageManager.languageData.add(new languageModel("86", "添加/修改安装目录完成！"));
        languageManager.languageData.add(new languageModel("87", "添加/修改菜单启动器完成！"));
        languageManager.languageData.add(new languageModel("88", "添加/修改软件包依赖完成！"));
        languageManager.languageData.add(new languageModel("89", "选择编译输出目录！"));
        languageManager.languageData.add(new languageModel("90", "选择PostInst脚本！"));
        languageManager.languageData.add(new languageModel("91", "选择PostRm脚本！"));
        languageManager.languageData.add(new languageModel("92", "选择PreInst脚本！"));
        languageManager.languageData.add(new languageModel("93", "选择PreRm脚本！"));
        languageManager.languageData.add(new languageModel("94", "请选择启动器图片文件！"));
        languageManager.languageData.add(new languageModel("95", "选择源目录！"));
        languageManager.languageData.add(new languageModel("96", "选择目标目录！"));
        languageManager.languageData.add(new languageModel("97", "选择DebBuilder工程文件!"));
        languageManager.languageData.add(new languageModel("98", "选择工程文件保存位置！"));
        languageManager.languageData.add(new languageModel("99", "DebBuilder软件包生成器 V1.7 QQ707519239"));

        languageManager.languageData.add(new languageModel("100", "为(x)快速打包"));

        languageManager.languageData.add(new languageModel("113","请选择PreUpgrade脚本！"));
        languageManager.languageData.add(new languageModel("114","请选择PostUpgrade脚本！"));
        languageManager.languageData.add(new languageModel("115","请选择PreDowngrade脚本！"));
        languageManager.languageData.add(new languageModel("116","请选择PostDowngrade脚本！"));

        try {
            languageManager.saveLanguageFile(savepaths);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * 入口函数
     *
     * @param args
     */
    public static void main(String[] args)
    {

        configManager.loadConfig();
        loadLanguage();

        if (args != null && args.length >= 1) {
            String cmd = null;
            String project = null;
            String debfile = null;
            String temp = null;
            for (int k = 0; k < args.length; k++) {
                temp = args[k] == null ? "" : args[k].trim();
                if (temp.startsWith("--help")) {
                    System.out.println("DebBuilder");
                    //System.out.println(" cmd：");
                    System.out.println(" --help    Help(帮助)");
                    System.out.println(" -compile  Compile Project(编译软件包工程文件)");
                    System.out.println(" -open Open Project(打开工程文件)");
                    System.out.println(" -project= Set Project Path(指向软件包工程文件全路径)");
                    System.out.println(" -debfile= Set Deb Output Path(编译完成后软件包保存位置)");

                } else {
                    if (temp.startsWith("-compile")) {
                        cmd = "compile";
                    } else if (temp.startsWith("-open")) {
                        cmd = "open";
                    } else if (temp.startsWith("-project=")) {
                        project = temp.replace("-project=", "");
                    } else if (temp.startsWith("-debfile=")) {
                        debfile = temp.replace("-debfile=", "");
                    }
                }
            }
            if (cmd != null && cmd.startsWith("compile") && project != null && debfile != null) {
                debProjectModel dpmm = debProjectModelRW.loadProject(project);
                makeInstallPkg(dpmm, debfile, true);
            } else if (cmd != null && cmd.startsWith("open") && project != null) {
                MainForm dialog = new MainForm();
                dialog.pack();
                dialog.setTitle(languageManager.getShowText("99"));
                //dialog.setSize(new Dimension(900,650));
                dialog.changeEditorStatus(true, project);
                MainForm.setFormToCenter(dialog);
                dialog.setVisible(true);
                System.exit(0);
            }

        } else {
            MainForm dialog = new MainForm();
            dialog.pack();
            dialog.setTitle(languageManager.getShowText("99"));
            //dialog.setSize(new Dimension(900,650));
            dialog.changeEditorStatus(false, "/home/wcss/测试工程.dpro");
            MainForm.setFormToCenter(dialog);
            dialog.setVisible(true);
            System.exit(0);
        }
    }
}
