# 问题记录

## -------2023.05.10--------
### 访问不通
现象：使用 helm install 时报错，kubernetes.docker.internal:6443 访问不通
解决过程：
1. 查看 hosts , 域名没有问题
```sh
cat /etc/hosts
```
2. ping kubernetes.docker.internal 可以 ping 通
3. 在 docker 中部署一个 nginx , 绑定本地 6100 端口
4. 可以访问 nginx 欢迎页
```sh
curl localhost:6100
```
5. 无法访问 nginx 欢迎页
```sh
curl kubernetes.docker.internal：6100
```
6. 怀疑是 ufw 问题 ，关闭防火墙 , 重启
```sh
sudo ufw disable
```
7. 还是不行
8. 查看头信息
```sh 
curl -v localhost:6100
```
9. 查看头信息
```sh
curl -v kubernetes.docker.internal:6100
```
10. 发现 curl -v kubernetes.docker.internal:6100 头信息显示这个连接会走本地翻墙工具 clash 代理
11. 打开系统设置 - 网络 - 代理 ， 在排除名单中添加域名 kubernetes.docker.internal ， 重启
12. 解决问题

原因：本地开了代理 ，但是代理排除名单中没有本地 k8s 的默认域名 ，导致出错

## -------23.05.11---------
### maven 引包错误
1. flink 程序无法启动
2. 改用 flink cdc 2.0.0 ，参照以前的项目 https://github.com/Moxiaonan/flink-test.git
2. 成功启动

### flink cdc 2.0.0 没有合适的反序列化器用于解析 binglog
1. 升级到 pom.xml 到 flink cdc 2.1.0，这个版本有默认的 json 反序列化器

### flink cdc 2.1.0 启动报 ClassNoFound错
1. 引包问题，要把 flink-table-XXX 的几个包一起引入

### flink 闪退
1. 引包完成后，发现程序启动失败，闪退，无提示信息
2. 猜测是 log 日志没配，看不到报错信息
3. flink 使用 slf4j 这个日志接口规范，但是并没有实现，需要引入一个日志实现包，这次使用 log4j
4. 引入 slf4j & log4j 相关包，在项目 resource 目录下新增 log4j.properties 文件，用于配置 log4j
5. 重新启动应用，控制台打印出报错信息，空指针

### 空指针异常
1. 跟踪源码发现 NPE 是由于 MysqlSource.builder() 构建的时候，没有传递 tableList 信息导致的
2. builder() 传入一个 "" 空字符串
3. 重启应用，成功启动

### mysql bigint unsigned 字段乱码
1. 发现控制台打印的 binlog json 有乱码，而且只有 mysql bigint unsigned 字段乱码
2. 搜 google ，找到答案，参考 https://github.com/ververica/flink-cdc-connectors/wiki/FAQ(ZH)
3. 按照答案，在 MysqlSource.builder() 增加一个 properties 配置
4. 重启应用，解决