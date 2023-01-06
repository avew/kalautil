package io.github.avew.util;

import net.sf.jett.transform.ExcelTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static File toExcelFile(Map<String, Object> beans,
                                   File fileTemplate,
                                   File fileOut) throws IOException, InvalidFormatException {
        ExcelTransformer transformer = new ExcelTransformer();
        FileInputStream fis = new FileInputStream(fileTemplate);
        Workbook workbook = transformer.transform(fis, beans);
        FileOutputStream fos = new FileOutputStream(fileOut);
        workbook.write(fos);

        fis.close();
        fos.close();

        return fileOut;
    }

    public static File toExcelFile(List<Map<String, Object>> beans,
                                   File fileTemplate,
                                   File fileOut,
                                   List<String> templateSheetNames,
                                   List<String> sheetNames) throws IOException, InvalidFormatException {
        FileInputStream fis = new FileInputStream(fileTemplate);
        return toExcelFile(beans, fis, fileOut, templateSheetNames, sheetNames);
    }

    public static File toExcelFile(List<Map<String, Object>> beans,
                                   InputStream fileTemplate,
                                   File fileOut,
                                   List<String> templateSheetNames,
                                   List<String> sheetNames) throws IOException, InvalidFormatException {
        ExcelTransformer transformer = new ExcelTransformer();
        Workbook workbook = transformer.transform(fileTemplate, templateSheetNames, sheetNames, beans);
        FileOutputStream fos = new FileOutputStream(fileOut);
        workbook.write(fos);

        fileTemplate.close();
        fos.close();

        return fileOut;
    }
}
