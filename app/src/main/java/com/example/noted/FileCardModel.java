package com.example.noted;

public class FileCardModel {
    String fileName, fileSize;

    public FileCardModel(String fileName, String fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getCreator() {
        return getFileName().split(" ")[0];
    }
}
