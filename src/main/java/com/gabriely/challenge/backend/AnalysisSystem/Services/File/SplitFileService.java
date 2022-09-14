package com.gabriely.challenge.backend.AnalysisSystem.Services.File;

import com.gabriely.challenge.backend.AnalysisSystem.Entities.DataTypeCode;
import com.gabriely.challenge.backend.AnalysisSystem.Utils.FileValidation;
import com.gabriely.challenge.backend.AnalysisSystem.Utils.GenericUtilFunctions;
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
import java.util.stream.Stream;

@Service
public class SplitFileService {
    private final Dotenv dotenv = Dotenv.load();
    private final String folderPath = dotenv.get("app.folder.path");
    private final String splitSymbol = dotenv.get("app.split.symbol");
    private static final Logger LOGGER = LoggerFactory.getLogger(SplitFileService.class);

    private List<String> getAllFileNamesFromDir(String folderPath) {
        //TODO verify if will throw or not
        boolean dirExists = GenericUtilFunctions.directoryExists(folderPath);
        List<String> fileNameList = new ArrayList<>();

        if (dirExists) {
            try(Stream<Path> files = Files.list(Paths.get(folderPath))) {
                if (files.findAny().isPresent()) {
                    fileNameList = files
                            .map(p -> p.getFileName().toString())
                            .filter(file -> file.endsWith(".dat"))
                            .collect(Collectors.toList());

                    if (fileNameList.isEmpty()) {
                        LOGGER.warn(String.format("Folder %s do not have any .dat file", folderPath));
                    }

                } else {
                    LOGGER.warn(String.format("Folder %s do not have any file", folderPath));
                }

            } catch (RuntimeException | IOException e) {
                e.printStackTrace();
                LOGGER.warn(String.format("Error on get all files in directory %s. Try again later.", folderPath), e);
            }
        }

        return fileNameList;
    }

    public String getFileContent(String filePath) throws IOException {
        //TODO try catch e return empty list
        String path = "/home/ilegra/Music/" + filePath;

        String content = "";

        if (new File(path).length() > 0) {
            content = Files
                    .readString(Paths.get(path), Charset.defaultCharset());

            if (FileValidation.fileContainsAnyTypeOfCode(content)) {
                if (FileValidation.fileContainsEnoughSplitSymbol(this.splitSymbol, content))
                    return content;
                else
                    LOGGER.warn(String.format("File %s do not have enough split symbol for can match with any types of data", filePath));
            } else {
                LOGGER.warn(String.format("File %s do not have any code for can match with data types", filePath));
            }

        }

        return content;

    }

    public List<List<String>> getSpecificDataTypeListFromFile(String filePath, String dataTypeCode) throws IOException {
        int i = 0;
        String fileContent = this.getFileContent(filePath);
        List<String> allFileRecordsList = new ArrayList<>();
        List<List<String>> specificDataTypeInstancesList = new ArrayList<>();
        List<String> allCodesList = DataTypeCode.getAllCodes();
        List<Integer> listOfAllCodesIndexes = new ArrayList<>();
        List<List<String>> specificDataTypeList = new ArrayList<>();

        if (fileContent.length() > 0) {
            allCodesList
                    .stream()
                    .map(code -> GenericUtilFunctions.getAllIndexOfString(code, fileContent)).forEach(listOfAllCodesIndexes::addAll);
            listOfAllCodesIndexes.remove(Integer.valueOf(-1));

            if (listOfAllCodesIndexes.isEmpty()) {
                return specificDataTypeInstancesList;
            }
            else {
                listOfAllCodesIndexes.sort(Comparator.naturalOrder());

                while (i < listOfAllCodesIndexes.size() - 1) {
                    allFileRecordsList.add(
                            fileContent.substring( listOfAllCodesIndexes.get(i),
                                    listOfAllCodesIndexes.get(i + 1) )
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

        }

        return specificDataTypeList;

    }

    private List<String> getLinesFromFile(String filePath) throws IOException {
        //TODO try catch e return empty list
        String path = this.folderPath + filePath;

        List<String> lines = new ArrayList<>();

        if (new File(path).length() > 0) {
            lines = Files
                    .readAllLines(Paths.get(path), Charset.defaultCharset());

            boolean fileContainsAnyTypeOfCode = lines.stream().anyMatch(FileValidation::fileContainsAnyTypeOfCode);
            boolean fileContainsEnoughSeparationSymbol = lines.stream()
                    .anyMatch(line -> FileValidation.fileContainsEnoughSplitSymbol(this.splitSymbol, line));

            if (fileContainsAnyTypeOfCode) {
                if (fileContainsEnoughSeparationSymbol)
                    return lines;
                else
                    LOGGER.warn(String.format("File %s do not have enough separation criteria for can match with any types of data", filePath));
            } else {
                LOGGER.warn(String.format("File %s do not have any code for can match with data types", filePath));
            }

        }

        return lines;

    }

}
