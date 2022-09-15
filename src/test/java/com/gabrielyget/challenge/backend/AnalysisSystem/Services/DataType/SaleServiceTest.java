package com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.ItemSale;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Sale;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class SaleServiceTest {

    private final String folderInPath = "src/test/java/resources/in";

    @Mock
    private static SplitFileService splitFileServiceMock;

    @InjectMocks
    private SaleService saleService;

    @BeforeAll
    public static void setup() {
        splitFileServiceMock = mock(SplitFileService.class);
    }

    @Test
    public void shouldGetAllSalesInFileWithBrackets() {
        ItemSale item1 = new ItemSale("1", 10, 100.00);
        ItemSale item2 = new ItemSale("2", 30, 2.50);
        ItemSale item3 = new ItemSale("3", 40, 3.10);
        List<ItemSale> itemSaleList01 = List.of(item1, item2, item3);
        List<String> data01 = List.of("003", "10", "[1-10-100,2-30-2.50,3-40-3.10]", "Diego");
        Sale sale01 = new Sale(data01.get(1), itemSaleList01, data01.get(3));

        ItemSale item4 = new ItemSale("1", 34, 10.00);
        ItemSale item = new ItemSale("2", 33, 15.50);
        ItemSale item6 = new ItemSale("3", 40, 0.10);
        List<ItemSale> itemSaleList02 = List.of(item4, item, item6);
        List<String> data02 = List.of("003", "08", "[1-34-10,2-33-15.50,3-40-0.10]", "Renato");
        Sale sale02 = new Sale(data02.get(1), itemSaleList02, data02.get(3));

        List<List<String>> salesExpectedList = List.of(data01, data02);

        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALE_CODE()))
        )
                .thenReturn(salesExpectedList);

        this.saleService = new SaleService(splitFileServiceMock);
        List<Sale> saleList = this.saleService.getAllSalesInFile(folderInPath + "/EnterpriseTestFile.dat");

        assertEquals(List.of(sale01, sale02).toString(), saleList.toString());
    }

    @Test
    public void shouldGetAllSalesInFileWithoutBrackets() {
        ItemSale item1 = new ItemSale("1", 10, 100.00);
        ItemSale item2 = new ItemSale("2", 30, 2.50);
        ItemSale item3 = new ItemSale("3", 40, 3.10);
        List<ItemSale> itemSaleList01 = List.of(item1, item2, item3);
        List<String> data01 = List.of("003", "10", "1-10-100,2-30-2.50,3-40-3.10", "Diego");
        Sale sale01 = new Sale(data01.get(1), itemSaleList01, data01.get(3));

        ItemSale item4 = new ItemSale("1", 34, 10.00);
        ItemSale item = new ItemSale("2", 33, 15.50);
        ItemSale item6 = new ItemSale("3", 40, 0.10);
        List<ItemSale> itemSaleList02 = List.of(item4, item, item6);
        List<String> data02 = List.of("003", "08", "1-34-10,2-33-15.50,3-40-0.10", "Renato");
        Sale sale02 = new Sale(data02.get(1), itemSaleList02, data02.get(3));

        List<List<String>> salesExpectedList = List.of(data01, data02);

        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALE_CODE()))
        )
                .thenReturn(salesExpectedList);

        this.saleService = new SaleService(splitFileServiceMock);
        List<Sale> saleList = this.saleService.getAllSalesInFile(folderInPath + "/EnterpriseTestFile.dat");

        assertEquals(List.of(sale01, sale02).toString(), saleList.toString());
    }

    @Test
    public void shouldGetEmptyList() {
        List<List<String>> specificDataTypeListEmpty = new ArrayList<>();
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALE_CODE()))
        )
                .thenReturn(specificDataTypeListEmpty);

        this.saleService = new SaleService(splitFileServiceMock);
        List<Sale> saleList = this.saleService.getAllSalesInFile(folderInPath + "/EnterpriseTestFile.dat");

        assertTrue(saleList.isEmpty());

    }

    @Test
    public void shouldLogExceptionOnGetAllSale(CapturedOutput output) {
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALE_CODE()))
        )
                .thenThrow(new RuntimeException());

        this.saleService = new SaleService(splitFileServiceMock);
        List<Sale> saleList = this.saleService.getAllSalesInFile(folderInPath + "/EnterpriseTestFile.dat");
        String LOG_MESSAGE = "Error on get all sales in file";

        assertTrue(output.getOut().contains(LOG_MESSAGE));

    }

    @Test
    public void shouldLogExceptionOnMapToSale(CapturedOutput output) {
        List<String> data01 = List.of("003", "", "[1-34-10,2-33-15.50,3-40-0.10]", "Renato");

        List<List<String>> salesExpectedList = List.of(data01);
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getSALE_CODE()))
        )
                .thenReturn(salesExpectedList);

        this.saleService = new SaleService(splitFileServiceMock);
        List<Sale> saleList = this.saleService.getAllSalesInFile(folderInPath + "/EnterpriseTestFile.dat");
        String LOG_MESSAGE = "An error occurred on add attributes";

        assertTrue(output.getOut().contains(LOG_MESSAGE));

    }

}