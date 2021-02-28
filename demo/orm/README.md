# Windows 启停mysql

## 初始化

* mysqld.exe --defaults-file=D:/mysql-8.0.23-winx64/conf/my.cnf --initialize-insecure --console

## 启动停止

* C:\> ./mysqld --defaults-file=D:/mysql-8.0.23-winx64/conf/my.cnf --console
* C:\> "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqladmin" -u root shutdown

## db1

* db1使用了mybatis plus

## db2

* db2使用了mybatis pagehelper