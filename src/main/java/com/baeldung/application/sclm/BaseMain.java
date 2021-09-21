package com.baeldung.application.sclm;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPCmd;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baeldung.application.sclm.CompareData.LOCAL_ADDRESS;
import static com.baeldung.application.sclm.CompareData.PROMOTE_ADDRESS_FILES;


/**
 * Created by m_sayekooie on 01/13/2019.
 */

public class BaseMain {
    private String replyText;
    private FTPClient ftp = null;
    FTPUtils ftpUtils = new FTPUtils();

    public static final String host = "192.168.180.15";

    public BaseMain() {

//        ftp = new FTPClient();
    }

    public void readLoadFromIbm(String loadName, String loadPath, String user, String pass) throws Exception {
        try {
            ftp = ftpUtils.connectFtp(user, pass, host);
            ftp.sendCommand(FTPCmd.CHANGE_TO_PARENT_DIRECTORY);
            File file;
            file = new File(LOCAL_ADDRESS + loadName.trim());
            OutputStream outputStream = new FileOutputStream(file);
            ftp.site("filetype=seq");
            ftp.retrieveFile(loadPath + "(" + loadName.trim() + ")", outputStream);
            System.out.println("\nGETLOAD " + ftp.getReplyString());

        } catch (Exception e) {
            throw new Exception("$$$\nERROR In Method \b@readLoadFromIbm has been occure.");

        } finally {
            ftp.quit();
        }
    }

    public String doPromote(String user, String pass, String load, String project, String group, String loadType) {
        ReplaceFileContents fileContents = new ReplaceFileContents();
        JCLSubmitter jclSubmitter = new JCLSubmitter();
        File file = new File(PROMOTE_ADDRESS_FILES + "promote.txt");
        File outfile = new File(PROMOTE_ADDRESS_FILES);
        int rc = -1;
        String gres = null;
        int counter = 10;
        File file1 = new File(PROMOTE_ADDRESS_FILES + "tmp_promote.txt");

//        File file1 = new File("promoteFiles/tmp_promote.txt");
        try {
            List<String> oldParam = new ArrayList<>();
            oldParam.add(0, "<USERNAME>");
            oldParam.add(1, "<SRCNAME>");
            oldParam.add(2, "<PROJECT>");
            oldParam.add(3, "<GROUP>");
            oldParam.add(4, "<EXIT_NUM>");
            oldParam.add(5, "<TYPE>");
            List<String> newParam = new ArrayList<>();
            String exitNum = "EXIT" + String.valueOf(counter);
            counter++;
            newParam.add(0, user);
            newParam.add(1, load);
            newParam.add(2, project);
            newParam.add(3, group);
            newParam.add(4, exitNum);
            newParam.add(5, loadType);
            fileContents.replace(file, oldParam, newParam);

            ftp=ftpUtils.connectFtp(user, pass, host);
             gres= jclSubmitter.ftpProcess(ftp, file1.getAbsolutePath(), host, outfile.getAbsolutePath(), user, load);
            
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            File f2 = new File(outfile.getAbsolutePath() + "jcl.out");
//            file1.delete();
//            f2.delete();
        }
        return gres;
    }

