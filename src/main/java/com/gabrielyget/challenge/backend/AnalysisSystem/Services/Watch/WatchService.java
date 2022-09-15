package com.gabrielyget.challenge.backend.AnalysisSystem.Services.Watch;

import com.gabrielyget.challenge.backend.AnalysisSystem.Services.File.ReportFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class WatchService implements IWatchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatchService.class);

    private final ReportFileService reportFileService;

    public WatchService(ReportFileService reportFileService) {
        this.reportFileService = reportFileService;
    }

    @Override
    public void watchFolder(String path) {
        try(java.nio.file.WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path pathFowWatch = Paths.get(path);
            pathFowWatch.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

            WatchKey key;
            while ((key = watchService.take()) != null) {

                for (WatchEvent<?> event : key.pollEvents()) {
                    String fileName = "/" + event.context().toString();

                    File fileExists = new File(path + fileName);

                    if (event.kind().equals(ENTRY_CREATE) || event.kind().equals(ENTRY_MODIFY)) {

                        if (fileExists.exists()) {
                            this.reportFileService.generateReportOfFile(fileName);
                        }

                    }

                }
                key.reset();
            }

        } catch (InterruptedException | IOException e) {
            LOGGER.error("Something went wrong and was not possible start application!", e);
        }
    }

}
