# 内网穿透配置文件修改器
----
## 这是什么？
内网穿透配置文件修改器是一项基于 FRPC 内网穿透软件的配置文件修改器。其主要目的是根据`toml`语法来修改 FRPC 配置文件（即`frpc.toml`）。它允许您以更快、更高效的形式通过Java应用程序来修改此配置文件。

## 如何使用？
### 基于Maven构建的应用程序
如果您的应用程序是基于Maven构建，您大可在您的`pom.xml`中的`<dependencies></dependencies>`标签中添加如下内容：
```xml
<dependency>
  <groupId>cloud.computer</groupId>
  <artifactId>frpc-config</artifactId>
  <version>1.0.0</version>
</dependency>
```
**注意：由于这是团队内部产品，您务必添加团队仓库才能找到此包**，此包尚未发布到`maven-central`

### 基于Gradle Groovy DSL构建的应用程序
如果您的应用程序时基于Gradle Groovy DSL构建，您可以在相关文件内加入如下内容以启用此包：
```
implementation 'cloud.computer:frpc-config:1.0.0'
```

### 基于Gradle Kotlin DSL构建的应用程序
如果您的应用程序时基于Gradle Kotlin DSL构建，您可以在相关文件内加入如下内容以启用此包：
```
implementation("cloud.computer:frpc-config:1.0.0")
```

## 基本使用方法
```java
FRPCConfiguration config = new FRPCConfiguration("/path/to/file.toml");
config.load();  //您必须首先加载，否则后续您将无法进行操作
config.getServerAddress();  // 获取要连接的服务器的地址
config.getServerPort();  // 获取要连接的服务器的端口号
List<Proxy> proxies = config.getProxies(); // 获取所有已配置在文件中的代理列表
...

```

