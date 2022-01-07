# 创建程序用户
use mysql;
create user 'moirae'@'%' identified by 'moirae@2021';

# 授权
GRANT ALL ON db_moirae.* to 'moirae'@'%';
# grant select,insert,update,delete,execute,trigger,lock tables on db_moirae.* to 'moirae'@'%';
flush privileges;
