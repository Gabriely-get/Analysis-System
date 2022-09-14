package com.gabriely.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabriely.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabriely.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final SplitFileService splitFileService;

    public CustomerService(SplitFileService splitFileService) {
        this.splitFileService = splitFileService;
    }

    public List<Customer> getAllCustomersInFile(String filePath) throws IOException {
        List<Customer> customers = new ArrayList<>();
        List<List<String>> customersList = this.splitFileService.getSpecificDataTypeListFromFile(filePath, DataTypeCode.getCUSTOMER_CODE());

        for (List<String> attributes: customersList) {
            Customer customer = null;
            boolean hasException = false;

            try {
                customer = mapToCustomer(attributes);
            } catch (RuntimeException e) {
                LOGGER.warn(String.format("An error occurred on add attributes %s. Data ignored on file %s.", attributes, filePath), e);
                hasException = true;
            }

            if (!hasException) customers.add(customer);

        }

        return customers;
    }

    public Customer mapToCustomer(List<String> attributes) {
        return new Customer(attributes.get(1), attributes.get(2), attributes.get(3));
    }

}
