package fr.umlv.localkube.utils;

/**
 * This interface contains methods related to the operating system used.
 */
public interface OperatingSystem {

    /**
     * Returns a string containing command to open console according to the OS.
     *
     * @return the resulting string
     */
    String getCMD();

    /**
     * Returns a string containing an option for docker run command according to the OS.
     *
     * @return the resulting string
     */
    default String getHostOption() {
        return "";
    }

    /**
     * Returns a string containing an option for command according to the OS.
     *
     * @return the resulting string
     */
    String getOption();

    /**
     * Returns a string containing wlo1 address for docker swarm init command.
     *
     * @return the resulting string
     */
    default String getWlo1IpAddress() {
        return "";
    }

    /**
     * Checks on which OS this program is running.
     *
     * @return a new object which represents the OS (ex: Windows, Unix)
     */
    static OperatingSystem checkOS() {
        var os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            return new Windows();
        } else {
            return new Unix();
        }
    }
}
