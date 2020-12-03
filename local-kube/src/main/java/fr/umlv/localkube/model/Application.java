package fr.umlv.localkube.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class Application {

    public interface View {
         interface OnStart {}
         interface OnListAndStop extends OnStart {}
    }

    private static final int MIN_PORT_SERVICE = 49152;
    private static final int MAX_PORT_SERVICE = 65535;
    private final long startTime = System.currentTimeMillis();

    /**
     * Application ID
     */
    @JsonProperty("id")
    @JsonView(View.OnStart.class)
    private final int id;
    /**
     * Application name
     */
    @JsonProperty("app")
    @JsonView(View.OnStart.class)
    private final String app;
    /**
     * Application public port
     */
    @JsonProperty("port")
    @JsonView(View.OnStart.class)
    private final int portApp;
    /**
     * Application service/private port
     */
    @JsonProperty("service-port")
    @JsonView(View.OnStart.class)
    private final int portService;
    /**
     * Docker instance name
     */
    @JsonProperty("docker-instance")
    @JsonView(View.OnStart.class)
    private final String dockerInstance;
    /**
     * Elapsed time since application launch
     */
    @JsonProperty("elapsed-time")
    @JsonView(View.OnListAndStop.class)
    private String elapsedTime;

    public static class ApplicationBuilder {
        private int id;
        private String app;
        private int portApp;
        private int portService;
        private String dockerInstance;

        public Application build() {
            if (id <= 0) {
                throw new IllegalStateException("id can't be negative or equal to 0");
            }
            return new Application(id, app, portApp, portService, dockerInstance);
        }

        public Application buildRandom() {
            var id = new Random().nextInt(100_000) + 1; // pour éviter 0
            var portApp = new Random().nextInt(65536);
            var portService = new Random().nextInt(65536);
            var app = "hello:" + portApp;
            var dockerInstance = "hello_" + portApp;
            return new Application(id, app, portApp, portService, dockerInstance);
        }

        public ApplicationBuilder setId(int id) {
            this.id = id;
            return this;
        }
        public ApplicationBuilder setApp(String app) {
            this.app = app;
            return this;
        }
        public ApplicationBuilder setportApp(int portApp) {
            this.portApp = portApp;
            return this;
        }
        public ApplicationBuilder setportService(int portService) {
            this.portService = portService;
            return this;
        }
        public ApplicationBuilder setDockerInstance(String dockerInstance) {
            this.dockerInstance = dockerInstance;
            return this;
        }

    }

    public static Application initializeApp(String app, int id) throws IOException {
        if (id <= 0) {
            throw new IllegalArgumentException("id can't be negative");
        }
        Objects.requireNonNull(app);
        var portApp = getPortFromName(app);
        var portService = getAvailablePortService();
        var dockerInstance = app.split(":")[0] + "_" + portApp;
        return new Application(id, app, portApp, portService, dockerInstance);
    }

    private Application(int id, String app, int portApp, int portService, String dockerInstance) {
        this.id = id;
        this.app = app;
        this.portApp = portApp;
        this.portService = portService;
        this.dockerInstance = dockerInstance;
    }

    private static int getAvailablePortService() throws IOException {
        var port = MIN_PORT_SERVICE-1;
        while (port < MAX_PORT_SERVICE+1) {
            try {
                port++;
                var s = new ServerSocket(port);
                s.close();
                break;
            } catch (IOException ignored){
            }
        }
        if (port > MAX_PORT_SERVICE) {
            throw new IOException("no available private port found");
        }
        return port;
    }

    @JsonIgnore
    public String getJarName() {
        return getName() + ".jar";
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public String getName() {
        return app.split(":")[0];
    }

    public int getPortService() {
        return portService;
    }

    public int getPortApp() {
        return portApp;
    }

    public String getDockerInstance() {
        return dockerInstance;
    }

    public String getElapsedTime() {
        elapsedTime = formatElapsedTime(System.currentTimeMillis());
        return elapsedTime;
    }

    private static int getPortFromName(String app) {
        var strPort = app.split(":")[1];
        return Integer.parseInt(strPort);
    }

    private String formatElapsedTime(long endTime) {
        var calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endTime - startTime);
        return calendar.get(Calendar.MINUTE) + "m" + calendar.get(Calendar.SECOND) + "s";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Application a &&
                id == a.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", port=" + portApp +
                ", service-port=" + portService +
                ", docker-instance='" + dockerInstance + '\'' +
                ", elapsed-time='" + getElapsedTime() + '\'' +
                '}';
    }
}
