package com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final SplitFileService splitFileService;

    public CustomerService(SplitFileService splitFileService) {
        this.splitFileService = splitFileService;
    }

    public List<Customer> getAllCustomersInFile(String filePath) {
        List<Customer> customers = new ArrayList<>();

        try {
            List<List<String>> customersList = this.splitFileService.getSpecificDataTypeListFromFile(filePath, DataTypeCode.getCUSTOMER_CODE());

            if (!customersList.isEmpty()) {
                for (List<String> attributes : customersList) {
                    if (!attributes.isEmpty()) {
                        Customer customer = null;
                        boolean hasException = false;

                        try {
                            customer = this.mapToCustomer(attributes);
                        } catch (RuntimeException e) {
                            LOGGER.warn(String.format("An error occurred on add attributes [ %s ].Data ignored on file [ %s ].", attributes, filePath), e);
                            hasException = true;
                        }

                        if (!hasException && customer != null) customers.add(customer);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.error(String.format("Error on get all customers in file [ %s ].", filePath), e);
        }

        return customers;
    }

    private Customer mapToCustomer(List<String> attributes) {
        return new Customer(attributes.get(1).trim(), attributes.get(2).trim(), attributes.get(3).trim());
    }

}
