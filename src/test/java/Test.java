import cloud.computer.frpc.FRPCConfiguration;
import cloud.computer.frpc.Proxy;
import cloud.computer.frpc.ProxyType;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        FRPCConfiguration frpcConfiguration =
                new FRPCConfiguration(
                        new File("E:\\Java-Project\\maven-packages\\frpc-config\\src\\main\\resources\\frpc.toml"),
                        true
                );
        frpcConfiguration.load();

        Proxy proxy = new Proxy("112", ProxyType.TCP, "10.0.0.1", 80, 80);
        frpcConfiguration.addProxy(proxy);
        for (Proxy p : frpcConfiguration.getProxies()) {
            System.out.println(p);
        }
    }
}
