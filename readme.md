### 数据库创建方法
1. 在mysql8下创建307f数据库
    ```mysql
    CREATE DATABASE `307f` CHARACTER SET 'utf8';
    ```
2. 在resources文件夹下，修改hibernate.cfg.xml中的mysql用户名和密码
    ```xml
    <property name="hibernate.connection.username">root</property>
    <property name="hibernate.connection.password">chenchen</property>
    ```

3. 不需要建立表格 运行时会自己创建