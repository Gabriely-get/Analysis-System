package com.gabriely.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabriely.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.ItemSale;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Sale;
import com.gabriely.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import com.gabriely.challenge.backend.AnalysisSystem.Utils.GenericUtilFunctions;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SaleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleService.class);
    private final Dotenv dotenv = Dotenv.load();
    private final SplitFileService splitFileService;

    public SaleService(SplitFileService splitFileService) {
        this.splitFileService = splitFileService;
    }

    public List<Sale> getAllSalesInFile(String filePath) throws IOException {
        List<Sale> sales = new ArrayList<>();
        List<List<String>> saleList = this.splitFileService.getSpecificDataTypeListFromFile(filePath, DataTypeCode.getSALE_CODE());

        for (List<String> attributes: saleList) {
            Sale sale = null;
            boolean hasException = false;

            try {
                sale = mapToSale(attributes);
            } catch (RuntimeException e) {
                LOGGER.warn(String.format("An error occurred on add attributes %s. Data ignored on file %s.", attributes, filePath), e);
                hasException = true;
            }

            if (!hasException) sales.add(sale);

        }
        sales.forEach(System.out::println);
        return sales;

    }


    public Sale mapToSale(List<String> attributes) {
        String splitSymbol = this.dotenv.get("app.item.split.symbol");
        List<ItemSale> items;

        String itemsListAsString = attributes.get(2);
        String itemsListAsStringWithoutBrackets = this.removeBracketsFromItemList(itemsListAsString);

        if (itemsListAsStringWithoutBrackets.isEmpty()) {
            items = Arrays
                    .stream(itemsListAsString.split(","))
                    .map(item -> this.mapToItem(item, splitSymbol))
                    .collect(Collectors.toList());
        } else {
            items = Arrays
                    .stream(itemsListAsStringWithoutBrackets.split(","))
                    .map(item -> this.mapToItem(item, splitSymbol))
                    .collect(Collectors.toList());
        }

        return new Sale(attributes.get(1), items, attributes.get(3));
    }

    private ItemSale mapToItem(String itemListAsString, String splitSymbol) {
        String[] fields = itemListAsString.split(splitSymbol);

        return new ItemSale(fields[0], Integer.parseInt(fields[1]), Double.parseDouble(fields[2]));
    }

    private String removeBracketsFromItemList(String itemsListAsString) {

        String itemsListAsStringWithoutBrackets = "";
        List<Integer> indexes = GenericUtilFunctions.getAllIndexOfString("[", itemsListAsString);
        for (int index : indexes) {
            itemsListAsStringWithoutBrackets = itemsListAsString.replace("[", "");
        }


        List<Integer> indexes2 = GenericUtilFunctions.getAllIndexOfString("]", itemsListAsStringWithoutBrackets);
        for (int index : indexes2) {
            itemsListAsStringWithoutBrackets = itemsListAsStringWithoutBrackets.replace("]", "");
        }

        return itemsListAsStringWithoutBrackets;

    }

}
