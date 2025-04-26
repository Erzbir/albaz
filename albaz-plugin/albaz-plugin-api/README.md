## 插件框架的 api 模块

可以通过 `PluginManagerProvider` 来获取一个 `PluginManager` 的实现

例如:

```java
PluginManager pluginManager = PluginManagerProvider.INSTANCE.getInstance();
```

- `PluginLoader` - 插件加载器接口, 加载文件形式的插件
- `PluginDescription` - 插件描述, 其中包含 id, version 等
- `Plugin` - 插件接口, 定义了生命周期回调
- `JavaPlugin` - 插件抽象类, 所有 Java 的插件都应该继承这个类