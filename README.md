# environment

一个基于 Spring Boot + Thymeleaf 的**项目申报与积分排行系统**：用户在线提交项目申报，管理员审核编辑，系统按累计积分生成排行榜。

## 功能概览

### 用户端
- 注册 / 登录 / 退出（登录带图形验证码校验）
- 个人详情页：查看本人申报记录与累计积分
- 项目申报：填写项目名称、项目描述、申报积分、上传图片
- 申报历史：按本人或按项目名检索，支持编辑与删除
- 排行榜：按累计积分分页查看全站排名

### 管理端
- 管理员独立登录入口
- 查看全部申报详情
- 编辑申报内容与状态、积分

### 通用
- 验证码接口 `/validateCode`，AWT 动态生成 50×20 图片并写入 Session
- 排行榜自动更新：访问详情页时按最新积分重算排名

## 技术栈

| 分类 | 选型 |
| --- | --- |
| 语言 | Java 17 |
| 框架 | Spring Boot 3.0.2（Web / Thymeleaf / Data Redis） |
| ORM | MyBatis-Plus 3.5.3.1 |
| 数据库 | MySQL 8（`mysql-connector-java` 8.0.33） |
| 缓存 | Redis（`spring-boot-starter-data-redis`） |
| 模板 | Thymeleaf |
| 前端 | Bootstrap、jQuery 3.6、ECharts、Font Awesome |
| 工具 | Lombok |

## 目录结构

```
src/main/java/com/ch/environment
├── Config/            MyBatis-Plus 配置（分页插件等）
├── Controller/        UserController / AdminController / Pappcontroller / ValidateCodeController
├── Entity/            User、Admin、Projectapplication、Urank、Userscorehistory
├── Mapper/            对应各表的 MyBatis-Plus Mapper
├── Service/           业务接口与实现
└── EnvironmentApplication.java
src/main/resources
├── application.properties   数据源等配置
├── static/                   css / js / images
└── templates/                login、register、detail、add、edit、history、rank、search、ad* 等页面
```

## 数据表

| 表名 | 说明 |
| --- | --- |
| `user` | 用户（userid、username、password、department、classname、status、totalpoint） |
| `admin` | 管理员（adminid、adminname、password） |
| `projectapplication` | 项目申报（projectid、userid、projectname、points、projectdesc、status、imagepath、createtime、updatetime） |
| `urank` | 排行榜（userid、username、totalpoint、ranksort、updatetime） |
| `userscorehistory` | 积分流水（recordid、userid、username、projectdesc、points） |

建库语句需自行准备，库名默认为 `environment`。

## 快速开始

### 1. 环境准备
- JDK 17
- Maven 3.8+（或直接使用仓库内的 `mvnw` / `mvnw.cmd`）
- MySQL 8.0
- Redis（如未使用缓存功能可暂不启动）

### 2. 初始化数据库

```sql
CREATE DATABASE environment DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

导入上述五张表结构后，插入一条管理员记录即可登录后台：

```sql
INSERT INTO admin (adminname, password) VALUES ('your_admin_name', 'your_password');
```

### 3. 修改配置

`src/main/resources/application.properties` 中填写自己的数据库连接信息。：

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/environment?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:}
```

### 4. 启动

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

或先打包再运行：

```bash
mvn clean package -DskipTests
java -jar target/environment-0.0.1-SNAPSHOT.jar
```

默认端口 `8080`。

### 5. 访问地址

| 页面 | 路径 |
| --- | --- |
| 用户登录 | `/user/` |
| 注册 | `/user/register` |
| 管理员登录 | `/admin/adLogin` |
| 申报项目 | `/app/toAdd` |
| 申报历史 | `/app/tohistory` |
| 排行榜 | `/app/toRank` |
| 验证码 | `/validateCode` |

## 主要接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/user/login` | 用户登录校验 |
| GET | `/user/logout` | 退出登录（销毁 Session） |
| GET | `/user/detail` | 用户详情 |
| POST | `/app/add` | 提交申报（`act=add`） |
| GET | `/app/toRank?page=1&size=10` | 排行榜分页 |
| GET | `/app/toEdit?projectid=` | 编辑页 |
| POST | `/app/edit` | 保存编辑 |
| DELETE | `/app/deleteRecord/{projectid}` | 删除申报记录 |
| GET | `/admin/adLoginVerify` | 管理员登录校验 |
| POST | `/admin/edit` | 管理员修改申报 |

## 开发约定

- Session 中用户对象键名 `userlogin`，管理员键名 `adminlogin`，验证码键名 `rand`
- Controller 返回 Thymeleaf 模板名，业务逻辑放在 `Service` 层
- 新增表时同时补 `Entity`、`Mapper`、`Service`、`ServiceImpl` 四层

## 待优化

- 密码目前为明文存储，建议接入 BCrypt 等哈希算法
- 数据源与密钥改为配置中心 / 环境变量管理
- 补充统一异常处理与参数校验
- 增加单元测试覆盖

## 许可证

暂未指定，如需开源可补充 MIT 或 Apache-2.0。
