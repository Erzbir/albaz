## 插件框架的 core 模块

一个简单的实现, 并没有做插件依赖

提供了一个默认的 `PluginManager`, 并且插件通过 `PluginLoader` 加载后会包装成一个 `PluginWrapper`

这个 `PluginWrapper` 实现了生命周期以及一些隔离, 也实现了自定义异常处理

插件通过 `PluginManager` 调用 `PluginLoader` 来加载

`PluginLoader` 的实现中中:

> 注意, 这里所有的实现会判断文件头和后缀

- `ClassPluginLoader` - 用于加载 `.class` 类型的插件
- `FatJarPluginLoader` - 用于加载 `.jar` 类型的插件, 会加载 Jar 包中的所有 `class`
- `SpiPluginLoader` - 用于加载 `.jar` 类型的插件, 通过 `ServiceLoader` 来加载

所有的 `PluginLoader` 加载类时都是通过 `PluginClassLoader` 来加载的, 这个 `PluginClassLoader` 继承了 `URLClassLoader`

### JavaPluginManager

这是一个 `PluginManager` 的实现, 支持热重载 (前提是 JVM 也要支持)

这个实现中只可加载 `.jar` 和 `.class` 两种类型的插件