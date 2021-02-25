# Windows 启停mysql

## 初始化
* mysqld.exe --defaults-file=D:/mysql-8.0.23-winx64/conf/my.cnf --initialize-insecure --console

## 启动停止
* C:\> ./mysqld --defaults-file=D:/mysql-8.0.23-winx64/conf/my.cnf --console
* C:\> "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqladmin" -u root shutdown

C:\> "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld"
          --install MySQL --defaults-file=C:\my-opts.cnf
https://dev.mysql.com/doc/refman/8.0/en/windows-start-command-line.html
https://dev.mysql.com/doc/refman/8.0/en/windows-start-service.html