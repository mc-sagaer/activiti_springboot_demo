# activiti_springboot_demo
---from 黑马，修改了下测试类的注解配置

### 使用前谨记修改数据库属性

#### QA
1. 启动项目后，并未自动创建表(database-schema-update: 已经为true)

在使用mysql-connect 8.+以上版本的时候需要添加nullCatalogMeansCurrent=true参数，否则在使用mybatis-generator生成表对应的xml等时会扫描整个服务器里面的全部数据库中的表，而不是扫描对应数据库的表。

2. Activiti7可以自动部署流程

自己去yml里配置一些参数，包括流程图的文件包