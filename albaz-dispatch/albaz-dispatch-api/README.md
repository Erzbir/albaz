## 事件驱动框架的 api 模块

可以通过 `EventDispatcherProvider` 来获取一个 `EventDispatcher` 的实现

### EventChannel

只是事件的通道, 并不能作为入口

可以通过事件通道来注册监听器和拦截器

通过 `filter()` 和 `filterInstance()` 来过滤出一个子通道

监听器在其中存在的形式有三种: 只监听一次的监听器; 持续监听的监听器; 被截断的监听器

### EventDispatcher

提供了两个接口一个是同步的 `EventDispatcher`, 一个是异步的 `AsyncEventDispatcher` 继承了 `EventDispatcher`

`EventDispatcher` 可以通过 `async()` 方法来升级为异步实现

`AsyncEventDispatcher` 也可以通过 `of()` 来将一个 `EventDispatcher` 转换为异步实现

事件的入口只有 `EventDispatcher`, 通过 `EventDispatcher` 来分发到 `EventChannel` 中

### Event

事件可以被拦截, 通过 `intercepted` 来拦截一个事件, 拦截可以发生在 `Listener` 中, 也可以发生在 `Interceptor` 中

如果在 `Listener` 中被拦截, 应该遵循一个规则: 监听器拦截后事件后, 事件将不再会到达优先级较小的监听器中

### Listener

`Listener` 有三种执行结果用于判断这个 `Listener` 该不该继续监听以及该不该被移除:

- CONTINUE - 继续监听
- TRUNCATED - 被截断, 通常是因为监听器中发生了错误
- STOP - 停止监听, 停止监听后这个监听器就应该被移除