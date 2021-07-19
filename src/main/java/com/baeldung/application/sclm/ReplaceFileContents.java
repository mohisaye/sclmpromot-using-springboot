package com.baeldung.application.sclm;


import java.io.*;
import java.util.List;

import static com.baeldung.application.sclm.CompareData.PROMOTE_ADDRESS_FILES;

/**
 * Created by m_sayekooie on 02/18/2019.
 */

public class ReplaceFileContents {

    public void replace(File oldFileName, String oldParameter, String oldParameter2, String newParameter, String newParameter2) {
//            String oldFileName = "try.dat";
        String tmpFileName = PROMOTE_ADDRESS_FILES + "tmp_" + oldFileName.getName();

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(oldFileName));
            bw = new BufferedWriter(new FileWriter(tmpFileName));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(oldParameter.toUpperCase())) {
                    line = line.replace(oldParameter.toUpperCase(), newParameter.toUpperCase());
                }
                if (line.contains(oldParameter2.toUpperCase())) {
                    line = line.replace(oldParameter2.toUpperCase(), newParameter2.toUpperCase());
                }
                bw.write(line + "\n");
            }
        } catch (Exception e) {
            return;
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                //
            }
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                //
            }
        }
        // Once everything is complete, delete old file..
        File oldFile = new File(String.valueOf(oldFileName));
        oldFile.delete();

        // And rename tmp file's name to old file name
//        File newFile = new File(tmpFileName);
//        newFile.renameTo(oldFile);

    }

    public void replace(File oldFileName, List<String> oldParameter, List<String> newParameter) {
        String tmpFileName = PROMOTE_ADDRESS_FILES+"tmp_" + oldFileName.getName();

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(oldFileName));
            bw = new BufferedWriter(new FileWriter(tmpFileName));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (line.contains(oldParameter.get(i).toUpperCase())) {
                    line = line.replace(oldParameter.get(i).toUpperCase(), newParameter.get(i).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 1).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 1).toUpperCase(), newParameter.get(i + 1).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 2).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 2).toUpperCase(), newParameter.get(i + 2).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 3).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 3).toUpperCase(), newParameter.get(i + 3).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 4).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 4).toUpperCase(), newParameter.get(i + 4).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 5).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 5).toUpperCase(), newParameter.get(i + 5).toUpperCase());
                }
                bw.write(line + "\n");
            }

        } catch (Exception e) {
            return;
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                //
            }
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                //
            }
        }

    }

    public void replace2(File oldFileName, List<String> oldParameter, List<String> newParameter) {
        String tmpFileName = PROMOTE_ADDRESS_FILES+"tmp_" + oldFileName.getName();

        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(oldFileName));
            bw = new BufferedWriter(new FileWriter(tmpFileName));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (line.contains(oldParameter.get(i).toUpperCase())) {
                    line = line.replace(oldParameter.get(i).toUpperCase(), newParameter.get(i).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 1).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 1).toUpperCase(), newParameter.get(i + 1).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 2).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 2).toUpperCase(), newParameter.get(i + 2).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 3).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 3).toUpperCase(), newParameter.get(i + 3).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 4).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 4).toUpperCase(), newParameter.get(i + 4).toUpperCase());
                }
                if (line.contains(oldParameter.get(i + 5).toUpperCase())) {
                    line = line.replace(oldParameter.get(i + 5).toUpperCase(), newParameter.get(i + 5).toUpperCase());
                }
                bw.write(line + "\n");
            }

        } catch (Exception e) {
            return;
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                //
            }
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                //
            }
        }

    }
}




