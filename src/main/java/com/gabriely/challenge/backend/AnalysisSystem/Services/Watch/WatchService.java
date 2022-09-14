package com.gabriely.challenge.backend.AnalysisSystem.Services.Watch;

import com.gabriely.challenge.backend.AnalysisSystem.Services.File.ReportFileService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class WatchService implements IWatchService {
    @Autowired
    ReportFileService reportService;

    @Override
    public void watchFolder(String path) {
        try(java.nio.file.WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path pathFowWatch = Paths.get(path);
            pathFowWatch.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                // TODO continue logic of stream
//                key.pollEvents().stream()
//                        .filter(event -> event.kind().equals(ENTRY_CREATE) || event.kind().equals(ENTRY_MODIFY))
//                        .map(watchEvent -> reportService.reportService(watchEvent.context().toString()));

                for (WatchEvent<?> event : key.pollEvents()) {
                    String fileName = event.context().toString();

                    File fileExists = new File(path + fileName);
                    if (event.kind().equals(ENTRY_CREATE)) {
                        Thread t = Thread.currentThread();
                        System.out.println("Thread = " + t.getName());

                        if (fileExists.exists()) {
                            //this.reportService(event.context().toString());
                            System.out.println(fileName + " was created");

                            DataInputStream input = new DataInputStream(new FileInputStream(path + fileName));

                            byte[] data = Files.readAllBytes(Paths.get(path + fileName));
                            String content = new String(data, StandardCharsets.UTF_8);
                            System.out.println(content);
                        }

                    }
                    if (event.kind().equals(ENTRY_MODIFY)) {
                        if (fileExists.exists()) {
                            System.out.println(fileName + " was modified");
                            //this.reportService(event.context().toString());

                            DataInputStream input = new DataInputStream(new FileInputStream(path + fileName));

                            byte[] data = Files.readAllBytes(Paths.get(path + fileName));
                            String content = new String(data, StandardCharsets.UTF_8);
                            System.out.println(content);
                        }

                    }

                }
                key.reset();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
