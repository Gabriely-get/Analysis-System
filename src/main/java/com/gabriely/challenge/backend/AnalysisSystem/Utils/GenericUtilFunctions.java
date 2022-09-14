package com.gabriely.challenge.backend.AnalysisSystem.Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class GenericUtilFunctions {

    public static boolean directoryExists(String path) {
        return new File(path).exists();
    }

    public static List<String> splitStringBySymbol(String splitSymbol, String stringToSplit) {
        return List.of(stringToSplit.split(splitSymbol));
    }

    public static List<Integer> getAllIndexOfString(String stringToSearch, String whereToSearch) {
        List<Integer> listOfIndexes = new ArrayList<>();
        int index = whereToSearch.indexOf(stringToSearch);

        while (index >= 0) {
            listOfIndexes.add(index);
            index = whereToSearch.indexOf(stringToSearch, index + 1);
        }

        return listOfIndexes;

    }

    public static int getClassFieldsQuantity(Class c) throws RuntimeException {
        Field[] fields = c.getDeclaredFields();
        int count = 0;

        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                count++;
            }
        }

        return count;
    }

}
