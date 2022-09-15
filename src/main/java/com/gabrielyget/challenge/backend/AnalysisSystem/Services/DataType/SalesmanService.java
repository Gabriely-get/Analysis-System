package com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Salesman;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalesmanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesmanService.class);
    private final SplitFileService splitFileService;

    public SalesmanService(SplitFileService splitFileService) {
        this.splitFileService = splitFileService;
    }

    public List<Salesman> getAllSalesmanInFile(String filePath) {
        List<Salesman> salesmanList = new ArrayList<>();

        try {
            List<List<String>> salesmanAttributesList = this.splitFileService
                    .getSpecificDataTypeListFromFile(filePath, DataTypeCode.getSALESMAN_CODE());

            if (!salesmanAttributesList.isEmpty()) {
                for (List<String> attributes : salesmanAttributesList) {
                    if (!attributes.isEmpty()) {
                        Salesman salesman = null;
                        boolean hasException = false;

                        try {
                            salesman = mapToSalesman(attributes);
                        } catch (RuntimeException e) {
                            LOGGER.warn(String.format("An error occurred on add attributes [ %s ]. Data ignored on file %s.", attributes, filePath), e);
                            hasException = true;
                        }

                        if (!hasException && salesman != null) salesmanList.add(salesman);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.error(String.format("Error on get all salesman in file [ %s ].", filePath), e);
        }

        return salesmanList;
    }

    private Salesman mapToSalesman(List<String> attributes) {
        return new Salesman(attributes.get(1).trim(), attributes.get(2).trim(), Double.parseDouble(attributes.get(3)));
    }

}
