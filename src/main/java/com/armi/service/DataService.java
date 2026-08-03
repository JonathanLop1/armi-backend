package com.armi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class DataService {

    private final ObjectMapper objectMapper;
    // Default to the flutter project's assets/data.json relative to backend dir
    private final String DATA_FILE_PATH = "../assets/data.json";

    public DataService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode getData() throws IOException {
        File file = new File(DATA_FILE_PATH);
        if (!file.exists()) {
            return objectMapper.createObjectNode();
        }
        return objectMapper.readTree(file);
    }

    public void saveData(JsonNode data) throws IOException {
        File file = new File(DATA_FILE_PATH);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }
}
