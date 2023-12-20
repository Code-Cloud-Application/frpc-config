package cloud.computer.frpc;

public class Proxy {
    private String name;
    private ProxyType type;
    private String LocalIP;
    private int LocalPort;
    private int RemotePort;

    public Proxy(String name, ProxyType type, String localIP, int localPort, int remotePort) {
        this.name = name;
        this.type = type;
        LocalIP = localIP;
        LocalPort = localPort;
        RemotePort = remotePort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProxyType getType() {
        return type;
    }

    public void setType(ProxyType type) {
        this.type = type;
    }

    public String getLocalIP() {
        return LocalIP;
    }

    public void setLocalIP(String localIP) {
        LocalIP = localIP;
    }

    public int getLocalPort() {
        return LocalPort;
    }

    public void setLocalPort(int localPort) {
        LocalPort = localPort;
    }

    public int getRemotePort() {
        return RemotePort;
    }

    public void setRemotePort(int remotePort) {
        RemotePort = remotePort;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", LocalIP='" + LocalIP + '\'' +
                ", LocalPort=" + LocalPort +
                ", RemotePort=" + RemotePort +
                '}';
    }
}
