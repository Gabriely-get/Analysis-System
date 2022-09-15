package com.gabrielyget.challenge.backend.AnalysisSystem.Utils;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Sale;
import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.Salesman;

import java.util.stream.Collectors;

public class FileValidation {

    public static boolean fileDoesntContainsAnyTypeOfCode(String content) {
        return DataTypeCode.getAllCodes().stream().map(content::indexOf).allMatch(index -> index == -1);
    }

    public static boolean fileContainsEnoughSplitSymbol(String splitSymbol, String content) {
        long fileContainsCodeQuantity = GenericUtilFunctions.getAllIndexOfString(splitSymbol, content).size();

        return fileContainsCodeQuantity > GenericUtilFunctions.getClassFieldsQuantity(Customer.class) - 1
                || fileContainsCodeQuantity > GenericUtilFunctions.getClassFieldsQuantity(Sale.class) - 1
                || fileContainsCodeQuantity > GenericUtilFunctions.getClassFieldsQuantity(Salesman.class) - 1;
    }

}
