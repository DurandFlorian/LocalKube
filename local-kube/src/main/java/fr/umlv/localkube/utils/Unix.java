package fr.umlv.localkube.utils;

public class Unix implements OperatingSystem {

    @Override
    public String getSeparator() {
        return "/";
    }

    @Override
    public String getCMD() {
        return "bash";
    }
}
