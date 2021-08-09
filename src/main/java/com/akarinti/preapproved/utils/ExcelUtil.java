package com.akarinti.preapproved.utils;

import com.akarinti.preapproved.utils.exception.CustomException;
import com.akarinti.preapproved.utils.exception.StatusCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Component
@Data
@Slf4j
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    final static String NUMBER_FORMAT = "#,##0";
    final static String NUMBER_FORMAT_DECIMAL = "#,##0.00";

    public final static String MEDIA_TYPE_XLS = "application/vnd.ms-excel";
    public final static String MEDIA_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public File generateExcel( List<String>listSheetName, List<String[]> listHeader, List<List<Object[]>> listData) throws IOException {
        File file = File.createTempFile("file", "tmp");
        try {
            Workbook wb = new XSSFWorkbook();

            CellStyle headerStyle = headerStyle(wb);
            CellStyle textStyle = textStyle(wb);
            CellStyle numberStyle = numberStyle(wb);
            CellStyle dateStyle = dateStyle(wb);
            CellStyle boldTextStyle = boldTextStyle(wb);
            CellStyle textStyleBordered = textStyleBordered(wb);
            CellStyle numberStyleBordered = numberStyleBordered(wb);
            CellStyle dateStyleBordered = dateStyleBordered(wb);

            for(int i=0; i<listSheetName.size(); i++){
                int rowIdx = 0;

                Sheet sheet;
                if(listSheetName.get(i) != null) {
                    sheet = wb.createSheet(listSheetName.get(i));

                }else{
                    sheet = wb.createSheet("Sheet "+i);
                }

                // sheet add header
                if(listHeader.size() > i) {
                    String[] headerSheet = listHeader.get(i);
                    if (headerSheet != null) {
                        // loop header column
                        Row headerRow = sheet.createRow(rowIdx);
                        Cell cell = headerRow.createCell(0);
                        cell.setCellStyle(headerStyle);
                        cell.setCellValue("No");

                        for (int k = 1; k <= headerSheet.length; k++) {
                            // loop column
                            Cell nextCell = headerRow.createCell(k);
                            nextCell.setCellStyle(headerStyle);
                            nextCell.setCellValue(headerSheet[k-1]);
                            sheet.autoSizeColumn(k);
                        }
                    }
                }

                // sheet add data
                if(listData.size() > i) {
                    List<Object[]> dataSheet = listData.get(i);
                    if (dataSheet != null) {
                        for (int j = 0; j < dataSheet.size(); j++) {
                            // loop row
                            rowIdx++;
                            Row nextRow = sheet.createRow(rowIdx);

                            Object[] data = dataSheet.get(j);
                            Cell cellNo = nextRow.createCell(0);
                            cellNo.setCellValue(j+1d);
                            cellNo.setCellStyle(headerStyle);

                            for (int k = 1; k <= data.length; k++) {
                                // loop column
                                Cell cell = nextRow.createCell(k);

                                if(data[k-1] == null){
                                    cell.setCellStyle(textStyleBordered);

                                }else if(data[k-1] instanceof String){
                                    cell.setCellValue(data[k-1].toString());
                                    cell.setCellStyle(textStyleBordered);

                                } else if (data[k-1] instanceof Number){
                                    cell.setCellValue(Integer.parseInt(data[k-1].toString()));
                                    cell.setCellStyle(numberStyleBordered);

                                } else if (data[k-1] instanceof Date){
                                    Date date = org.apache.commons.lang3.time.DateUtils.truncate((Date) data[k-1], Calendar.DAY_OF_MONTH);
                                    cell.setCellValue(date);
                                    cell.setCellStyle(dateStyleBordered);
                                }
                                sheet.autoSizeColumn(k);
                            }
                        }
                        rowIdx++;
                    }
                }

            }

            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }

        return file;
    }

    private CellStyle textStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);

        return style;
    }

    private CellStyle boldTextStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);

        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    private CellStyle numberStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();

        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(format.getFormat(NUMBER_FORMAT));

        return style;
    }

    private CellStyle dateStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setDataFormat(format.getFormat("dd MMM yyyy"));

        return style;
    }

    private CellStyle headerStyle(Workbook wb){
        CellStyle style = borderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);

        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    private CellStyle textStyleBordered(Workbook wb){
        CellStyle style = borderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);

        return style;
    }

    private CellStyle numberStyleBordered(Workbook wb){
        CellStyle style = borderedStyle(wb);
        DataFormat format = wb.createDataFormat();

        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(format.getFormat(NUMBER_FORMAT));

        return style;
    }

    private CellStyle dateStyleBordered(Workbook wb){
        CellStyle style = borderedStyle(wb);
        DataFormat format = wb.createDataFormat();

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setDataFormat(format.getFormat("dd MMM yyyy"));

        return style;
    }


    private CellStyle borderedStyle(Workbook wb){
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();

        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        return style;
    }


}
