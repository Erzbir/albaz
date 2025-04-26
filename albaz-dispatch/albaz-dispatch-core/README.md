## 事件驱动框架的实现层

实现了两个 `EventDispatcher`:

- `NotificationDispatcher`
- `PollingEventDispatcher`

这两个实现不同之处就在于, `PollingEventDispatcher` 是基于轮询的, 内部维护了一个 `PriorityBlockingQueue`, 可以很好的支持事件优先级;
而 `NotificationEventDispatcher` 是一个即时触发的, 内部没有任何缓存机制


对于 `EventChannel` 的实现, 这里是用总线来实现的, 最顶层的父通道是 `GlobalEventChannel`, 可以通过 `filter()` 方法过滤出一些子通道, 但是最终都会委托到父通道中, 而 `FilterEventChannel` 的实现更像是在子通道和父通道中间加上一层 "滤网"