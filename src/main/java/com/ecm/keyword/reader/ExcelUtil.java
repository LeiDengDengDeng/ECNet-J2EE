package com.ecm.keyword.reader;



import com.ecm.keyword.manager.TypeCalculator;
import com.ecm.model.Evidence_Body;
import com.ecm.model.Evidence_Head;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    /**
     * 将excel转为String数组
     *
     * @param file_dir 为文件路径
     * @return String[] 返回的是excel里面每个单元格的内容
     */
    public static String[] excelToArray(String file_dir) throws IOException {
//        fail("Not yet implemented");
        ArrayList<String> excelList = new ArrayList<String>();
        //String file_dir = "D:/model.xlsx";
        Workbook book = null;
        book = getExcelWorkbook(file_dir);

        Sheet sheet = getSheetByNum(book, 0);

        int lastRowNum = sheet.getLastRowNum();

        //System.out.println("last number is "+ lastRowNum);

        for (int i = 0; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                //System.out.println("reading line is " + i);
                int lastCellNum = row.getLastCellNum();
                //System.out.println("lastCellNum is " + lastCellNum );
                Cell cell = null;

                for (int j = 0; j <= lastCellNum; j++) {
                    cell = row.getCell(j);
                    if (cell != null) {
                        // String cellValue = cell.getStringCellValue();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String cellValue = cell.getStringCellValue();
                        excelList.add(cellValue);
                        // System.out.println(cellValue);
                        //System.out.println("cell value is \n" + cellValue);
                    }
                }
            }

        }
        String[] array = new String[excelList.size()];
        excelList.toArray(array);
        return array;
    }

    public static Sheet getSheetByNum(Workbook book, int number) {
        Sheet sheet = null;
        try {
            sheet = book.getSheetAt(number);
//          if(sheet == null){
//              sheet = book.createSheet("Sheet"+number);
//          }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return sheet;
    }

    public static Workbook getExcelWorkbook(String filePath) throws IOException {
        Workbook book = null;
        File file = null;
        FileInputStream fis = null;

        try {
            file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("文件不存在");
            } else {
                fis = new FileInputStream(file);
                book = WorkbookFactory.create(fis);


            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return book;
    }

    /**
     * 将excel转为String数组
     *
     * @param file_dir 为文件路径
     * @return String[] 返回的是excel里面每个单元格的内容
     */
    public static List<Evidence_Body> evidenceToList(String file_dir) throws IOException {
//        fail("Not yet implemented");
        ArrayList<Evidence_Body> evidenceList = new ArrayList<Evidence_Body>();
        //String file_dir = "D:/model.xlsx";
        Workbook book = null;
        book = getExcelWorkbook(file_dir);

        Sheet sheet = getSheetByNum(book, 0);

        int lastRowNum = sheet.getLastRowNum();
        Evidence_Body evidenceBody = new Evidence_Body();
        System.out.println("last number is " + lastRowNum);
        int id = 1;
        for (int i = 2; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                System.out.println("reading line is " + i);
                int lastCellNum = row.getLastCellNum();
                System.out.println("lastCellNum is " + lastCellNum);
                Cell cell = null;
                if (id != (int) row.getCell(1).getNumericCellValue()) {
                    evidenceBody = new Evidence_Body();
                    evidenceBody.setCaseID((int) sheet.getRow(0).getCell(2).getNumericCellValue());
                    evidenceBody.setBody(row.getCell(3).getStringCellValue());
                    evidenceBody.setTypeByString(row.getCell(4).getStringCellValue());
                    evidenceBody.setTrustByString(row.getCell(7).getStringCellValue());
                    id++;
                }
                Evidence_Head evidence_head = new Evidence_Head();
                evidence_head.setCaseID((int) sheet.getRow(0).getCell(2).getNumericCellValue());
                evidence_head.setHead(row.getCell(8).getStringCellValue());

                evidenceBody.addHead(evidence_head);
                evidenceList.add(evidenceBody);

            }

        }
        return evidenceList;
    }

}