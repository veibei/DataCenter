#DTU数据中心

##简介
> DTU数据中心是为DTU（Data Transfer Unit）转发数据建立的系统,它有数据中心子系统和数据中心管理子系统构成。其中数据中心子系统负责与DTU建立连接，转发DTU数据。管理子系统提供基于Web浏览器的监控页面，监控数据中心子系统的运行状态

##相关技术
- DTU数据中心子系统采用Apache MINA作为NIO连接框架，未来可能会前移至Netty
- DTU数据中心管理子系统采用AngularJs作为前端框架，后端由Spring MVC提供rest接口，BootStrap提供页面样式
- 管理子系统基于WebSocket实现了实时监控数据推送功能，WebSocket基于Tomcat实现

##构建流程

- 获取工程代码 :`git clone https://github.com/QuanCong/DataCenter.git `	
-  `cd DataCenter/data` 执行sql语句还原数据库
- 构建数据中心子系统工程代码
```
   cd DataCenter/DeployApp_V2
   mvn package
```
- 修改配置文件参数
```
cd DataCenter/DeployApp_V2/conf
修改server.properties
server.ip=yourip #数据中心地址
server.port=yourport #数据中心连接端口
mc.servers=172.27.48.230:50120 #memcached地址
修改log4j.properties
log4j.appender.db.URL=jdbc:mysql://172.27.48.230:3306/test?useUnicode=true&characterEncoding=UTF-8 #修改数据库地址
```

- 启动数据中心：
```
cd DataCenter/DeployApp_V2/target
java -jar DDC-1.0-SNAPSHOT.jar E:/conf/deploy_app/
```
- 修改数据中心管理系统配置，修改memcached.xml和spring-dao-context.xml 修改其中memcached和mysql的地址
- 部署tomcat启动

##数据中心命令行操作
启动数据中心子系统以后，可以通过命令行交互的形式修改一些参数

* set  ip:port 修改数据中心启动ip和端口 需要在启动前修改
* start 启动数据中心
* stop  关闭数据中心
* exit  退出系统



