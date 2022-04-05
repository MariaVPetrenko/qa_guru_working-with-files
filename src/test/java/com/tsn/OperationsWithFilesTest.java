package com.tsn;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class OperationsWithFilesTest {

    @Test
    void readPdfInZipTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/files/Sample.zip");
        ZipEntry zipPdfEntry = zipFile.getEntry("sample.pdf");
        InputStream inputStream = zipFile.getInputStream(zipPdfEntry);
        PDF pdf = new PDF(inputStream);
        assertThat(pdf.text).contains("This is a small demonstration .pdf file - ");
    }

    @Test
    void readXlsInZipTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/files/Sample.zip");
        ZipEntry zipXlsEntry = zipFile.getEntry("broadcast_network_1648982768740.xlsx");
        InputStream inputStream = zipFile.getInputStream(zipXlsEntry);
        XLS xls = new XLS(inputStream);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(0)
                .getCell(0)
                .getStringCellValue()).contains("Market");
    }

    @Test
    void readCsvInZipTest() throws Exception {
        ZipFile zipFile = new ZipFile("src/test/resources/files/Sample.zip");
        ZipEntry zipCsvEntry = zipFile.getEntry("broadcast_network_1648982735535.csv");
        try (InputStream inputStream = zipFile.getInputStream(zipCsvEntry);
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> content = reader.readAll();
            assertThat(content.get(0)).contains(
                    "Market",
                    "District",
                    "Campus name",
                    "Enrollment",
                    "Screens count",
                    "Address",
                    "Classification",
                    "State / City",
                    "Screen status",
                    "Partner content",
                    "Created");
        }
    }

    @Test
    void jsonTypeTest() throws Exception {
    ObjectMapper mapper = new ObjectMapper ();
    File file = new File("src/test/resources/files/example.json");
    SuperHeroSquad shs = mapper.readValue(file,SuperHeroSquad.class);
    assertThat(shs.squadName).contains("Super hero squad");
    assertThat(shs.homeTown).contains("Metro City");
    assertThat(shs.formed).contains("2016");
    assertThat(shs.secretBase).contains("Super tower");
}
}
