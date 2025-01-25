# FriendsPlugin
 
### 1、简介：基于Minecraft Spigot1.12的服务器集群好友插件

---

## 2、权限
```
Team.Normal:
  default: true
Team.Admin:
  default: op
```

### 3、命令
```

/hy tj <玩家> - 申请添加某人为好友 - Team.Normal:
/hy sc <玩家> - 删除好友- Team.Normal:
/hy js <玩家> - 接收某人的好友申请 - Team.Normal:
/hy lb <页数> - 查看好友列表 - Team.Normal:
/hyop vip <玩家> - 为某人开通更多好友位 - Team.Admin
/hyop gui <玩家> - 为某人打开好友GUI - Team.Admin
/fm <玩家> <内容> - 好友私聊 - Team.Normal:
/fr <内容> - 快速回复 - Team.Normal:
/fg <内容> - 发送好友全局消息 - Team.Normal:

```

### 4、config.yml
```yml
#TeamPlugin by X_32xm
#for only Dxzzz.net
#QQ:2644489337

#！！！注意：1、本插件需要手动创建一个Friends数据库,插件会自动连接到数据库并创建表！！！
#          2、本插件需要Authme数据库支持

#数据库设置(仅支持MySQL)
DataBase:
  MySQL:
    Host: localhost
    Username: root
    Password:
    Port: 3306

#当前插件是否在Lobby中运行
AsLobby: true

```
