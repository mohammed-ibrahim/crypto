package org.crypto.service;

class Config {
    public enum OperationMode {
        ENCRYPT,
        DECRYPT
    }

    private String inputFileName;

    private String outputFileName;

    private OperationMode mode;

    private String secretKey;

    public Config() {

    }

    public Config(String inputFileName, String outputFileName, OperationMode mode, String secretKey) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.mode = mode;
        this.secretKey = pad(secretKey);
    }

    public void withInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void withOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void withMode(OperationMode mode) {
        this.mode = mode;
    }

    public void withSecretKey(String secretKey) {
        this.secretKey = pad(secretKey);
    }

    public String getInputFileName() {
        return this.inputFileName;
    }

    public String getOutputFileName() {
        return this.outputFileName;
    }

    public OperationMode getMode() {
        return this.mode;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    private String pad(String key) {
        String padkey = "###################################################";

        if (key.length() < 16) {
            return key + padkey.substring(0, 16 - key.length());
        }

        return key;
    }
}
