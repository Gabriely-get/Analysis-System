package com.gabriely.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabriely.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Salesman;
import com.gabriely.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesmanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesmanService.class);
    private final SplitFileService splitFileService;

    public SalesmanService(SplitFileService splitFileService) {
        this.splitFileService = splitFileService;
    }

    public List<Salesman> getAllSalesmanInFile(String filePath) throws IOException {
        List<Salesman> salesmanList = new ArrayList<>();
        List<List<String>> salesmanAttributesList = this.splitFileService.getSpecificDataTypeListFromFile(filePath, DataTypeCode.getSALESMAN_CODE());

        for (List<String> attributes: salesmanAttributesList) {
            Salesman salesman = null;
            boolean hasException = false;

            try {
                salesman = mapToSalesman(attributes);
            } catch (RuntimeException e) {
                LOGGER.warn(String.format("An error occurred on add attributes %s. Data ignored on file %s.", attributes, filePath), e);
                hasException = true;
            }

            if (!hasException) salesmanList.add(salesman);

        }

        salesmanList.forEach(System.out::println);
        return salesmanList;
    }

    public Salesman mapToSalesman(List<String> attributes) {
        return new Salesman(attributes.get(1), attributes.get(2), Double.parseDouble(attributes.get(3)));
    }

}
