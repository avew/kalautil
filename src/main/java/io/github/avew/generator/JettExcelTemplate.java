package io.github.avew.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jett.transform.ExcelTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JettExcelTemplate implements Closeable {

    final ExcelTransformer transformer = new ExcelTransformer();
    final Workbook workbook;
    final InputStream templateStream;

    final List<String> sourceSheetNames = new ArrayList<>();
    final List<SheetSource> targetSheetNames = new ArrayList<>();

    public JettExcelTemplate(String templatePath) throws IOException, InvalidFormatException {
        this(new File(templatePath));
    }

    public JettExcelTemplate(Path templatePath) throws IOException, InvalidFormatException {
        this(templatePath.toFile());
    }

    public JettExcelTemplate(File template) throws IOException, InvalidFormatException {
        this(new FileInputStream(template));
    }

    public JettExcelTemplate(InputStream inputStream) throws IOException, InvalidFormatException {
        this.templateStream = inputStream;
        this.workbook = WorkbookFactory.create(templateStream);
        workbook.sheetIterator().forEachRemaining(sheet -> {
            sourceSheetNames.add(sheet.getSheetName());
        });
    }

    public JettExcelTemplate addBean(Map<String, Object> bean) {
        sourceSheetNames.forEach(name -> {
            targetSheetNames.add(SheetSource.builder()
                    .sheetSource(name)
                    .sheetTarget(name)
                    .bean(bean)
                    .build());
        });
        return this;
    }

    public JettExcelTemplate addSheet(String sheet, Map<String, Object> bean) {
        if (!sourceSheetNames.contains(sheet))
            throw new IllegalArgumentException("No sheet exist with that name");

        targetSheetNames.add(SheetSource.builder()
                .sheetSource(sheet)
                .sheetTarget(sheet)
                .bean(bean)
                .build());
        return this;
    }

    public JettExcelTemplate cloneSheet(String existingTemplateSheetName, String newSheetName, Map<String, Object> bean) {
        if (!sourceSheetNames.contains(existingTemplateSheetName))
            throw new IllegalArgumentException("No copyable sheet exist");

        targetSheetNames.add(SheetSource.builder()
                .sheetSource(existingTemplateSheetName)
                .sheetTarget(newSheetName)
                .bean(bean)
                .build());
        return this;
    }

    public void write(File output) throws IOException {
        write(new FileOutputStream(output));
    }

    /**
     * @param os
     * @throws IOException           asd
     * @throws IllegalStateException jika settingan sheet kosong
     */
    public void write(OutputStream os) throws IOException {
        if (targetSheetNames.isEmpty())
            throw new IllegalStateException("No Excel sheets to write");

        List<String> sheetSourceNames = targetSheetNames.stream()
                .map(SheetSource::getSheetSource)
                .collect(Collectors.toList());
        List<String> sheetTargetNames = targetSheetNames.stream()
                .map(SheetSource::getSheetTarget)
                .collect(Collectors.toList());
        List<Map<String, Object>> sheetBeans = targetSheetNames.stream()
                .map(SheetSource::getBean)
                .collect(Collectors.toList());

        transformer.transform(workbook, sheetSourceNames, sheetTargetNames, sheetBeans);
        workbook.write(os);
        os.close();
    }

    @Override
    public void close() throws IOException {
        templateStream.close();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SheetSource {
        private String sheetSource;
        private String sheetTarget;

        @Builder.Default
        private Map<String, Object> bean = new TreeMap<>();
    }

}
