package com.baeldung.application.sclm;

/**
 * Created by m_sayekooie on 01/21/2019.
 */

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class JCLSubmitter {
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

    public String ftpProcess(FTPClient ftp, String jclfile, String host, String outputfile, String user, String loadName) {
        outputfile = outputfile + "\\jcl.out";

        try {                           //Certain JCL format
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
        String jobnum = "";
        try {
            // submit JCL file
            input = new FileInputStream(jclfile);
            ftp.storeFile(host, input);
            String replyString = ftp.getReplyString();
            input.close();

            // check JOB number in JES reply

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
                            String result = getJobResult(outputfile, user, loadName);
                            log.print(result);
                            System.out.println("\nERROR-->\n\n"+result+"\n\n<--ERROR");
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
                                    if (jobrc.equals("08")) {
                                        System.out.println(prefmsg()
                                                + " detected non-zero "
                                                + "return code <" + jobrc
                                                + "> in " + jobnum + ".");
                                        returncode = rc_error;
                                    } else if (jobrc.equals("04")) {
                                        System.out.println(prefmsg()
                                                + " detected non-zero "
                                                + "return code <" + jobrc
                                                + "> in " + jobnum + ".");
                                        returncode = rc_warning;
                                    } else if (result.startsWith("JCL ERROR")) {
                                        returncode = rc_fatal;
                                    }

//                                    }
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
        String generalResult=String.valueOf(rc)+"-num:"+jobnum;
        return generalResult;
    }

    // get line including haspmsg or jclerr in file
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
        String gener = "-" + user.toUpperCase() + "$   " + loadName.toUpperCase() + " GENER0";
        String prmdel = "-" + user.toUpperCase() + "$   " + loadName.toUpperCase() + " PRMDEL0";
        String promo = "-" + user.toUpperCase() + "$   " + loadName.toUpperCase() + " PROM0";
        String jclError = "JOB NOT RUN - JCL ERROR";
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
                    linePos = line.indexOf("GENER0");
                    if (line.substring(linePos + 12, linePos + 14).equals("04")) {
                        result = line.substring(linePos + 12, linePos + 14);
                    }
                    if (line.substring(linePos + 12, linePos + 14).equals("08")) {
                        result = line.substring(linePos + 12, linePos + 14);
                    }
                }
                pos = line.indexOf(prmdel);
                if (pos != -1) {
                    linePos = line.indexOf("PRMDEL0");
                    if (line.substring(linePos + 12, linePos + 14).equals("04")) {
                        result = line.substring(linePos + 12, linePos + 14);
                    }
                    if (line.substring(linePos + 12, linePos + 14).equals("08")) {
                        result = line.substring(linePos + 12, linePos + 14);
                    }
                }
                pos = line.indexOf(promo);
                if (pos != -1) {
                    linePos = line.indexOf("PROM0");
                    if (line.substring(linePos + 12, linePos + 14).equals("04")) {
                        result = line.substring(linePos + 12, linePos + 14);
                    }
                    if (line.substring(linePos + 12, linePos + 14).equals("08")) {
                        result = line.substring(linePos + 12, linePos + 14);
                    }
                }
                pos = line.indexOf(jclError);
                if (pos != -1) {
                    linePos = line.indexOf("JCL ERROR");
                    if (line.substring(linePos, linePos + 9).equals("JCL ERROR")) {
                        result = line.substring(linePos);
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
}
