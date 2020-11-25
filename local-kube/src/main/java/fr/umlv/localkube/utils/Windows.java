package fr.umlv.localkube.utils;

public class Windows implements OperatingSystem {

    @Override
    public String getSeparator() {
        return "\\\\";
    }

    @Override
    public String getCMD() {
        return "cmd.exe";
    }

    @Override
    public String getOption() {
        return "/c";
    }

    @Override
    public String getHostOption() {
        return "";
    }
}
