# Windows 启停mysql

## 初始化
* bin\mysqld --initialize --console
* bin\mysqld --initialize-insecure --console
* --defaults-file=/opt/mysql/mysql/etc/my.cnf
    --initialize --user=mysql

## 启动停止
* C:\> "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld" --console
* C:\> "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqladmin" -u root shutdown

C:\> "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld"
          --install MySQL --defaults-file=C:\my-opts.cnf
https://dev.mysql.com/doc/refman/8.0/en/windows-start-command-line.html
https://dev.mysql.com/doc/refman/8.0/en/windows-start-service.html