package com.gabrielyget.challenge.backend.AnalysisSystem.Services.File;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class SplitFileServiceTest {
    private final String folderInPath = "src/test/java/resources/in";
    private String emptyFolderInPath = "src/test/java/resources/empty_folder";
    private String folderWithTxtOnly = "src/test/java/resources/folder_with_txt";
    private String folderWithDatEmpty = "src/test/java/resources/empty_file";
    private String LOG_MESSAGE = "";
    private final SplitFileService splitFileService = new SplitFileService();

    @Nested
    @DisplayName("Tests for getAllFileNamesFromDir SplitFileService method")
    class TestGetAllFileNamesFromDir {
        @Test
        public void shouldGetAllFileNamesFromDir() {
            List<String> filesNames = splitFileService.getAllFileNamesFromDir(folderInPath);
            List<String> filesNamesExpected = List.of("src/test/java/resources/in/EnterpriseTestFile.dat");

            assertEquals(filesNamesExpected, filesNames);
        }

        @Test
        public void shouldReturnEmptyListIfDirectoryDoesNotExists() {
            List<String> filesNamesError = splitFileService.getAllFileNamesFromDir("/random/path/for/fail");
            List<String> emptyListExpected = new ArrayList<>();

            assertEquals(emptyListExpected, filesNamesError);
        }

        @Test
        public void shouldLogIfFolderIsEmpty(CapturedOutput output) {
            List<String> filesNamesError = splitFileService.getAllFileNamesFromDir(emptyFolderInPath);
            List<String> emptyListExpected = new ArrayList<>();
            LOG_MESSAGE = String.format("Folder %s do not have any file. Add a file for generate some report.", emptyFolderInPath);

            assertEquals(emptyListExpected, filesNamesError);
            assertTrue(output.getOut().contains(LOG_MESSAGE));
        }

        @Test
        public void shouldLogIfFolderDoesntHaveDatFile(CapturedOutput output) {
            List<String> filesNamesError = splitFileService.getAllFileNamesFromDir(folderWithTxtOnly);
            List<String> emptyListExpected = new ArrayList<>();
            LOG_MESSAGE = String.format("Folder %s do not have any .dat file", folderWithTxtOnly);

            assertEquals(emptyListExpected, filesNamesError);
            assertTrue(output.getOut().contains(LOG_MESSAGE));
        }

    }

    @Nested
    @DisplayName("Tests for getSpecificDataTypeListFromFile SplitFileService method")
    class TestGetSpecificDataTypeListFromFile {

        @Test
        public void shouldGetCustomerListFromFile() {
            List<List<String>> customers = splitFileService.getSpecificDataTypeListFromFile(folderInPath + "/EnterpriseTestFile.dat",
                    DataTypeCode.getCUSTOMER_CODE());
            String customersExpected = "[[002, 11111111111111, Jose da Silva Teste, Marketing], [002, 22222222222222, Eduardo Pereira, Varejo]]";

            assertEquals(customersExpected, customers.toString());
        }

        @Test
        public void shouldGetSalesmanListFromFile() {
            List<List<String>> salesmanList = splitFileService.getSpecificDataTypeListFromFile(folderInPath + "/EnterpriseTestFile.dat",
                    DataTypeCode.getSALESMAN_CODE());
            String salesmanListExpected = "[[001, 44444444444, Gabriely Neh, 40000.99], [001, 32456788654, Renato, 40000.99]]";

            assertEquals(salesmanListExpected, salesmanList.toString());
        }

        @Test
        public void shouldGetSaleListFromFile() {
            List<List<String>> sales = splitFileService.getSpecificDataTypeListFromFile(folderInPath + "/EnterpriseTestFile.dat",
                    DataTypeCode.getSALE_CODE());
            String salesExpected = "[[003, 10, [1-10-100,2-30-2.50,3-40-3.10], Diego], [003, 08, [1-34-10,2-33-15.50,3-40-0.10], Renato]]";

            assertEquals(salesExpected, sales.toString());
        }

    }

}