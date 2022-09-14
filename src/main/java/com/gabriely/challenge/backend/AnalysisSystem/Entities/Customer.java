package com.gabriely.challenge.backend.AnalysisSystem.Entities;

import lombok.Data;

import java.util.List;

public @Data class Customer implements IDataType {
    private final String code = DataTypeCode.getCUSTOMER_CODE();
    private String cnpj;
    private String name;
    private String businessArea;

    public Customer() {

    }

    public Customer(String cnpj, String name, String businessArea) {
        this.cnpjValidation(cnpj);
        this.nameValidation(name);
        this.businessAreaValidation(businessArea);

        this.cnpj = cnpj;
        this.name = name;
        this.businessArea = businessArea;
    }

    public Customer(List<String> attributes) {
        this.cnpjValidation(attributes.get(1));
        this.nameValidation(attributes.get(2));
        this.businessAreaValidation(attributes.get(3));

        this.cnpj = attributes.get(1);
        this.name = attributes.get(2);
        this.businessArea = attributes.get(3);
    }

    private void cnpjValidation(String cnpj) {
        if (cnpj == null || cnpj.isBlank() || cnpj.isEmpty()) {
            throw new IllegalArgumentException("CNPJ is irregular");
        }

        if (cnpj.length() != 14) {
            throw new IllegalArgumentException("CNPJ has irregular length");
        }

    }

    private void nameValidation(String name) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("Name is irregular");
        }
    }

    private void businessAreaValidation(String businessArea) {
        if (businessArea == null || businessArea.isBlank() || businessArea.isEmpty()) {
            throw new IllegalArgumentException("Business Area string is irregular");
        }
    }

    @Override
    public String getCode() {
        return this.code;
    }

}