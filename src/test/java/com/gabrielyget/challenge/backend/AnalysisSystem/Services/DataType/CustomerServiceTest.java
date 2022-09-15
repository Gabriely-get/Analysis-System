package com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
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
class CustomerServiceTest {
    private final String folderInPath = "src/test/java/resources/in";

    @Mock
    private static SplitFileService splitFileServiceMock;

    @InjectMocks
    private CustomerService customerService;

    @BeforeAll
    public static void setup() {
        splitFileServiceMock = mock(SplitFileService.class);
    }

    @Test
    public void shouldGetAllCustomersInFile() {
        List<String> data01 = List.of("002", "11111111111111", "Jose da Silva Teste", "Marketing");
        Customer customer01 = new Customer(data01.get(1), data01.get(2), data01.get(3));

        List<String> data02 = List.of("002", "22222222222222", "Eduardo Pereira", "Varejo");
        Customer customer02 = new Customer(data02.get(1), data02.get(2), data02.get(3));

        List<List<String>> customersExpectedList = List.of(data01, data02);

        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getCUSTOMER_CODE()))
        )
                .thenReturn(customersExpectedList);

        this.customerService = new CustomerService(splitFileServiceMock);
        List<Customer> customersList = this.customerService.getAllCustomersInFile(folderInPath + "/EnterpriseTestFile.dat");

        assertEquals(List.of(customer01, customer02).toString(), customersList.toString());
    }

    @Test
    public void shouldGetEmptyList() {
        List<List<String>> specificDataTypeListEmpty = new ArrayList<>();
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getCUSTOMER_CODE()))
        )
                .thenReturn(specificDataTypeListEmpty);

        this.customerService = new CustomerService(splitFileServiceMock);
        List<Customer> customersList = this.customerService.getAllCustomersInFile(folderInPath + "/EnterpriseTestFile.dat");

        assertTrue(customersList.isEmpty());

    }

    @Test
    public void shouldLogExceptionOnGetAllCustomer(CapturedOutput output) {
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getCUSTOMER_CODE()))
        )
                .thenThrow(new RuntimeException());

        this.customerService = new CustomerService(splitFileServiceMock);
        List<Customer> customersList = this.customerService.getAllCustomersInFile(folderInPath + "/EnterpriseTestFile.dat");
        String LOG_MESSAGE = "Error on get all customers in file";

        assertTrue(output.getOut().contains(LOG_MESSAGE));

    }

    @Test
    public void shouldLogExceptionOnMapToCostumer(CapturedOutput output) {
        List<String> data01 = List.of("002", "11111111111111", "", "Marketing");

        List<List<String>> customersExpectedList = List.of(data01);
        when(splitFileServiceMock.getSpecificDataTypeListFromFile(
                eq(folderInPath + "/EnterpriseTestFile.dat"),
                eq(DataTypeCode.getCUSTOMER_CODE()))
        )
                .thenReturn(customersExpectedList);

        this.customerService = new CustomerService(splitFileServiceMock);
        List<Customer> customersList = this.customerService.getAllCustomersInFile(folderInPath + "/EnterpriseTestFile.dat");
        String LOG_MESSAGE = "An error occurred on add attributes";

        assertTrue(output.getOut().contains(LOG_MESSAGE));

    }

}