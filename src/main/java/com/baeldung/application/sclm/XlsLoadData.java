package com.baeldung.application.sclm;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by m_sayekooie on 01/13/2019.
 */

public class XlsLoadData {

    public static List<InputDto> readXLSXFile(File file) throws Exception {
//        String[] ss = new String[0];
        List<InputDto> inputDtos = new ArrayList<>();

        Integer i = 0;
        InputStream ExcelFileToRead = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

        XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();
//        ss = new String[sheet.getLastRowNum()];
        sheet.getLastRowNum();
        while (rows.hasNext()) {
            InputDto input = new InputDto();
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            if (row.getRowNum() == 0) {
                continue; //just skip the rows if row number is 0 or 1
            }
            String s = "";
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();

                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//                        System.out.print(cell.getStringCellValue() + " ");
                    s += cell.getStringCellValue().trim() + "|";
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//                        System.out.print(cell.getNumericCellValue() + " ");
                    s += cell.getRawValue().trim() + "|";
                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
//                        System.out.print(cell.getNumericCellValue() + " ");
                    s += cell.getStringCellValue().trim() + "|";
                }
                    /*else {
                        //U Can Handel Boolean, Formula, Errors
                    }*/
            }
            if (!s.equals("") && s.split("\\|").length == 8) {
                input.setLoadName(s.split("\\|")[6]);
                input.setLoadSize(s.split("\\|")[1]);
                input.setLoadDate(s.split("\\|")[0]);
                input.setLoadPath(s.split("\\|")[4]);
                input.setLoadType(s.split("\\|")[2]);
                input.setCicsName(s.split("\\|")[5]);
                input.setRowId(s.split("\\|")[7]);
                System.out.println(input.getRowId());
                inputDtos.add(input);
//                ss[i] = s;

            } else {
                throw new Exception("EXCEL DATA IS NOT COMPELETED");
            }
            i++;
        }

        return inputDtos;
    }


    public  List<InputDto> readXLSXFile2(File file) throws Exception {
        List<InputDto> inputDtos = new ArrayList<>();
        InputStream ExcelFileToRead = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
        XSSFWorkbook test = new XSSFWorkbook();
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;
        Iterator rows = sheet.rowIterator();
        while (rows.hasNext()) {
            InputDto input;
            row = (XSSFRow) rows.next();
            System.out.println(row.getCell(row.getFirstCellNum()));
            row.getCell(row.getFirstCellNum()).setCellType(Cell.CELL_TYPE_STRING);
            if (row.getCell(row.getFirstCellNum()).getStringCellValue().equals("LOAD\nDate and Time")) {
                row = (XSSFRow) rows.next();
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                while (!(row.getCell(0).getStringCellValue().equals("تاح?ضوت") || row.getCell(0).getStringCellValue().equals("تاحيضوت"))) {
                    input = new InputDto();
                    for (int j = 0; j < 9; j++) {
                        if (row.getCell(j) != null)
                            row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    }
                    input.setLoadDate(row.getCell(0).getStringCellValue());
                    input.setLoadSize(row.getCell(2).getStringCellValue());
                    input.setLoadType("ARCHBIND");
                    input.setLoadPath(row.getCell(4).getStringCellValue());
                    input.setCicsName(row.getCell(5).getStringCellValue());
                    input.setLoadName(row.getCell(7).getStringCellValue());
                    input.setRowId(row.getCell(8).getStringCellValue());
                    row = (XSSFRow) rows.next();
                    row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                    inputDtos.add(input);
                }

            }
        }
        return inputDtos;
    }

    public static void writeXLSXFile(List<String> lists, File file, int x, int y) {
        try {
            InputStream ExcelFileToRead = new FileInputStream(file);
            // Obtain a workbook from the excel file
            XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(ExcelFileToRead);

            // Get Sheet at index 0
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum2 = findRow(sheet, "FOTSBM00");//t

            // Get Row at index 1
            XSSFRow row;//= sheet.getRow(2);
            row = sheet.getRow(rowNum2);//t
            XSSFCell cell2 = row.getCell(row.getFirstCellNum());//t


            // Get the Cell at index 2 from the above row
//            XSSFCell cell = row.createCell(5);

            // Create a Font for styling header cells
            Font cellFont = workbook.createFont();
            cellFont.setBoldweight((short) 3);
            cellFont.setFontHeightInPoints((short) 14);
            cellFont.setColor(IndexedColors.RED.getIndex());

            // Create a CellStyle with the font
            CellStyle CellStyle = workbook.createCellStyle();
            cell2.setCellStyle(CellStyle);//t
            CellStyle.setFont(cellFont);
            int rowNum = x;
            for (int i = 0; i < lists.size(); i++) {
                row = sheet.getRow(rowNum++);
                if (row == null) {
                    sheet.createRow(rowNum);
                }
                XSSFCell cell = row.createCell(y);
                cell.setCellValue(lists.get(i));
                cell.setCellStyle(CellStyle);

            }
            String[] columns = new String[7];
            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // Write the output to the file
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized static void writeXLSXFile(File file, String content, int flag,String jobnum) {
        try {
            InputStream ExcelFileToRead = new FileInputStream(file);
            // Obtain a workbook from the excel file
            XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(ExcelFileToRead);
            ExcelFileToRead.close();
            // Get Sheet at index 0
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum2 = findRow(sheet, content);//t

            // Get Row at index 1
            XSSFRow row;//= sheet.getRow(2);
            row = sheet.getRow(rowNum2);//t

            // Create a CellStyle with the font
            CellStyle CellStyle = workbook.createCellStyle();
            sheet.setDisplayGridlines(false);
            // Get the Cell at index 2 from the above row
//            XSSFCell cell = row.createCell(5);

            // Create a Font for styling header cells
            if (flag == 1) {
                XSSFCell cell2 = row.getCell(row.getFirstCellNum() + 6);//t
                cell2.getStringCellValue().concat("__");
                String s = cell2.getStringCellValue().concat("__");
                cell2.setCellValue(s);
                Font cellFontpromote = workbook.createFont();
                cellFontpromote.setBoldweight((short) 4);
                cellFontpromote.setFontHeightInPoints((short) 15);
                cellFontpromote.setColor(IndexedColors.RED.getIndex());
                CellStyle.setFont(cellFontpromote);
                cell2.setCellStyle(CellStyle);
            } else if (flag == 2) {
                XSSFCell cell2 = row.getCell(row.getFirstCellNum());//t
                Font cellFontDate = workbook.createFont();
                cellFontDate.setBoldweight((short) 3);
                cellFontDate.setFontHeightInPoints((short) 14);
                cellFontDate.setColor(IndexedColors.BLUE.getIndex());
                CellStyle.setFont(cellFontDate);
                cell2.setCellStyle(CellStyle);
                cell2 = row.createCell(row.getLastCellNum() + 2);//t
                cell2.setCellValue("Problem in load Date");
            } else if (flag == 3) {
                XSSFCell cell2 = row.getCell(row.getFirstCellNum() + 1);//t
                Font cellFontSize = workbook.createFont();
                cellFontSize.setBoldweight((short) 3);
                cellFontSize.setFontHeightInPoints((short) 13);
                cellFontSize.setColor(IndexedColors.ORANGE.getIndex());
                CellStyle.setFont(cellFontSize);
                cell2.setCellStyle(CellStyle);
                cell2 = row.createCell(row.getLastCellNum() + 2);//t
                cell2.setCellValue("Problem in load size");
            } else if (flag == 4) {
                XSSFCell cell2 = row.getCell(row.getFirstCellNum() + 6);//t
                Font cellFontPromoteOk = workbook.createFont();
                cellFontPromoteOk.setBoldweight((short) 3);
                cellFontPromoteOk.setFontHeightInPoints((short) 13);
                cellFontPromoteOk.setColor(IndexedColors.DARK_GREEN.getIndex());
                CellStyle.setFont(cellFontPromoteOk);
                cell2.setCellStyle(CellStyle);
                cell2 = row.createCell(row.getLastCellNum() + 2);//t
                cell2.setCellValue("load promoted successfully:"+ jobnum);
            } else if (flag == 5) {
                XSSFCell cell2 = row.getCell(row.getFirstCellNum() + 6);//t
                Font cellFontNotExist = workbook.createFont();
                cellFontNotExist.setBoldweight((short) 3);
                cellFontNotExist.setFontHeightInPoints((short) 13);
                cellFontNotExist.setColor(IndexedColors.YELLOW.getIndex());
                CellStyle.setFont(cellFontNotExist);
                CellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
                cell2.setCellStyle(CellStyle);
                cell2 = row.createCell(row.getLastCellNum() + 3);
                cell2.setCellValue("load not exist");
            } else if (flag == 6) {
                XSSFCell cell2 = row.getCell(row.getFirstCellNum());//t
                Font cellFontDateAfter = workbook.createFont();
                cellFontDateAfter.setBoldweight((short) 3);
                cellFontDateAfter.setFontHeightInPoints((short) 14);
                cellFontDateAfter.setColor(IndexedColors.GREEN.getIndex());
                CellStyle.setFont(cellFontDateAfter);
                cell2.setCellStyle(CellStyle);
            } else if (flag == 7) {
                XSSFCell cell2 = row.createCell(row.getLastCellNum() + 1);//t
                cell2.setCellValue("Release Fail");
            } else if (flag == 8) {
                XSSFCell cell2 = row.createCell(row.getLastCellNum() + 1);//t
                cell2.setCellValue("Release Done");
            }
//            cell2.setCellStyle(CellStyle);//t

            String[] columns = new String[7];
            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // Write the output to the file
            while (!file.renameTo(file)) {
                // Cannot read from file, windows still working on it.
                Thread.sleep(10);
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized static void writeXLSXFile(File file, String loadName, String loadNewStat, int flag) {
        try {
            InputStream ExcelFileToRead = new FileInputStream(file);
            // Obtain a workbook from the excel file
            XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(ExcelFileToRead);
            ExcelFileToRead.close();
            // Get Sheet at index 0
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum2 = findRow(sheet, loadName);//t

            // Get Row at index 1
            XSSFRow row;//= sheet.getRow(2);
            row = sheet.getRow(rowNum2);//t

            // Create a CellStyle with the font
            CellStyle CellStyle = workbook.createCellStyle();
            sheet.setDisplayGridlines(false);
            // Get the Cell at index 2 from the above row
//            XSSFCell cell = row.createCell(5);

            // Create a Font for styling header cells
            if (flag == 9) {
                Font cellFontNotExist = workbook.createFont();
                cellFontNotExist.setBoldweight((short) 3);
                cellFontNotExist.setFontHeightInPoints((short) 13);
                cellFontNotExist.setColor(IndexedColors.BROWN.getIndex());
                CellStyle.setFont(cellFontNotExist);
                CellStyle.setFillBackgroundColor(IndexedColors.BROWN.getIndex());
                XSSFCell cell2 = row.createCell(row.getLastCellNum() + 3);
                cell2.setCellValue(loadNewStat);
                cell2.setCellStyle(CellStyle);
            }
//            cell2.setCellStyle(CellStyle);//t

            String[] columns = new String[10];
            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // Write the output to the file
            while (!file.renameTo(file)) {
                // Cannot read from file, windows still working on it.
                Thread.sleep(10);
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This is the method to find the row number
     * XSSFSheet sheet
     * String cellContent
     **/
    private static int findRow(XSSFSheet sheet, String cellContent) {

        int rowNum = 1;
        outerloop:
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getRichStringCellValue().getString().toUpperCase().trim().equals(cellContent)) {
                        rowNum = row.getRowNum();
                        break outerloop;
                    }
                }
            }
        }
        return rowNum;
    }

//    public List<String> getLoadName() {
//        String[] rowInfo = readXLSXFile();
//        List<String> loadNames = new ArrayList<>();
//        for (int i = 0; i < rowInfo.length; i++) {
//            String byRow = rowInfo[i];
//            String loadName = String.valueOf(byRow).split(",")[0] + "";
//            loadNames.add(i, loadName);
//        }
//        return loadNames;
//    }
//
//    public List<String> getLoadSize() {
//        String[] rowInfo = readXLSXFile();
//        List<String> loadsizes = new ArrayList<>();
//        for (int i = 0; i < rowInfo.length; i++) {
//            String byRow = rowInfo[i];
//            String loadName = String.valueOf(byRow).split(",")[1] + "";
//            loadsizes.add(i, loadName);
//        }
//        return loadsizes;
//    }
//
//    public List<String> getLoadDate() {
//        String[] rowInfo = readXLSXFile();
//        List<String> loadDates = new ArrayList<>();
//        for (int i = 0; i < rowInfo.length; i++) {
//            String byRow = rowInfo[i];
//            String loadName = String.valueOf(byRow).split(",")[1] + "";
//            loadDates.add(i, loadName);
//        }
//        return loadDates;
//    }

//    public static void main(String[] args) {
//        try {
//            XlsLoadData data = new XlsLoadData();
//            List<String> list = new ArrayList<String>();
//            list.add(0, "hdhdhddh");
//            list.add(1, "ffffffff");
//            list.add(2, "gggggggg");
//            File f = new File("D:\\excel\\10023-2.xlsx");
//            data.writeXLSXFile(f, "FGUCLMR1", 2);
////            data.readXLSXFile2(f);
//            for (InputDto a : data.readXLSXFile(f)) {
//                System.out.println(a.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
