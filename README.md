# AndroidDataBase
数据库架构实现增删改查，多表分库，通过xml全局升级


多表分库针对每一个登陆用户会创建单独的数据库，总表记录了每一个分库的路径以获取对应的DataBase。

xml升级，xml定义格式如下：

首先应该写入当前的版本号和最新的版本号到本地，可以写入file的形式。升级的时候会读取此文件，到xml中找到要升级的版本，获取sql语句来实现升级的功能。

升级步骤，

1、执行rename操作。

2、执行新建表操作。

3、讲rename表中的数据copy到新建的表中，然后删除rename表。

考虑到安全问题，可以在执行升级之前把当前数据库中的数据备份，完成升级后删除即可。


<!-- 请保证该文档一定是 UTF-8编码 -->
<updateXml>
    
    <createVersion version="V003">
    
        <createDb name="user">
        
            <!-- 设备与软件关联信息 -->
            
            <sql_createTable>
            
                create table if not exists tb_user(
                
                name TEXT,
                
                password TEXT,
                
                loginName TEXT,
                
                lastLoginTime TEXT,
                
                _id Text
                
                );
                
            </sql_createTable>
            
        </createDb>
        
        <createDb name="login">
        
            <!-- 设备与软件关联信息 -->
            
            <sql_createTable>
            
                create table if not exists tb_photo(
                
                time TEXT,
                
                path TEXT,
                
                to_user TEXT,
                
                sendTime TEXT
                
                );
                
            </sql_createTable>
            
        </createDb>
        
    </createVersion>
    
    <updateStep
    
        versionFrom="V002"
        
        versionTo="V003">
        
        <updateDb name="login">
        
            <sql_before>alter table tb_photo rename to bak_tb_photo;</sql_before>
            
            <sql_after>
            
                insert into tb_photo(time,
                
                path)
                
                select time,path
                
                from bak_tb_photo;
                
            </sql_after>
            
            <sql_after>
            
                drop table if exists bak_tb_photo;
                
            </sql_after>
            
        </updateDb>
        
        <updateDb name="user">
        
            <sql_before>alter table tb_user rename to bak_tb_user;</sql_before>
            
            <sql_after>
            
                insert into tb_user(name,
                
                password,_id)
                
                select name,password,_id
                
                from bak_tb_user;
                
            </sql_after>
            
            <sql_after>
            
                drop table if exists bak_tb_user;
                
            </sql_after>
            
        </updateDb>
        
    </updateStep>
    

</updateXml>