    public String changeAuthorizationCode(String user, String pass, String loadName, String loadType, String group, String project, String fromAuth, String toAuth) {
//        https://www.ibm.com/support/knowledgecenter/SSLTBW_2.3.0/com.ibm.zos.v2r3.f54pd00/cmfserv.htm
        ReplaceFileContents fileContents = new ReplaceFileContents();
        JCLSubmitter jclSubmitter = new JCLSubmitter();

        File file = new File(PROMOTE_ADDRESS_FILES + "RUNSCRIPT.txt");
        File outfile = new File(PROMOTE_ADDRESS_FILES);
        int rc = -1;
        String gres = null;
        File file1 = new File(PROMOTE_ADDRESS_FILES + "tmp_RUNSCRIPT.txt");
        File fileBatch = new File(PROMOTE_ADDRESS_FILES + "releaseBatch.txt");
        try {
            List<String> oldParam = new ArrayList<>();
            oldParam.add(0, "<PROJECT>");
            oldParam.add(1, "<GROUP>");
            oldParam.add(2, "<TYPE>");
            oldParam.add(3, "<MEMBER>");
            oldParam.add(4, "<FROM_AUTHCODE>");
            oldParam.add(5, "<TO_AUTHCODE>");
            List<String> newParam = new ArrayList<>();
            newParam.add(0, project);
            newParam.add(1, group);
            newParam.add(2, loadType);
            newParam.add(3, loadName);
            newParam.add(4, fromAuth);
            newParam.add(5, toAuth);
            fileContents.replace2(file, oldParam, newParam);
            ftp=ftpUtils.connectFtp(user, pass, host);
            FileInputStream fileInputStream = new FileInputStream(file1);
            ftp.sendCommand(FTPCmd.CHANGE_TO_PARENT_DIRECTORY);
            ftp.site("filetype=seq");
            ftp.storeFile("AZM72.jcl(RUNSCRIP)", fileInputStream);
            gres = jclSubmitter.ftpProcess(ftp, fileBatch.getAbsolutePath(), host, outfile.getAbsolutePath(), user, "RELEASE");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            File f2 = new File(outfile.getAbsolutePath() + "\\jcl.out");
            file1.deleteOnExit();
            f2.delete();
        }
        return gres;
    }

