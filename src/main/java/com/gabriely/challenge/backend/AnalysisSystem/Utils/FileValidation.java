package com.gabriely.challenge.backend.AnalysisSystem.Utils;

import com.gabriely.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Sale;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Salesman;

public class FileValidation {

    public static boolean fileContainsAnyTypeOfCode(String content) {
        boolean codeFound = DataTypeCode.getAllCodes().stream().map(content::indexOf).allMatch(index -> index == -1);

        return !codeFound;
    }

    public static boolean fileContainsEnoughSplitSymbol(String splitsymbol, String content) {
        long fileContainsCodeQuantity = content.length() - content.replace(splitsymbol, "").length();

        return fileContainsCodeQuantity > GenericUtilFunctions.getClassFieldsQuantity(Customer.class) - 1
                || fileContainsCodeQuantity > GenericUtilFunctions.getClassFieldsQuantity(Sale.class) - 1
                || fileContainsCodeQuantity > GenericUtilFunctions.getClassFieldsQuantity(Salesman.class) - 1;
    }

}
