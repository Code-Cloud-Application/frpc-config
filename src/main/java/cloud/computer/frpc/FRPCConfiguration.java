package cloud.computer.frpc;

import cloud.computer.frpc.Exception.ConfigurationNotLoadException;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FRPCConfiguration {
    private final File ConfigFile;
    private String ServerAddress;
    private int ServerPort;
    private final List<Proxy> proxies = new ArrayList<>();
    private final boolean autosave;
    private CommentedFileConfig config;

    public FRPCConfiguration(File configFile) {
        ConfigFile = configFile;
        this.autosave = false;
    }

    public FRPCConfiguration(String path) {
        ConfigFile = new File(path);
        this.autosave = false;
    }

    public FRPCConfiguration(File configFile, boolean autosave) {
        ConfigFile = configFile;
        this.autosave = autosave;
    }

    public FRPCConfiguration(String path, boolean autosave) {
        ConfigFile = new File(path);
        this.autosave = autosave;
    }

    /**
     * 加载配置文件
     */
    public void load(){
        if(this.autosave){
            this.config = CommentedFileConfig.builder(this.ConfigFile)
                    .autosave()
                    .build();
        }else {
            this.config = CommentedFileConfig.builder(this.ConfigFile)
                    .build();
        }
        this.config.load();

        this.ServerAddress = this.config.get("serverAddr").toString();
        this.ServerPort = this.config.getInt("serverPort");
        List<CommentedConfig> commentedConfigs = this.config.get("proxies");
        for (CommentedConfig commentedConfig : commentedConfigs) {
            Map<String, Object> map = commentedConfig.valueMap();
            ProxyType type;
            switch (map.get("type").toString()){
                case "tcp" -> type = ProxyType.TCP;
                case "udp" -> type = ProxyType.UDP;
                case "http" -> type = ProxyType.HTTP;
                case "https" -> type = ProxyType.HTTPS;
                case null, default -> type = ProxyType.UNKNOWN;
            }
            Proxy proxy = new Proxy(
                    (String) map.get("name"),
                    type,
                    (String) map.get("localIP"),
                    Integer.parseInt(map.get("localPort").toString()),
                    Integer.parseInt(map.get("remotePort").toString())

            );
            this.proxies.add(proxy);
        }
    }


    /**
     * 设置frps服务器的地址
     * @param serverAddress 服务器地址
     * @throws ConfigurationNotLoadException 如果没有事先加载配置文件，则会抛出此异常
     */
    public void setServerAddress(String serverAddress) throws ConfigurationNotLoadException {
        if (this.config == null){
            throw new ConfigurationNotLoadException();
        }
        ServerAddress = serverAddress;
        this.config.set("serverAddr", serverAddress);
    }

    /**
     * 设置frps服务器的端口
     * @param serverPort 服务器端口
     * @throws ConfigurationNotLoadException 如果没有事先加载配置文件，则会抛出此异常
     */
    public void setServerPort(int serverPort) throws ConfigurationNotLoadException {
        if (this.config == null){
            throw new ConfigurationNotLoadException();
        }
        ServerPort = serverPort;
        this.config.set("serverPort", serverPort);
    }



    public String getServerAddress() {
        return ServerAddress;
    }

    public int getServerPort() {
        return ServerPort;
    }

    public List<Proxy> getProxies() {
        return proxies;
    }

    public void removeProxy(String name){
        this.proxies.removeIf(proxy -> proxy.getName().equals(name));
    }

    public void removeProxyByLocalIP(String LocalIP){
        this.proxies.removeIf(proxy -> proxy.getLocalIP().equals(LocalIP));
    }

    /**
     * 添加代理
     * @param proxy 需要添加的代理对象
     */
    public synchronized void addProxy(Proxy proxy){
        if(isExist(proxy.getName())) return;
        this.proxies.add(proxy);
        CommentedConfig subConfig = this.config.createSubConfig();
        subConfig.set("name", proxy.getName());
        String type = "tcp";
        switch (proxy.getType()){
            case TCP -> type = "tcp";
            case UDP -> type = "udp";
            case HTTP -> type = "http";
            case HTTPS -> type = "https";
        }
        subConfig.set("type", type);
        subConfig.set("remotePort", proxy.getRemotePort());
        subConfig.set("localIP", proxy.getLocalIP());
        subConfig.set("localPort", proxy.getLocalPort());
        ((List<CommentedConfig>) this.config.get("proxies")).add(subConfig);
        if(autosave) save();
    }


    public void updateProxy(Proxy proxy){
        for (Proxy p : this.getProxies()) {
            if(p.getName().equals(proxy.getName())){
                removeProxy(proxy.getName());
                addProxy(proxy);
            }
        }
    }


    /**
     * 判断是否存在名为name的代理
     *
     * @param name 代理名称
     * @return 如果存在名为name的代理返回true，否则返回false
     */
    public boolean isExist(String name) {
        return getProxies().stream()
                          .anyMatch(proxy -> proxy.getName().equals(name));
    }


    public void save(){
        this.config.save();
    }
}
