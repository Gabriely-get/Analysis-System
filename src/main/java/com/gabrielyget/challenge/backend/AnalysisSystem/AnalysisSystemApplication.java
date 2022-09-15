package com.gabriely.challenge.backend.AnalysisSystem;

import com.gabriely.challenge.backend.AnalysisSystem.Services.DataType.CustomerService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.DataType.SaleService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.DataType.SalesmanService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.File.ReportFileService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import com.gabriely.challenge.backend.AnalysisSystem.Services.Watch.WatchService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AnalysisSystemApplication {
	private static Dotenv dotenv = Dotenv.load();
	private static String folderIn = dotenv.get("app.folder.in.path");

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AnalysisSystemApplication.class, args);
		// TODO app who watch this service for restart or call for some help if this is down

		ReportFileService reportFileService = new ReportFileService(
				new CustomerService(new SplitFileService()),
				new SalesmanService(new SplitFileService()),
				new SaleService(new SplitFileService()),
				new SplitFileService()
		);

		System.out.println("string path parameter on main reportallfiles/watchfolder : " + folderIn);
		reportFileService.generateReportOfAllFilesInFolder(folderIn);

		new WatchService(
				reportFileService
		).watchFolder(folderIn);

	}

}