    public Boolean getSizeFtp(String user, String pass, String loadPath, String loadName, String fileSize) throws Exception {
        boolean _isSameSize = false;
        try {
            ftp = ftpUtils.connectAndLogin(user, pass);
            ftp.sendCommand(FTPCmd.CHANGE_TO_PARENT_DIRECTORY);
            ftp.sendCommand(FTPCmd.TYPE);
            ftp.changeWorkingDirectory(loadPath);
            FTPFile[] files = ftp.listFiles("");
            for (FTPFile f : files) {
//                System.out.println(f.getRawListing());
                if (f.getRawListing().startsWith(loadName)) {
                    System.out.println(f.getRawListing());
                    String s = "";
                    if (f.getRawListing().indexOf(fileSize.substring(2, 8)) != -1) {
                        String a = f.getRawListing().substring(f.getRawListing().indexOf(fileSize.substring(2, 8)), f.getRawListing().indexOf(fileSize.substring(2, 8)) + 6);
                        s = a;
                        fileSize = StringUtils.leftPad(fileSize, 8, '0');
                        String realSize = s;
                        realSize = StringUtils.leftPad(realSize, 8, '0');
                        if (realSize.equals(fileSize)) {
                            _isSameSize = true;
                            break;
                        } else {
                            _isSameSize = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            ftp.quit();
        }
        return _isSameSize;
    }

    public Boolean isLoadExist(String user, String pass, String loadPath, String loadName) throws Exception {
        boolean _isLoadExist = false;
        try {
            ftp = ftpUtils.connectAndLogin(user, pass);
            ftp.sendCommand(FTPCmd.CHANGE_TO_PARENT_DIRECTORY);
            ftp.sendCommand(FTPCmd.TYPE);
            ftp.changeWorkingDirectory(loadPath);
            FTPFile[] files = ftp.listFiles("");
            for (FTPFile f : files) {
                if (f.getRawListing().startsWith(loadName)) {
                    _isLoadExist = true;
                    break;
                } else {
                    _isLoadExist = false;
                }
            }
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                ftp.quit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _isLoadExist;
    }

    public void doNewProg(List<InputDto> loadInfoNew, File excelFileName) throws Exception {
        String cicsName = null;
        int Res = -1;
        int Res2 = -1;
        File outfile = new File(PROMOTE_ADDRESS_FILES);
        for (int i = 0; i < 2; i++) {
            cicsName = loadInfoNew.get(i).getCicsName();
        }
        createNewProgFile(loadInfoNew);
        if (!cicsName.equals("null") && cicsName.equals("CICSBMI")) {
            modifyNewProgJOB(cicsName);
            Res = ftpUtils.submitBatchJob(PROMOTE_ADDRESS_FILES + "BNEWPROG.txt", "192.168.180.15", outfile.getAbsolutePath(), excelFileName, loadInfoNew);
            cicsName = "";//TODO Name
            modifyNewProgJOB(cicsName);
            Res2 = ftpUtils.submitBatchJob(PROMOTE_ADDRESS_FILES + "BNEWPROG.txt", "192.168.180.15", outfile.getAbsolutePath(), excelFileName, loadInfoNew);
        } else if (!cicsName.equals("null") && cicsName.equals("CICSBSI")) {
            modifyNewProgJOB(cicsName);
        }
//        int Res = ftpUtils.submitBatchJob(PROMOTE_ADDRESS_FILES + "BNEWPROG.txt", "192.168.180.15", outfile.getAbsolutePath(), excelFileName, loadInfoNew);
        if (Res != 0 || Res2 != 0) {
            throw new Exception("running BNEWPROG was UNSuccess");
        }
    }

    public void createNewProgFile(List<InputDto> loads) throws Exception {
        File fin = new File(PROMOTE_ADDRESS_FILES + "azmoon.cics.resource.list.txt");
//        File fout = new File("D:\\IdeaProjects\\SclmAPI\\promoteFiles\\azmoon.cics.resource.list2.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(fin));
        BufferedReader br = new BufferedReader(new FileReader(fin));
        for (int i = 0; i < loads.size(); i++) {
            String loadn = StringUtils.rightPad(loads.get(i).getLoadName(), 8, ' ') + ",";
            bw.write(loadn);
            bw.write("\r\n");
        }
        bw.close();
        ftpUtils.storeSeqFile(fin);

    }

    public void modifyNewProgJOB(String cicsName)  {
        File orginFile = new File(PROMOTE_ADDRESS_FILES + "orgBNEWPROG.txt");
        File fileToBeModified = new File(PROMOTE_ADDRESS_FILES + "BNEWPROG.txt");
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(orginFile));
            //Reading all the lines of input text file into oldContent
            String line = reader.readLine();
            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }
            //Replacing oldString with newString in the oldContent
            String newContent = oldContent.replaceAll("<CicsName>", cicsName.toUpperCase().trim());
            //Rewriting the input text file with newContent
            writer = new FileWriter(fileToBeModified);
            System.out.println(newContent);
            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean compareDate(String dateTime, String loadName)  {
        boolean check = false;
        File file = new File(LOCAL_ADDRESS + loadName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile(dateTime);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    check = true;
                }
            }

        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        } finally {
            File f = new File(LOCAL_ADDRESS + loadName);
            if (f.exists()) {
                f.delete();
            }
        }
        if (check)
            return true;
        else
            return false;
    }

    //    public void getDataSetFromIbm(File dataSet, String user, String pass) throws Exception {
//        try {
//            //Submit the job from the text file.Use \\ to avoid using escape notation
////            connectFtp(user, pass, host);
//            connectAndLogin(user, pass);
//            ftp.sendCommand(FTPCmd.CHANGE_TO_PARENT_DIRECTORY);
////            System.out.printf(ftp.getReplyString());
//            ftp.sendCommand(FTPCmd.SITE_PARAMETERS, "seq");
////            System.out.println("\nftp.sendCommand: " + ftp.getReplyString());
////            File file = new File(dataSet);
//            OutputStream outputStream = new FileOutputStream(dataSet);
//            ftp.site("filetype=seq");
//            ftp.retrieveFile(dataSet.getName(), outputStream);
//            System.out.println("ftp.retrieveFile: " + ftp.getReplyString());
//            if (ftp.getReplyCode() == 450) {
//                throw new Exception(dataSet.getName() + " in used!!!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("$$$\nERROR In Method \b@getDataSetFromIbm has been occure.");
//        } finally {
//            ftp.quit();
//        }
//    }
//
//    public void setDataSetToIbm(File localPath, String qsamIBMPath, String user, String pass) throws Exception {
//        int replyCode = 0;
////        connectFtp(user, pass, host);
//        connectAndLogin(user, pass);
//        try {
//            ftp.sendCommand(FTPCmd.CHANGE_TO_PARENT_DIRECTORY);
////            System.out.println(ftp.getReplyString());
//
//            FileInputStream fileInputStream = new FileInputStream(localPath);
//            ftp.site("filetype=seq");
//            ftp.storeFile(qsamIBMPath, fileInputStream);
//            System.out.println(ftp.getReplyString());
//
//            replyCode = ftp.getReplyCode();
//            if (replyCode == 450) {
//                throw new Exception(qsamIBMPath + " in used!!!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("$$$\nERROR In Method \b@setDataSetToIbm has been occure.");
//        } finally {
//            //Quit the server
//            ftp.quit();
//        }
//    }
//
//    public void writeQsam(File file, String[] ss) throws Exception {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
//            for (int i = 0; i < ss.length; i++) {
//                String byRow = ss[i];
//                if (!byRow.equals("")) {
//                    String size = String.valueOf(byRow).split(",")[1];
//                    String loadName = String.valueOf(byRow).split(",")[0].toUpperCase().trim();
//                    String loadNameSize;
//                    if (loadName.length() < 8) {
//                        loadNameSize = loadName + "  " + size;
//                    } else {
//                        loadNameSize = loadName + " " + size;
//                    }
//                    bw.write(loadNameSize);
//                    bw.write("\n");
//                }
//            }
//        } catch (Exception e) {
//            throw new Exception("Write qsam file problem!!!");
//        }
//    }
//
//    public void modifyFile(File file, String replacement, String loadPath) {
//        try {
//            ReplaceFileContents replaceFileContents = new ReplaceFileContents();
//            replaceFileContents.replace(file, "<user>", "<LoadsPath>", replacement, loadPath);
//        } catch (Exception e) {
//            e.getMessage();
//        }
//    }

    //    public void subBatch(String user, String pass, String loadPath) {
//        try {
//            readLoadFromIbm("listmem", "azm24.bmid.jcl", user, pass);
//            File file = new File(LOCAL_ADDRESS + "listmem.txt");
//            modifyFile(file, user.toUpperCase() + "S", loadPath.toUpperCase());
//            File file1 = new File("E:\\IdeaProjects\\RestEasy-UP-DOWN-Excel-File\\promoteFiles\\tmp_listmem.txt");
////            connectFtp(user, pass, host);
//            connectAndLogin(user, pass);
//            FileInputStream inputStream = new FileInputStream(file1);
//            ftp.site("filetype=jes");
//            ftp.storeFile(host, inputStream);
//            System.out.println("\nreplyText for sub: " + ftp.getReplyString() + "\n");
//            Thread.sleep(1000);
//            ftp.quit();
//        } catch (
//                Exception e
//                ) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean getJesFile(String user, String pass) {
//        boolean _hasJclError = true;
//        try {
////            connectFtp(user, pass, host);
//            connectAndLogin(user, pass);
//            // Get a list of jobs
//            String[] names = ftp.listNames("*");
//
//            // Retrieve part of a job log file
//            String sRemoteFilename = names[0] + ".1";
//            InputStream is = ftp.retrieveFileStream(sRemoteFilename);
//            BufferedReader br = new BufferedReader(new InputStreamReader((is)));
//            int linePos;
//            int pos;
//            String result = "";
//            String line;
//            while ((line = br.readLine()) != null) {
//                pos = line.indexOf("-" + user.toUpperCase() + "S            STEP");
//                if (pos != -1) {
//                    linePos = line.indexOf("STEP");
//                    result = "00";
//                    if (line.substring(linePos + 12, linePos + 14).equals("04")) {
//                        result = line.substring(linePos + 12, linePos + 14);
//                    }
//                    if (line.substring(linePos + 12, linePos + 14).equals("08")) {
//                        result = line.substring(linePos + 12, linePos + 14);
//                    }
//                }
////                Pattern jclSizeError = Pattern.compile("STEP        04");
////                Pattern jclLoadError = Pattern.compile("STEP        08");
////                Matcher sizeError = jclSizeError.matcher(line);
////                Matcher loadError = jclLoadError.matcher(line);
////                while (sizeError.find() || loadError.find()) {
////                    if (!sizeError.group().equals("")) {
////                        System.out.println(sizeError.group());
////                        _hasJclError = false;
////                        break;
////                    } else if (!loadError.group().equals("")) {
////                        System.out.println(loadError.group());
////                        _hasJclError = false;
////                        break;
////                    }
////
////                }
//            }
//            if (!result.equals("00") && !result.equals("")) {
//                _hasJclError = false;
//                if (result.equals("04"))
//                    System.out.println("\nSIZE LOAD MOGHAYER AST");
//                if (result.equals("08"))
//                    System.out.println("\nLOAD VOJOOD NADARAD!!!!");
//            } else {
//                _hasJclError = true;
//            }
//            is.close();
//            br.close();
//            ftp.completePendingCommand();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                ftp.quit();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return _hasJclError;
//    }
//
//    public void promote(String user, String pass, String load, String project, String group) {
//        ReplaceFileContents fileContents = new ReplaceFileContents();
//        File file = new File(".\\jesFile\\promote.txt");
//        int counter = 10;
//        try {
//            List<String> oldParam = new ArrayList<>();
//            oldParam.add(0, "<USERNAME>");
//            oldParam.add(1, "<SRCNAME>");
//            oldParam.add(2, "<PROJECT>");
//            oldParam.add(3, "<GROUP>");
//            oldParam.add(4, "<EXIT_NUM>");
//            List<String> newParam = new ArrayList<>();
//            String exitNum = "EXIT" + String.valueOf(counter);
//            counter++;
//            newParam.add(0, user);
//            newParam.add(1, load);
//            newParam.add(2, project);
//            newParam.add(3, group);
//            newParam.add(4, exitNum);
//            fileContents.replace(file, oldParam, newParam);
//            File file1 = new File("tmp_promote.txt");
////            connectFtp(user, pass, host);
//            connectAndLogin(user, pass);
//            FileInputStream inputStream = new FileInputStream(file1);
//            ftp.site("filetype=jes");
//            ftp.storeFile(host, inputStream);
//            System.out.println("\nreplyText for sub: " + ftp.getReplyString() + "\n");
//            Thread.sleep(1000);
//            ftp.quit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            File f = new File("./tmp_promote.txt");
//            f.delete();
//        }
//    }

//    public static void main(String[] args) {
//        BaseMain bm = new BaseMain();
//        List<InputDto> l1 = new ArrayList<>();
//        InputDto inp = new InputDto();
//        InputDto inp1 = new InputDto();
////        inp.setLoadName("loadn1");
////        l1.add(inp);
////        inp.setLoadName("loadn2");
////        l1.add(inp);
//        inp1.setLoadName("ADUE2288");
//        inp.setLoadName("ATSTSMT0");
//        l1.add(inp);
//        l1.add(inp1);
////        bm.changeAuthorizationCode("azm72", "saye7705", "ATSTSMT0", "ARCHBIND", "AZMOON", "LBTSLIB", "AZM", "U30");
//        try {
//            bm.createNewProgFile(l1);
//            bm.modifyNewProgJOB("cicsBMI");
//            bm.doNewProg(l1, new File("D://Users//m_sayekooie//Desktop//12517.xlsx"));
//            System.out.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
