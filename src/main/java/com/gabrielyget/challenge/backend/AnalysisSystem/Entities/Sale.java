package com.gabriely.challenge.backend.AnalysisSystem.Entities;

import lombok.Data;

import java.util.List;

public @Data class Sale implements IDataType {
    private final String code = DataTypeCode.getSALE_CODE();
    private String id;
    private List<ItemSale> itemList;
    private String salesmanName;

    public Sale() {

    }

    public Sale(String id, List<ItemSale> itemList, String salesmanName) {
        this.idValidation(id);
        this.itemListValidation(itemList);
        this.salesmanNameValidation(salesmanName);

        this.id = id;
        this.itemList = itemList;
        this.salesmanName = salesmanName;
    }

    private void idValidation(String id) {
        if (Integer.parseInt(id) <= 0 ) {
            throw new IllegalArgumentException("Id value is irregular. Can not be 0 or negative number.");
        }
    }

    private void salesmanNameValidation(String salesmanName) {
        if (salesmanName == null || salesmanName.isBlank() || salesmanName.isEmpty()) {
            throw new IllegalArgumentException("Salesman name is irregular");
        }
    }

    private void itemListValidation(List<ItemSale> itemList) {
        if (itemList.isEmpty() || itemList == null || itemList.size() < 1) {
            throw new IllegalArgumentException("Item list is irregular");
        }

        for (ItemSale itemSale : itemList) {
            if (itemSale == null) {
                throw new IllegalArgumentException("Item of sale is irregular");
            }
        }

    }

    @Override
    public String getCode() {
        return this.code;
    }

}
