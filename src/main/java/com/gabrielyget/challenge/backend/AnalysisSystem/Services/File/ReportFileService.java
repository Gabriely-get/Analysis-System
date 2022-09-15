package com.gabrielyget.challenge.backend.AnalysisSystem.Services.File;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Sale;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Salesman;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.CustomerService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.SaleService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.SalesmanService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportFileService.class);
    private Dotenv dotenv = Dotenv.load();
    private final String folderOutPath = dotenv.get("app.folder.out.path");
    private final String splitSymbol = dotenv.get("app.split.symbol");

    private CustomerService customerService;
    private SalesmanService salesmanService;
    private SaleService saleService;

    public ReportFileService(
            CustomerService customerService,
            SalesmanService salesmanService,
            SaleService saleService) {

        this.customerService = customerService;
        this.salesmanService = salesmanService;
        this.saleService = saleService;
    }

    public void generateReportOfFile(String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf("/")).replace(".dat", "").trim();
        String pathFilenameOut = folderOutPath + fileName + ".done.dat";

        try {
            DataOutputStream output = new DataOutputStream(new FileOutputStream(pathFilenameOut));

            List<String> report = List.of(
                    String.valueOf(this.getAmountOfDistinctClientsInFile(filePath)),
                    String.valueOf(this.getAmountOfDistinctSalesmanInFile(filePath)),
                    this.getMostExpensiveSaleId(filePath),
                    this.getWorstSalesmanEver(filePath)
            );

            output.write((String.join(splitSymbol, report)).getBytes());
            output.close();

            LOGGER.info(String.format("File report: [ %s ] was created", pathFilenameOut));
        } catch (RuntimeException | IOException e) {
            LOGGER.error(String.format("Error on generate report of file [ %s ] to [ %s ].", filePath, pathFilenameOut), e);
        }

    }

    public void generateReportOfAllFilesInFolder(String folderIn) {
        List<String> allFilesInFolder = new SplitFileService().getAllFileNamesFromDir(folderIn);

        allFilesInFolder.forEach(fileName -> {
            try {
                this.generateReportOfFile(fileName);
            } catch (RuntimeException e) {
                LOGGER.error(String.format("Error on generate file [ %s ] report", fileName), e);
            }
        });

    }

    private long getAmountOfClientsInFile(String filePath) {
        return customerService.getAllCustomersInFile(filePath).size();
    }

    private long getAmountOfDistinctClientsInFile(String filePath) throws IOException {
        List<Customer> customers = customerService.getAllCustomersInFile(filePath);
        long amount = -1;

        if (customers.isEmpty()) amount = -1;
        else if (customers.size() == 1) amount = 1;
        else amount = customers.stream().map(Customer::getCnpj).distinct().count();

        return amount;
    }

    private long getAmountOfSalesmanInFile(String filePath) {
        return customerService.getAllCustomersInFile(filePath).size();
    }

    private long getAmountOfDistinctSalesmanInFile(String filePath) throws IOException {
        List<Salesman> salesmanList = salesmanService.getAllSalesmanInFile(filePath);
        long amount;

        if (salesmanList.isEmpty()) amount = -1;
        else if (salesmanList.size() == 1) amount = 1;
        else amount = salesmanList.stream().map(Salesman::getCpf).distinct().count();

        return amount;
    }

    private String getMostExpensiveSaleId(String filePath) throws IOException {
        List<Sale> salesList = saleService.getAllSalesInFile(filePath);
        Map<String, Double> saleIdAndTotalValue = new HashMap<>();

        try {
            for (Sale sale : salesList) {
                Double totalValue = sale.getItemList().stream()
                        .map(item -> item.getQuantity() * item.getPrice())
                        .mapToDouble(Double::doubleValue)
                        .sum();

                saleIdAndTotalValue.put(sale.getId(), totalValue);

            }

            boolean allSalesValuesIsZero = saleIdAndTotalValue.values().stream().allMatch(value -> value < 1);
            if (allSalesValuesIsZero) return String.valueOf(-1);
        } catch (RuntimeException e) {
            LOGGER.error(String.format("Something went wrong on get most expensive sale in file [ %s ].", filePath), e);
        }

        return Collections.max(saleIdAndTotalValue.entrySet(), Map.Entry.comparingByValue()).getKey();

    }

    private String getWorstSalesmanEver(String filePath) throws IOException {
        List<Sale> salesList = saleService.getAllSalesInFile(filePath);
        Multimap<String, Double> allSalesmanNamesAndTotalValue = ArrayListMultimap.create();
        Map<String, Double> salesmanNamesAndTotalValueFiltered = new HashMap<>();

        try {
            for (Sale sale : salesList) {
                Double totalValue = sale.getItemList().stream()
                        .map(item -> item.getQuantity() * item.getPrice())
                        .mapToDouble(Double::doubleValue)
                        .sum();

                allSalesmanNamesAndTotalValue.put(sale.getSalesmanName(), totalValue);
            }

            List<String> distinctSalesmanNames = allSalesmanNamesAndTotalValue.keySet().stream().distinct().collect(Collectors.toList());

            for (String salesmanName : distinctSalesmanNames) {
                Double totalSaleValueBySalesman = allSalesmanNamesAndTotalValue.entries()
                        .stream()
                        .filter(name -> name.getKey().equals(salesmanName))
                        .map(Map.Entry::getValue)
                        .mapToDouble(Double::doubleValue).sum();

                salesmanNamesAndTotalValueFiltered.put(salesmanName, totalSaleValueBySalesman);
            }
        } catch (RuntimeException e) {
            LOGGER.error(String.format("Something went wrong on get worst salesman in file [ %s ].", filePath), e);
        }

        if (salesmanNamesAndTotalValueFiltered.isEmpty()) return "";

        return Collections.min(salesmanNamesAndTotalValueFiltered.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

}
