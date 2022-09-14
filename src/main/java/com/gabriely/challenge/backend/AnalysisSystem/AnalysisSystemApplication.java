package com.gabriely.challenge.backend.AnalysisSystem;

import com.gabriely.challenge.backend.AnalysisSystem.Services.DataType.CustomerService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.DataType.SaleService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.File.ReportFileService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AnalysisSystemApplication {
	private static Dotenv dotenv = Dotenv.load();

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AnalysisSystemApplication.class, args);
		// TODO app who watch this service for restart or call for some help if this is down

		System.out.println("\n\n");

		new ReportFileService().getAmountOfDistinctClientsInFile("test.txt", new CustomerService(new SplitFileService()));
		new SaleService(new SplitFileService()).getAllSalesInFile("test2.txt");

	}

}
