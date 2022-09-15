package com.gabrielyget.challenge.backend.AnalysisSystem.Services.File;

import com.gabrielyget.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabrielyget.challenge.backend.AnalysisSystem.Utils.FileValidation;
import com.gabrielyget.challenge.backend.AnalysisSystem.Utils.GenericUtilFunctions;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SplitFileService {
    private final Dotenv dotenv = Dotenv.load();
    private final String splitSymbol = dotenv.get("app.split.symbol");
    private static final Logger LOGGER = LoggerFactory.getLogger(SplitFileService.class);

    public List<String> getAllFileNamesFromDir(String folderPath) {
        boolean dirExists = GenericUtilFunctions.directoryExists(folderPath);
        List<String> fileNameList = new ArrayList<>();

        if (dirExists) {
            try {
                List<Path> files = Files.list(Paths.get(folderPath)).collect(Collectors.toList());
                if (!files.isEmpty()) {

                    for (Path file : files) {
                        if (file.toString().endsWith(".dat")) {
                            String content = Files
                                    .readString(Paths.get(file.toUri()), Charset.defaultCharset());

                            if (    !FileValidation.fileDoesntContainsAnyTypeOfCode(content)
                                    || FileValidation.fileContainsEnoughSplitSymbol(this.splitSymbol, content)
                            )
                                fileNameList.add(file.toString());
                        }
                    }

                    if (fileNameList.isEmpty()) {
                        LOGGER.warn(String.format("Folder %s do not have any .dat file", folderPath));
                    }

                } else {
                    LOGGER.warn(String.format("Folder %s do not have any file. Add a file for generate some report.", folderPath));
                }

            } catch (RuntimeException | IOException e) {
                LOGGER.error(String.format("Error on get all files in directory %s. Try again later.", folderPath), e);
            }
        }

        return fileNameList;
    }

    private String getFileContent(String filePath) {
        String content = "";

        try {
            if (new File(filePath).length() > 0) {

                content = Files
                        .readString(Paths.get(filePath), Charset.defaultCharset());
            }
        } catch (RuntimeException | IOException e) {
            LOGGER.error(String.format("Something went wrong on get file [ %s ] content.", filePath), e);
        }

        return content;

    }

    public List<List<String>> getSpecificDataTypeListFromFile(String filePath, String dataTypeCode) {
        int i = 0;
        String fileContent = this.getFileContent(filePath);
        List<String> allFileRecordsList = new ArrayList<>();
        List<String> allCodesList = DataTypeCode.getAllCodes();
        List<Integer> listOfAllCodesIndexes = new ArrayList<>();
        List<List<String>> specificDataTypeList = new ArrayList<>();

        try {
            if (fileContent.length() > 0) {
                allCodesList
                        .stream()
                        .map(code -> GenericUtilFunctions.getAllIndexOfString(code, fileContent)).forEach(listOfAllCodesIndexes::addAll);
                listOfAllCodesIndexes.remove(Integer.valueOf(-1));

                listOfAllCodesIndexes.sort(Comparator.naturalOrder());

                if (listOfAllCodesIndexes.isEmpty())
                    return specificDataTypeList;

                while (i < listOfAllCodesIndexes.size() - 1) {
                    allFileRecordsList.add(
                            fileContent.substring( listOfAllCodesIndexes.get(i),
                                    listOfAllCodesIndexes.get(i + 1) ).trim()
                    );

                    i++;
                }
                allFileRecordsList.add(
                        fileContent.substring(listOfAllCodesIndexes.get(listOfAllCodesIndexes.size()-1))
                );

                specificDataTypeList = allFileRecordsList.stream()
                        .map(data -> GenericUtilFunctions.splitStringBySymbol(this.splitSymbol, data))
                        .filter(data -> data.get(0).equals(dataTypeCode))
                        .collect(Collectors.toList());

            }
        } catch (RuntimeException e) {
            LOGGER.error(String.format("Something went wrong on searching a data type in file [ %s ]", filePath), e);
        }

        return specificDataTypeList;

    }

}
