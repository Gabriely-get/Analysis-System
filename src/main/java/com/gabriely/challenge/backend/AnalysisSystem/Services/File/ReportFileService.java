package com.gabriely.challenge.backend.AnalysisSystem.Services.File;

import com.gabriely.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Salesman;
import com.gabriely.challenge.backend.AnalysisSystem.Services.DataType.CustomerService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.DataType.SalesmanService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReportFileService {
    private Dotenv dotenv = Dotenv.load();
    private final String folderPath = dotenv.get("app.folder.path");

    public ReportFileService() {
    }

    public long getAmountOfDistinctClientsInFile(String filePath, CustomerService customerService) throws IOException {
        List<Customer> customers = customerService.getAllCustomersInFile(filePath);

        return customers.stream().map(Customer::getCnpj).distinct().count();
    }

    public long getAmountOfSalesmanInFile(String filePath, CustomerService customerService) throws IOException {
        return customerService.getAllCustomersInFile(filePath).stream().count();
    }

    public long getAmountOfDistinctSalesmanInFile(String filePath, SalesmanService salesmanService) throws IOException {
        List<Salesman> salesmanList = salesmanService.getAllSalesmanInFile(filePath);

        return salesmanList.stream().map(Salesman::getCpf).distinct().count();
    }

    public long getAmountOfClientsInFile(String filePath, CustomerService customerService) throws IOException {
        return customerService.getAllCustomersInFile(filePath).stream().count();
    }

}
