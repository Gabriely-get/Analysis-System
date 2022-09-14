package com.gabriely.challenge.backend.AnalysisSystem.Services.DataType;

import com.gabriely.challenge.backend.AnalysisSystem.Entities.Customer;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.IDataType;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Sale;
import com.gabriely.challenge.backend.AnalysisSystem.Entities.Salesman;

import java.io.IOException;
import java.util.List;

public interface IDataTypeService {

    List<IDataType> getAll();
    
}
