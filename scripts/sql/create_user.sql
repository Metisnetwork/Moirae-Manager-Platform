# create user 'rosettaflow'@'%' identified by 'rosettaflow';
# GRANT ALL ON *.* to 'rosettaflow'@'%';
# flush privileges;

# 创建程序用户
use mysql;
create user 'moirae'@'%' identified by 'moirae@2021';

# 授权
grant select,insert,update,delete,execute,trigger,lock tables on db_moirae.* to 'moirae'@'%';
flush privileges;
