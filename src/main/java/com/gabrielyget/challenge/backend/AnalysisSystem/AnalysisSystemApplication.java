package com.gabrielyget.challenge.backend.AnalysisSystem;

import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.CustomerService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.SaleService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.DataType.SalesmanService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.File.ReportFileService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.File.SplitFileService;
import com.gabrielyget.challenge.backend.AnalysisSystem.Services.Watch.WatchService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnalysisSystemApplication {
	private static Dotenv dotenv = Dotenv.load();
	private static String folderIn = dotenv.get("app.folder.in.path");

	public static void main(String[] args) {
		SpringApplication.run(AnalysisSystemApplication.class, args);

		ReportFileService reportFileService = new ReportFileService(
				new CustomerService(new SplitFileService()),
				new SalesmanService(new SplitFileService()),
				new SaleService(new SplitFileService())
		);
		reportFileService.generateReportOfAllFilesInFolder(folderIn);

		new WatchService().watchFolder(folderIn);

	}

}
