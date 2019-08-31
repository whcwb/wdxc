package com.ldz.util.commonUtil;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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

    public static void createSheet(OutputStream outputStream,String sheetName,  List<Map<Integer,String>> data){
        try {
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);//单元格设置边框
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat.setAlignment(Alignment.CENTRE);
            WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            Map<Integer,String> titleMap=data.get(0);
            for	(int c = 0;c < titleMap.size();c++){
                sheet.addCell(new Label(c, 0, titleMap.get(c)==null?titleMap.get(c+""):titleMap.get(c),cellFormat));
            }
            for (int r = 1;r < data.size();r++) {
                Map<Integer,String> row = data.get(r);
                for(int c = 0;c < row.size();c++){
                    sheet.addCell(new Label(c, r, row.get(c+"")==null?row.get(c):row.get(c+""),cellFormat));
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 以选项卡的方式导出EXCEL
     * @param out       输出流
     * @param dataList  数据LIST
     * @param sheetList 选项卡列表
     * @param sheetList name 选项卡的名称
     * @param sheetList sheelMap 选项卡下关联的列名和该列从 数据LIST 里取值的KEY
     */
    public static void createSheetArray(OutputStream out,  List<Map<String, String>> dataList,  List<Map<String, Object>> sheetList) {
        try {
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);//单元格设置边框
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            int sheetIndex=0;
            for(Map<String,Object> tableMap:sheetList){
                Map<String,String> sheelMap= (Map<String, String>) tableMap.get("sheelMap");
                String sheetNames= (String) tableMap.get("name");
                WritableSheet sheet = workbook.createSheet(sheetNames, sheetIndex);
                int j=0;
                for (String value : sheelMap.values()) {
                    sheet.addCell(new Label(j, 0, value));
                    j++;
                }
                int r=1;
                for (Map<String,String> data:dataList) {
                    int c=0;
                    for (String key : sheelMap.keySet()) {
                        sheet.addCell(new Label(c, r, data.get(key)));
                        c++;
                    }
                    r++;
                }

                sheetIndex++;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 以选项卡的方式导出EXCEL
     * @param out       输出流
     * @param sheetList 选项卡列表
     * @param sheetList name 选项卡的名称
     * @param sheetList sheelMap 选项卡下关联的列名和该列从 数据LIST 里取值的KEY
     */
    public static void createSheetArrayAndDataArray(OutputStream out,  List<List<Map<String, String>>> dataMapList,  List<Map<String, Object>> sheetList) {
        try {
            WritableCellFormat cellFormat = new WritableCellFormat();
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.DASH_DOT);//单元格设置边框
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            int sheetIndex=0;
            for(  int i = 0 ; i < sheetList.size() ; i++){
                Map<String, Object> tableMap = sheetList.get(i);
                Map<String,String> sheelMap= (Map<String, String>) tableMap.get("sheelMap");
                String sheetNames= (String) tableMap.get("name");
                WritableSheet sheet = workbook.createSheet(sheetNames, sheetIndex);
                int j=0;
                for (String value : sheelMap.values()) {
                    sheet.addCell(new Label(j, 0, value));
                    j++;
                }
                int r=1;
                List<Map<String, String>> dataList = dataMapList.get(i);
                for (Map<String,String> data:dataList) {
                    int c=0;
                    for (String key : sheelMap.keySet()) {
                        sheet.addCell(new Label(c, r, data.get(key)));
                        c++;
                    }
                    r++;
                }

                sheetIndex++;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}
