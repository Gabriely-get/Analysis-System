package com.gabrielyget.challenge.backend.AnalysisSystem.Services.File;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.ItemSale;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Sale;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Salesman;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.CustomerService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.SaleService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.SalesmanService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Utils.GenericUtilFunctions;
import io.github.cdimascio.dotenv.Dotenv;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReportFileServiceTest {
    private Dotenv dotenv = Dotenv.load();
    private final String folderOutPath = dotenv.get("app.folder.out.path");
    private final String splitSymbol = dotenv.get("app.split.symbol");
    private final String folderInPath = "src/test/java/resources/in";
    private final String filePath = folderInPath + "/EnterpriseTestFile.dat";
    private static List<String> filenamesToDelete = new ArrayList<>();

    @Mock
    private static CustomerService customerServiceMock;

    @Mock
    private static SalesmanService salesmanService;

    @Mock
    private static SaleService saleService;

    @InjectMocks
    private ReportFileService reportFileService;

    @BeforeAll
    public static void setup() {
        customerServiceMock = mock(CustomerService.class);
        salesmanService = mock(SalesmanService.class);
        saleService = mock(SaleService.class);
    }

    @AfterAll
    public static void clean() {
        filenamesToDelete.forEach(filename -> new File(filename).delete());
    }

    @Order(1)
    @Test
    public void shouldGenerateReportOfFile() {
        try {
            ItemSale item1 = new ItemSale("1", 10, 100.00);
            ItemSale item2 = new ItemSale("2", 30, 2.50);
            ItemSale item3 = new ItemSale("3", 40, 3.10);
            List<ItemSale> itemSaleList01 = List.of(item1, item2, item3);
            Sale sale01 = new Sale("10", itemSaleList01, "Diego");

            ItemSale item4 = new ItemSale("1", 34, 10.00);
            ItemSale item = new ItemSale("2", 33, 15.50);
            ItemSale item6 = new ItemSale("3", 40, 0.10);
            List<ItemSale> itemSaleList02 = List.of(item4, item, item6);
            Sale sale02 = new Sale("08", itemSaleList02, "Renato");
            List<Sale> saleList = List.of(sale01, sale02);

            when(saleService.getAllSalesInFile(anyString()))
                    .thenReturn(saleList);

            Customer customer1 = new Customer("11111111111111", "gabrielyh teste", "Rural");
            Customer customer2 = new Customer("22222222222222", "gabrielyh teste filho", "Rural");
            List<Customer> customerList = List.of(customer1, customer2);

            when(customerServiceMock.getAllCustomersInFile(anyString()))
                    .thenReturn(customerList);

            Salesman salesman1 = new Salesman("11111111111", "gabrielyh teste", 10000.00);
            Salesman salesman2 = new Salesman("22222222222", "gabrielyh teste filho", 12000.00);
            List<Salesman> salesmanList = List.of(salesman1, salesman2);

            when(salesmanService.getAllSalesmanInFile(anyString()))
                    .thenReturn(salesmanList);

            String fileName = filePath.substring(filePath.lastIndexOf("/")).replace(".dat", "").trim();
            String pathFilenameOut = folderOutPath + fileName + ".done.dat";

            long oldFilesQuantityInFolder = Files.list(Paths.get(folderOutPath)).count();
            reportFileService.generateReportOfFile(filePath);
            long actualFilesQuantityInFolder = Files.list(Paths.get(folderOutPath)).count();

            filenamesToDelete.add(pathFilenameOut);

            String content = Files
                    .readString(Paths.get(pathFilenameOut), Charset.defaultCharset());

            assertTrue(GenericUtilFunctions.directoryExists(pathFilenameOut));
            assertEquals(oldFilesQuantityInFolder + 1l, actualFilesQuantityInFolder);
            assertEquals("2ç2ç10çRenato", content);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }

}