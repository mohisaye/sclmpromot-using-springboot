package com.baeldung.application.sclm;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by m_sayekooie on 2/17/2020.
 */
public class FTPUtils {
    static String haspmsg = "$HASP395";   // JES job end message
    static String jclerr = "- JCL ERROR"; // JCL error
    static int rc_fatal = 16;             // fatal error return code
    static int rc_severe = 12;            // severe error return code
    static int rc_error = 8;              // error return code
    static int rc_warning = 4;            // warning return code
    Date curdate = null;                  // current date
    FTPClient ftp = null;                 // FTP client
    DateFormat sdf = null;                // date format

    boolean error = false;
    int minParams = 2;
    int returncode = 0;
    InputStream input = null;
    OutputStream output = null;

    // parameter options
    int retrycount = 0;
    int waitsec = 2;

    // prefix message
    String prefmsg() {
        curdate = new Date();
        sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(curdate);
    }

    public FTPUtils() {
        ftp = new FTPClient();
    }

    public FTPClient connectFtp(String fUserId, String fPassword, String fHost) {
        try {
            // check if the required parameters are set already
            if (fUserId == null | fPassword == null | fHost == null) {
                throw new Exception("UserId or Password is null");
            } else {
                ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                ftp.connect(fHost);

//
                if (ftp.getReplyCode() != 230 && ftp.getReplyCode() != 220)
                    showServerReply(ftp);

                // login using user name and password

                ftp.login(fUserId, fPassword);

                if (ftp.getReplyCode() != 230 && ftp.getReplyCode() != 220)
                    showServerReply(ftp);

                // point the FTP to JES spool
                ftp.site("filetype=jes");
                showServerReply(ftp);
//                System.out.println(replyText);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return ftp;
    }

    public FTPClient connectAndLogin(String fUserId, String fPassword) throws Exception {

        boolean sucess;
        String server = "192.168.180.15";//"127.0.0.1";//"192.168.180.15"+subDirectory;
//            int port = Integer.parseInt();
        String user = fUserId;
        String pass = fPassword;
        ftp = new FTPClient();
//        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftp.connect(server, 21);
        int replyCode = ftp.getReplyCode();
//            showServerReply(ftpClient);
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            ftp.disconnect();
            throw new Exception("Connection to FTP failed based on reply code of: " + replyCode);
        }
//            showServerReply(ftpClient);
        ftp.enterLocalPassiveMode();
        sucess = ftp.login(user, pass);
        if (!sucess) {
            throw new Exception("login fail");
        }

        ftp.setControlEncoding("UTF-8");
        ftp.setAutodetectUTF8(true);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
//            ftpClient.site("ascii");
        FTPClientConfig config = new FTPClientConfig();
        config.setUnparseableEntries(true);
        ftp.configure(config);
        System.out.println(ftp.getReplyString());
        return ftp;
    }

    public void changeDirectory() {
        String server = "192.168.180.15";
        int port = 21;
        String user = "AZM72";
        String pass = "mohi2968";

//        FTPClient ftp = new ftp();

        try {

            ftp.connect(server, port);
            showServerReply(ftp);

            int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Connect failed");
                return;
            }

            boolean success = ftp.login(user, pass);
            showServerReply(ftp);

            if (!success) {
                System.out.println("Could not login to the server");
                return;
            }

            // Changes working directory
            success = ftp.changeWorkingDirectory("azmoon.cics.resource");
            showServerReply(ftp);

            if (success) {
                System.out.println("Successfully changed working directory.");
            } else {
                System.out.println("Failed to change working directory. See server's reply.");
            }

            // logs out
            ftp.logout();
            ftp.disconnect();

        } catch (IOException ex) {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
    }

    public void submitBatchJob() {
        String serverName = "192.168.180.15";
        String userName = "AZM72";
        String password = "mohi2968";
        FTPClient ftp = new FTPClient();
        //Connect to the server
        try {
            ftp.connect(serverName);
            String replyText = ftp.getReplyString();
            System.out.println(replyText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Login to the server
        try {
            ftp.login(userName, password);
            String replyText = ftp.getReplyString();
            System.out.println(replyText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Tell server that the file will have JCL records
        try {
            ftp.site("filetype=jes");
            String replyText = ftp.getReplyString();
            System.out.println(replyText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Submit the job from the text file.Use \\ to avoid using escape notation
        try {
            FileInputStream inputStream = new FileInputStream("D:\\IdeaProjects\\SclmAPI\\promoteFiles\\ProgJOB.txt");
            ftp.storeFile(serverName, inputStream);
            String replyText = ftp.getReplyString();
            System.out.println(replyText);
            String jobnum = "";
            String jesstr = "to JES as ";
            int pos = replyText.indexOf(jesstr);
            if (pos != -1) {
                pos = pos + jesstr.length();
                jobnum = replyText.substring(pos, pos + 8);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Quit the server
        try {
            ftp.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int submitBatchJob(String jclfile, String host, String outputfile, File excelFileName, List<InputDto> newList) {
        String outpulistFile = outputfile + "\\listJcl.out";
        outputfile = outputfile + "\\jcl.out";
        String serverName = "192.168.180.15";
        String userName = "AZM72";
        String password = "mohi2968";
        try {
            connectFtp(userName, password, serverName);
            //Certain JCL format
            ftp.site("filetype=jes");
            ftp.site("jesjobname=*");
            String replyText = ftp.getReplyString();
            System.out.println(replyText);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
                throw new Exception("Set JES format: " + replyText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // submit JCL file
            input = new FileInputStream(jclfile);
            ftp.sendCommand(FTPCmd.CHANGE_TO_PARENT_DIRECTORY);
            ftp.storeFile(host, input);
            String replyString = ftp.getReplyString();
            input.close();

            // check JOB number in JES reply
            String jobnum = "";
            String jesstr = "to JES as ";
            int pos = replyString.indexOf(jesstr);
            if (pos != -1) {
                pos = pos + jesstr.length();
                jobnum = replyString.substring(pos, pos + 8);

                // check JOB number is valid
                if (jobnum.substring(0, 1).equals("J")) {
                    System.out.println(prefmsg() + " submitted " + jclfile
                            + " to " + host + " as " + jobnum + ".");

                    /// set output file name when it is not specified
                    if ((outputfile == null) || (outputfile.length() == 0)) {
                        outputfile = jclfile + "." + jobnum + ".log";
                    }

                    // get JOB output with JOB end until retry count is reached
                    for (int i = 0; true; ) {
                        try {
                            Thread.sleep(1000 * waitsec);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // get JES message log file first
                        output = new FileOutputStream(outputfile);
                        ftp.retrieveFile(jobnum + ".1", output);
                        output.close();

                        // check the reply code
                        replyString = ftp.getReplyString();
                        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                            // get job end message in JES message log file
                            String endmsg = getJobEnd(outputfile);
                            String result = getJobResult(outputfile, userName, "BNEWPROG");
                            log.print(result);
                            System.out.println("\nERROR-->\n\n" + result + "\n\n<--ERROR");
                            // get entire JOB output
                            output = new FileOutputStream(outputfile);
                            ftp.retrieveFile(jobnum, output);
                            output.close();

                            // check the reply code
                            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                                System.out.println(prefmsg() + " created "
                                        + outputfile + " from " + host
                                        + " :\n" + endmsg);

                                 // check job end message
//                                pos = endmsg.indexOf(haspmsg);
//                                if (pos != -1) {

                                // check return code in z/OS V2 message
                                String jobrc;
//                                    pos = endmsg.indexOf("RC=");
                                if (!result.equals("00")) {
                                    jobrc = result;
                                    System.out.println(prefmsg()
                                            + " detected non-zero "
                                            + "return code <" + jobrc
                                            + "> in " + jobnum + ".");
                                    returncode = rc_error;
                                } else {
                                    output = new FileOutputStream(outpulistFile);
                                    ftp.retrieveFile(jobnum + ".4", output);
                                    output.close();
                                    Map<String, String> loadMap = getListFile(outpulistFile, newList);
                                    for (int k = 0; k < loadMap.size(); k++) {
                                        if (loadMap.get(newList.get(k).getLoadName()) != null)
                                            XlsLoadData.writeXLSXFile(excelFileName,newList.get(k).getLoadName() ,loadMap.get(newList.get(k).getLoadName()), 9);
                                    }
                                }
//                                ftp.deleteFile(jobnum);   // delete job
                                break;  // exit for-loop
                            }
                        }

                        // check retry count is reached
                        if (i >= retrycount) {
                            System.out.println(prefmsg() + " failed to get "
                                    + jobnum + " :\n" + replyString);
                            returncode = rc_error;
                            break;  // exit for-loop
                        }

                        // retry to get job output
                        i++;
                        System.out.println(prefmsg() + " retrying to get "
                                + jobnum + " in " + waitsec + " seconds ("
                                + i + ").");
                    }  // end for-loop
                } else {  // invalid JOB number
                    System.out.println(prefmsg() + " submitted "
                            + jclfile + " to " + host
                            + " as invalid JOB <" + jobnum
                            + "> :\n" + replyString);
                    returncode = rc_error;
                }
            } else {
                System.out.println(prefmsg() + " failed to submit "
                        + jclfile + " to " + host + " :\n" + replyString);
                returncode = rc_error;
            }

            ftp.noop(); // check that control connection is working OK

            ftp.logout();
        } catch (FTPConnectionClosedException e) {
            error = true;
            System.out.println(prefmsg()
                    + " got FTP connection closed exception.");
            e.printStackTrace();
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }
        // exit with final message including return code
        int rc = (error ? rc_severe : returncode);
        System.out.println(prefmsg() + " ended with RC=" + rc + ".");
//        System.exit(rc);
        return rc;
    }

    private Map<String, String> getListFile(String outpulistFile, List<InputDto> newList) {
        Map<String, String> loadMapS = new HashMap<>();
        String line = "";
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(outpulistFile);
            br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < newList.size(); i++) {
                    if (line.contains(newList.get(i).getLoadName())) {
                        String loadName = line.substring(1, 9);
                        String status = line.substring(9, line.length()).trim();
                        loadMapS.put(loadName.trim(), status.trim());
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return loadMapS;
    }

    public void storeSeqFile(File fout) throws Exception {
        connectFtp("azm72", "mohi2968", "192.168.180.15");
        showServerReply(ftp);
//        ftpUtils.changeDirectory();
        InputStream in = new FileInputStream(fout);
        ftp.site("filetype=seq");
        ftp.sendCommand(FTPCmd.CHANGE_TO_PARENT_DIRECTORY);
//        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTP.ASCII_FILE_TYPE);
        boolean done = ftp.storeFile("azmoon.cics.resource.list", in);
        showServerReply(ftp);
        in.close();
        if (done)
            System.out.println("FILE IS UPLOADED SUCCESSFULY");
        else
            System.out.println("FILE IS NOT UPLOADED SUCCESSFULLY");
        ftp.logout();
        ftp.disconnect();
    }

    public String getJobEnd(String filePath) {
        String endmsg = "";
        int pos;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            String line = "";
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            // read file
            while ((line = br.readLine()) != null) {
                // check JOB end message
                pos = line.indexOf(haspmsg);
                if (pos == -1) {
                    pos = line.indexOf(jclerr);
                }
                if (pos != -1) {
                    endmsg = line;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return endmsg;
    }

    public String getJobResult(String filePath, String user, String loadName) {
        String gener = "-" + loadName.toUpperCase() + "          STEP";
        int pos;
        String result = "00";
        FileReader fr = null;
        BufferedReader br = null;
        try {
            String line = "";
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            int linePos;
            // read file
            while ((line = br.readLine()) != null) {
                // check JOB end message
                pos = line.indexOf(gener);
                if (pos != -1) {
                    linePos = line.indexOf("STEP");
                    if (!line.substring(linePos + 8, linePos + 10).equals("00")) {
                        result = line.substring(linePos + 12, linePos + 14);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void showServerReply(FTPClient ftp) {
        String[] replies = ftp.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
}
