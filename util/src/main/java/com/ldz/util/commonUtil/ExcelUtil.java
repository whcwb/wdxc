package com.ldz.util.commonUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

public class ExcelUtil {


    public static void createSheet(OutputStream outputStream,String sheetName, String[] heads, List<List<String>> data){
        createSheet(outputStream,sheetName,0,heads,data);
    }

    /**
     * 根据表名，表头，表数据 生成excel文件，输出到outputStream
     * @param outputStream 输出流
     * @param sheetName 表名
     * @param sheetIndex 表索引，默认为0
     * @param heads 表头
     * @param data 表数据
     */
    public static void createSheet(OutputStream outputStream,String sheetName, int sheetIndex, String[] heads, List<List<String>> data){
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = workbook.createSheet(sheetName, sheetIndex);
            for	(int c = 0;c < heads.length;c++){
                sheet.addCell(new Label(c, 0, heads[c]));
            }
            for (int r = 0;r < data.size();r++) {
                List<String> row = data.get(r);
                for(int c = 0;c < row.size();c++){
                    sheet.addCell(new Label(c, r+1, row.get(c)));
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





}
