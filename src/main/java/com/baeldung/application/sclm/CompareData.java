package com.baeldung.application.sclm;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.baeldung.application.sclm.XlsLoadData.writeXLSXFile;


/**
 * Created by m_sayekooie on 01/13/2019.
 */
public class CompareData {

    //    ResourceBundle bundle = ResourceBundle.getBundle("Application");
    public static final String LOCAL_ADDRESS = "E:\\IdeaProjects\\SclmAPI\\loads\\";
    public static final String PROMOTE_ADDRESS_FILES = "E:\\IdeaProjects\\SclmAPI\\promoteFiles\\";



    public String changeGroupPath(String group, String project) throws Exception {
        String s = null;
        if (group.toUpperCase().equals("TRANSIT")) {
            s = project + ".AZMOON.LOAD";
        } else if (group.toUpperCase().equals("AZMOON")) {
            s = project + ".RELEASE.LOAD";
        }
        return s;
    }

    public int compare(File excelFile, String user, String pass) {
        List<InputDto> listforNew = null;
        XlsLoadData loadData=new XlsLoadData();
        int res = 0;
        try {
            listforNew = new ArrayList<>();
            BaseMain main = new BaseMain();
            String loadPathNext;
            String project = "";
            String group = "";
            List<InputDto> loadInfo = loadData.readXLSXFile2(excelFile);
            if (loadInfo.size() == 0) {
                res = -1;
            }
            for (int i = 0; i < loadInfo.size(); i++) {
                System.out.println(">>>>>>>>>>>>>>>>>>>" + loadInfo.get(i).getRowId() + "<<<<<<<<<<<<<<<<<<<<<<<");
                if (loadInfo.get(i).getLoadPath() != null) {
                    project = loadInfo.get(i).getLoadPath().split("\\.")[0].trim();
                    group = loadInfo.get(i).getLoadPath().split("\\.")[1].trim();
                } else {
                    throw new Exception("Error in input PATH");
                }

                if (main.isLoadExist(user, pass, loadInfo.get(i).getLoadPath(), loadInfo.get(i).getLoadName())) {
                    if (!(loadInfo.get(i).getLoadSize() == null) && main.getSizeFtp(user, pass, loadInfo.get(i).getLoadPath(), loadInfo.get(i).getLoadName(), loadInfo.get(i).getLoadSize())) {
                        main.readLoadFromIbm(loadInfo.get(i).getLoadName(), loadInfo.get(i).getLoadPath(), user, pass);
                        if (!(loadInfo.get(i).getLoadDate() == null) && main.compareDate(loadInfo.get(i).getLoadDate(), loadInfo.get(i).getLoadName())) {
                            System.out.println("---------------" + "**" + loadInfo.get(i).getLoadName() + "**");
                            String gres = main.doPromote(user, pass, loadInfo.get(i).getLoadName(), project, group, loadInfo.get(i).getLoadType());
                            int rc=Integer.parseInt(gres.split("-num:")[0]);
                            String jobnum=gres.split("-num:")[1];
                            if (rc != 0) {
                                writeXLSXFile(excelFile, loadInfo.get(i).getLoadName(), 1,"");
                            } else {
                                listforNew.add(loadInfo.get(i));
                                writeXLSXFile(excelFile, loadInfo.get(i).getLoadName(), 4,jobnum);
                                loadPathNext = changeGroupPath(group, project);
                                if (main.getSizeFtp(user, pass, loadPathNext, loadInfo.get(i).getLoadName(), loadInfo.get(i).getLoadSize())) {
                                    main.readLoadFromIbm(loadInfo.get(i).getLoadName(), loadPathNext, user, pass);
                                    if (!main.compareDate(loadInfo.get(i).getLoadDate(), loadInfo.get(i).getLoadName())) {
                                        writeXLSXFile(excelFile, loadInfo.get(i).getLoadName(), 2,"");
                                    } else {
                                        writeXLSXFile(excelFile, loadInfo.get(i).getLoadName(), 6,"");
                                    }
                                } else {
                                    writeXLSXFile(excelFile, loadInfo.get(i).getLoadName(), 3,"");
                                }
                            }
                        } else {
                            writeXLSXFile(excelFile, loadInfo.get(i).getLoadName(), 2,"");
                        }
                    } else {
                        writeXLSXFile(excelFile, loadInfo.get(i).getLoadName(), 3,"");
                    }
                } else {
                    writeXLSXFile(excelFile, loadInfo.get(i).getLoadName(), 5,"");
                }
            }
            if (listforNew.size() != 0) {
                if (listforNew.get(0).getCicsName().equals("cicsbmi")) {
                    if (listforNew.size() != 0)
                        main.doNewProg(listforNew, excelFile);
                }
            }
            System.out.println("********TRANSFER DONE!********");
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().equals("Error in input PATH"))
                return res = -1;
        }
        return res;
    }

    public int loadRelease(File excelFile, String user, String pass, String toAuthCode) {
        int res = 0;
        XlsLoadData loadData=new XlsLoadData();
        try {
            List<InputDto> loadInfo = loadData.readXLSXFile2(excelFile);
            BaseMain main = new BaseMain();
            for (int i = 0; i < loadInfo.size(); i++) {
                System.out.println(loadInfo.get(i).getRowId() + "*****************");
                String project = loadInfo.get(i).getLoadPath().split("\\.")[0].trim();
                String group = loadInfo.get(i).getLoadPath().split("\\.")[1].trim();
                String loadName = loadInfo.get(i).getLoadName();
                String loadType = "SOURCE";
                String fromAuthCode = "AZM";
                if (main.isLoadExist(user, pass, loadInfo.get(i).getLoadPath(), loadInfo.get(i).getLoadName())) {
                    String gres = main.changeAuthorizationCode(user, pass, loadName, loadType, group, project, fromAuthCode, toAuthCode);
                    int rc=Integer.parseInt(gres.split("-num:")[0]);
                    if (rc != 0) {
                        writeXLSXFile(excelFile, loadName, 7,"");
                    } else {
                        writeXLSXFile(excelFile, loadName, 8,"");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

//    public static void main(String[] args) {
//
//
//        CompareData cd = new CompareData();
//        // locDataInPath  "E:\\transfer\\loads\\AMBLISTD.DATA"
//        System.out.println("Enter your load path: ");
//        Scanner scanner = new Scanner(System.in);

//        String loadPath = scanner.nextLine();
//        System.out.println("Enter your Excel file path: ");
//        String excelFile = scanner.nextLine();
//        File file = new File(excelFile);
//        System.out.println("Enter your user: ");
//        String user = scanner.nextLine();
//        System.out.println("Enter your pass: ");
//        String pass = scanner.nextLine();
//        try {
//            cd.loadRelease(file, "azm72", "saye7705", "U29");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
