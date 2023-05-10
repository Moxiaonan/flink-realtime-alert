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