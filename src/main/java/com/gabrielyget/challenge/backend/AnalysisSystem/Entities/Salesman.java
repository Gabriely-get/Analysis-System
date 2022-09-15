package com.gabriely.challenge.backend.AnalysisSystem.Entities;

import lombok.Data;

import java.util.List;

public @Data class Salesman implements IDataType {
    private final String code = DataTypeCode.getSALESMAN_CODE();
    private String cpf;
    private String name;
    private Double salary;

    public Salesman() {

    }

    public Salesman(String cpf, String name, Double salary) {
        this.cpfValidation(cpf);
        this.nameValidation(name);
        this.salaryValidation(salary);

        this.cpf = cpf;
        this.name = name;
        this.salary = salary;
    }

    private void cpfValidation(String cpf) {
        if (cpf == null || cpf.isBlank() || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF is irregular");
        }

        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF has irregular length");
        }
    }

    private void salaryValidation(Double salary) {
        if (salary < 1) {
            throw new IllegalArgumentException("Salary value is irregular");
        }
    }

    private void nameValidation(String name) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("Name is irregular");
        }
    }

    @Override
    public String getCode() {
        return this.code;
    }

}