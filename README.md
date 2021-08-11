## RosettaFlow-api-server 管理后台

### 1.后台服务api接口文档，采用swagger访问地址如下
http://10.10.8.182:8234/rosettaflow-console/doc.html

### 2.Jenkins自动部署管理后台
> 为了不影响前端开发，目前设置手动构建
- 访问地址：http://10.10.8.182:8080/login?from=%2F
- 用户名：admin
- 密码：admin
- 将代码提交develop分支，点击构建，如下图所示：
![执行构建](./scripts/images/jenkins构建后台服务.png)


