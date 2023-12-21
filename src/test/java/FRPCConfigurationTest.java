import cloud.computer.frpc.FRPCConfiguration;
import cloud.computer.frpc.ProxyType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import cloud.computer.frpc.Proxy;
import cloud.computer.frpc.Exception.ConfigurationNotLoadException;
import org.junit.jupiter.api.TestInstance;

import java.io.File;


public class FRPCConfigurationTest {

    private FRPCConfiguration frpcConfig;

    @BeforeEach
    public void setup() {
        File configFile = new File("src/main/resources/frpc.toml");
        frpcConfig = new FRPCConfiguration(configFile);
        frpcConfig.load();
    }

    @Test
    public void testLoad() {
        frpcConfig.load();
        Assertions.assertEquals("127.0.0.1", frpcConfig.getServerAddress());
        Assertions.assertEquals(7000, frpcConfig.getServerPort());
    }

    @Test
    public void testSetServerAddress() {
        Assertions.assertThrows(ConfigurationNotLoadException.class, () -> {
            frpcConfig.setServerAddress("new-server");
        });

        frpcConfig.load();
        Assertions.assertDoesNotThrow(() -> {
            frpcConfig.setServerAddress("new-server");
        });
    }

    @Test
    public void testSetServerPort() {
        Assertions.assertThrows(ConfigurationNotLoadException.class, () -> {
            frpcConfig.setServerPort(8081);
        });

        frpcConfig.load();
        Assertions.assertDoesNotThrow(() -> {
            frpcConfig.setServerPort(8081);
        });
    }

    @Test
    public void testAddProxy() {
        Proxy proxy1 = new Proxy("proxy1", ProxyType.TCP, "192.168.0.1", 8081, 8082);
        Proxy proxy2 = new Proxy("proxy2", ProxyType.UDP, "192.168.0.2", 8083, 8084);
        frpcConfig.addProxy(proxy1);
        frpcConfig.addProxy(proxy2);
        Assertions.assertEquals(4, frpcConfig.getProxies().size());
    }

    @Test
    public void testUpdateProxy() {
        Proxy proxy = new Proxy("proxy1", ProxyType.TCP, "192.168.0.1", 8081, 8082);
        frpcConfig.addProxy(proxy);
        Assertions.assertEquals(1, frpcConfig.getProxies().size());

        Proxy newProxy = new Proxy("proxy1", ProxyType.UDP, "192.168.0.1", 8081, 8082);
        frpcConfig.updateProxy(newProxy);
        Assertions.assertEquals(1, frpcConfig.getProxies().size());
        Assertions.assertEquals(newProxy, frpcConfig.getProxies().get(0));
    }

    @Test
    public void testIsExist() {
        Proxy proxy = new Proxy("proxy1", ProxyType.TCP, "192.168.0.1", 8081, 8082);
        frpcConfig.addProxy(proxy);
        Assertions.assertTrue(frpcConfig.isExist("proxy1"));
        Assertions.assertFalse(frpcConfig.isExist("proxy2"));
    }

}
