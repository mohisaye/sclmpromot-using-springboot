package com.baeldung.application.sclm;






/**
 * Created by m_sayekooie on 05/26/2019.
 */

public class PdfReadExample {

    private static final String FILE_NAME = "E:\\IdeaProjects\\RestEasy-UP-DOWN-Excel-File\\pdfs\\LOAD-LIST-10316-5.pdf";

//    public static void main(String[] args) {
//
//        PdfReader reader;
//        int y = 26;
//        try {
//
//            reader = new PdfReader("D:\\IdeaProjects\\RestEasy-UP-DOWN-Excel-File\\pdfs\\LOAD-LIST-10316-5.pdf");
//
//            // pageNumber = 1
//            String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);
//            ArrayList<String> list = new ArrayList<>();
//            ArrayList<String> list2 = new ArrayList<>();
//            for (String a : textFromPage.split("\n")) {
//                list.add(a);
//            }
//            for(String s:list)
//            {
//                if (s.matches("Date and Time Size Type Name Name Name Name")) {
//                    int x = list.indexOf("Date and Time Size Type Name Name Name Name");
//                    for (int i = x + 1; i < x+y+1; i++)
//                        list2.add(list.get(i));
//                }
//            }
//            System.out.println(list2);
//
//
//            System.out.println(textFromPage);
//
//            reader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    public static void main(String[] args) throws IOException {
//
//        try (PDDocument document = PDDocument.load(new File("D:\\IdeaProjects\\RestEasy-UP-DOWN-Excel-File\\pdfs\\LOAD-LIST-10316-5.pdf"))) {
//
//            document.getClass();
//
//            if (!document.isEncrypted()) {
//
//                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
//                stripper.setSortByPosition(true);
//
//                PDFTextStripper tStripper = new PDFTextStripper();
//
//                String pdfFileInText = tStripper.getText(document);
//                System.out.println( pdfFileInText);
//
//                // split by whitespace
//                String lines[] = pdfFileInText.split("\\r?\\n");
//                for (String line : lines) {
////                    System.out.println(line);
//                }
//
//            }
//
//        }
//
//    }

//    public static void main(String[] args) {

//        Document document = new Document("D:\\Test.pdf");
//        ExcelSaveOptions ex = new ExcelSaveOptions();
//        document.save("D:\\Test.xml" ,ex);
//        com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook("D:\\Test.xml");
//        workbook.save("D:\\Test.xlsx", com.aspose.cells.SaveFormat.XLSX);
//    }

}