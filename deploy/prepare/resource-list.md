# 资源清单

## 1. 部署环境
|组件|前置|状态|
|---|---|---|
|docker desktop|无|done|
|docker desktop k8s|docker desktop|done|
|helm|docker desktop k8s|done|

## 1.1 k8s 预装资源清单
1. 中间件

|中间件|类型|前置|状态|
|---|---|---|---|
|nginx-ingress-controller|helm/chart|helm|done|
|mysql|helm/chart|helm|done|
|kafka|helm/kafka|helm|done|
|flink-operator|helm/operator|helm|done|

2. 一个本项目专属 namespace
```sh
kubectl create namespace flink-realtime-alert
```

## 2. helm 环境准备
1. ubuntu 安装 helm
```sh
curl https://baltocdn.com/helm/signing.asc | gpg --dearmor | sudo tee /usr/share/keyrings/helm.gpg > /dev/null
sudo apt-get install apt-transport-https --yes
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/helm.gpg] https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list
sudo apt-get update
sudo apt-get install helm
```
2. 添加一个常用 repo
```sh
helm repo add bitnami https://charts.bitnami.com/bitnami
```


## 3. helm 安装 mysql
### 3.1 修改默认 mysql 配置
1. 检索 repo & 下载 mysql
```sh
# 搜索 repo 中的 mysql
helm search repo mysql

# 拉取包到本地，用于修改默认配置
helm pull bitnami/mysql

# 解压包
tar -xvf mysql-9.9.1.tgz
```
2. 修改 values.yaml
```yaml
# 默认密码
auth:
  ## @param auth.rootPassword Password for the `root` user. Ignored if existing secret is provided
  ## ref: https://github.com/bitnami/containers/tree/main/bitnami/mysql#setting-the-root-password-on-first-run
  ##
  rootPassword: "root"

# 使用 NodePort 把 mysql 暴露 30006 端口到宿主机
  service:
    ## @param primary.service.type MySQL Primary K8s service type
    ##
    type: NodePort
    ## @param primary.service.ports.mysql MySQL Primary K8s service port
    ##
    ports:
      mysql: 3306
    ## @param primary.service.nodePorts.mysql MySQL Primary K8s service node port
    ## ref: https://kubernetes.io/docs/concepts/services-networking/service/#type-nodeport
    ##
    nodePorts:
      mysql: 30006

# 开启 binlog , 最后三行
  configuration: |-
    [mysqld]
    default_authentication_plugin=mysql_native_password
    skip-name-resolve
    explicit_defaults_for_timestamp
    basedir=/opt/bitnami/mysql
    plugin_dir=/opt/bitnami/mysql/lib/plugin
    port=3306
    socket=/opt/bitnami/mysql/tmp/mysql.sock
    datadir=/bitnami/mysql/data
    tmpdir=/opt/bitnami/mysql/tmp
    max_allowed_packet=16M
    bind-address=*
    pid-file=/opt/bitnami/mysql/tmp/mysqld.pid
    log-error=/opt/bitnami/mysql/logs/mysqld.log
    character-set-server=UTF8
    collation-server=utf8_general_ci
    slow_query_log=0
    slow_query_log_file=/opt/bitnami/mysql/logs/mysqld.log
    long_query_time=10.0
    server-id=1
    binlog_format="ROW"
    log_bin=/opt/bitnami/mysql/logs/mysql_bin
```

### 3.2 安装脚本
```sh
helm install --name-template mysql -f values.yaml . --namespace flink-realtime-alert
```


## 4. helm 安装 nginx controller
```sh
helm install bitnami/nginx-ingress-controller --name-template ng-ctl --namespace flink-realtime-alert
```

## 5. helm 安装 kafka
```sh
helm install kafka bitnami/kafka --namespace flink-realtime-alert
```
## 6. helm 安装 flink-operator
1. 装一个证书
```sh
kubectl create -f https://github.com/jetstack/cert-manager/releases/download/v1.8.2/cert-manager.yaml
```
2. 装 operator
```sh
# 新增一个 repo
helm repo add flink-operator-repo https://downloads.apache.org/flink/flink-kubernetes-operator-1.4.0/

# 安装 flink-kubernetes-operator
helm install flink-kubernetes-operator flink-operator-repo/flink-kubernetes-operator --namespace flink-realtime-alert
```
3. 测试
```sh
# 一个 k8s CRD , 包含一个 flink job
kubectl create -f https://raw.githubusercontent.com/apache/flink-kubernetes-operator/release-1.4/examples/basic.yaml --namespace flink-realtime-alert

# 查看日志
kubectl logs -f deploy/basic-example --namespace flink-realtime-alert

# 临时暴露 svc 到宿主机 8081 端口
kubectl port-forward svc/basic-example-rest 8081 --namespace flink-realtime-alert
```


## 7. 查看 helm 所有空间的 release
```sh
helm list -A
```