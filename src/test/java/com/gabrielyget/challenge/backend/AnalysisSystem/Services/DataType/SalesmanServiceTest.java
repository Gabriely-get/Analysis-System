package com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Salesman;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class SalesmanServiceTest {
//TODO delete folder_no_types
    private final String folderInPath = "src/test/java/resources/in";

    @Mock
    private static SplitFileService splitFileServiceMock;

    @InjectMocks
    private SalesmanService salesmanService;

    @BeforeAll
    public static void setup() {
        splitFileServiceMock = mock(SplitFileService.class);
    }

    @Test
    public void shouldGetAllSalesmanInFile() {
        List<String> data01 = List.of("001", "44444444444", "Gabriely Neh", "40000.99");
        Salesman salesman01 = new Salesman(data01.get(1), data01.get(2), Double.parseDouble(data01.get(3)));

        List<String> data02 = List.of("002", "32456788654", "Renato", "40000.99");
        Salesman salesman02 = new Salesman(data02.get(1), data02.get(2), Double.parseDouble(data02.get(3)));

        List<List<String>> salesmanExpectedList = List.of(data01, data02);

        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALESMAN_CODE()))
        )
                .thenReturn(salesmanExpectedList);

        this.salesmanService = new SalesmanService(splitFileServiceMock);
        List<Salesman> salesmanList = this.salesmanService.getAllSalesmanInFile(folderInPath + "/EnterpriseTestFile.dat");

        assertEquals(List.of(salesman01, salesman02).toString(), salesmanList.toString());
    }

    @Test
    public void shouldGetEmptyList() {
        List<List<String>> specificDataTypeListEmpty = new ArrayList<>();
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALESMAN_CODE()))
        )
                .thenReturn(specificDataTypeListEmpty);

        this.salesmanService = new SalesmanService(splitFileServiceMock);
        List<Salesman> salesmanList = this.salesmanService.getAllSalesmanInFile(folderInPath + "/EnterpriseTestFile.dat");

        assertTrue(salesmanList.isEmpty());

    }

    @Test
    public void shouldLogExceptionOnGetAllSalesman(CapturedOutput output) {
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALESMAN_CODE()))
        )
                .thenThrow(new RuntimeException());

        this.salesmanService = new SalesmanService(splitFileServiceMock);
        List<Salesman> salesmanList = this.salesmanService.getAllSalesmanInFile(folderInPath + "/EnterpriseTestFile.dat");
        String LOG_MESSAGE = "Error on get all salesman in file";

        assertTrue(output.getOut().contains(LOG_MESSAGE));

    }

    @Test
    public void shouldLogExceptionOnMapToCSalesman(CapturedOutput output) {
        List<String> data01 = List.of("002", "11111111111", "Teste", "");

        List<List<String>> salesmanExpectedList = List.of(data01);
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALESMAN_CODE()))
        )
                .thenReturn(salesmanExpectedList);

        this.salesmanService = new SalesmanService(splitFileServiceMock);
        List<Salesman> salesmanList = this.salesmanService.getAllSalesmanInFile(folderInPath + "/EnterpriseTestFile.dat");
        String LOG_MESSAGE = "An error occurred on add attributes";

        assertTrue(output.getOut().contains(LOG_MESSAGE));

    }

}