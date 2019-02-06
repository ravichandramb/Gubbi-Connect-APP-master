package com.mygubbi.imaginestclientconnect.models;

public class UnzippedFiles {

    private String fileName;
    private String filePath;

    public UnzippedFiles(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return "UnzippedFiles{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }

}