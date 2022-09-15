package com.gabrielyget.challenge.backend.AnalysisSystem.Entities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DataTypeCode {

    private static final String SALESMAN_CODE = "001";
    private static final String CUSTOMER_CODE = "002";
    private static final String SALE_CODE = "003";

    public static String getSALESMAN_CODE() {
        return SALESMAN_CODE;
    }

    public static String getCUSTOMER_CODE() {
        return CUSTOMER_CODE;
    }

    public static String getSALE_CODE() {
        return SALE_CODE;
    }

    public static List<String> getAllCodes() {
        Field[] fields = DataTypeCode.class.getDeclaredFields();
        List<String> lines = new ArrayList<>();

        try {
            for (Field field : fields) {
                lines.add(field.get(DataTypeCode.class).toString());
            }

        } catch (RuntimeException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return lines;

    }

}
