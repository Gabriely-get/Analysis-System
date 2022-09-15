package com.gabrielyget.challenge.backend.AnalysisSystem.Entities;

import lombok.Data;

import java.util.Objects;

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

    private void cnpjValidation(String cnpj) {
        if (cnpj == null || cnpj.isBlank() || cnpj.isEmpty()) {
            throw new IllegalArgumentException("CNPJ is irregular");
        }

        if (cnpj.length() != 14) {
            throw new IllegalArgumentException("CNPJ has irregular length");
        }

    }

    private void nameValidation(String name) {
        if (Objects.isNull(name) || name.isBlank() || name.isEmpty()) {
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