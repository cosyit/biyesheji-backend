drop table if exists `user`;
create table if not exists `user`
(
    id     int primary key auto_increment,
    name   varchar(50) not null comment '教师姓名',
    gender smallint comment '性别 null/0其他 1男 2女',
    age    smallint    not null comment '年龄'
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '用户表' ;

drop table if exists `group`;
create table if not exists `group`
(
    id   int auto_increment primary key,
    name varchar(50) not null comment '组名'
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '用户组表';

drop table if exists `user_group`;
create table if not exists `user_group`
(
    id       int primary key auto_increment,
    user_id  int not null,
    group_id int not null
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '用户-组关联表';

drop table if exists `role`;
create table if not exists `role`
(
    id   int auto_increment primary key,
    name varchar(50) not null comment '角色名'
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '角色表';

drop table if exists `group_role`;
create table if not exists `group_role`
(
    id       int primary key auto_increment,
    group_id int not null,
    role_id  int not null
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '组-角色关联表';

drop table if exists `announcement`;
create table if not exists `announcement`
(
    id           int primary key auto_increment,
    title        varchar(150) not null comment '公告标题',
    from_user_id int          not null comment '发送公告的人',
    to_user_id   int comment '接受公告的人 为空代表全部',
    content      text         not null comment '公告内容'
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '公告表';

drop table if exists `achievement`;
create table if not exists `achievement`
(
    id            int primary key auto_increment,
    title         varchar(150) not null comment '项目标题/获奖名称',
    number        varchar(150) not null comment '项目编号/获奖题目',
    first_user_id int          not null comment '第一作者/完成人id',
    department    varchar(50)  not null comment '所属单位',
    college       varchar(50)  not null comment '学校署名',
    subject       varchar(50) comment '一级学科',
    categories    varchar(50)  not null comment '学课门类',
    publish_type  varchar(50) comment '刊物/著作类型/获奖类别',
    publish_area  varchar(50) comment '出版地(著作)/发证机关',
    publish_time  datetime comment '发布/出版时间/获奖日期',
    publish_scope varchar(50) comment '发布/出版范围/单位/获奖级别',
    word_count    int          not null comment '字数/项目经费/万 /获奖人数',
    translation   tinyint(1)   not null comment '是否译文',
    result        tinyint(1)   not null comment '结题评价/是否合格',
    language      varchar(50) comment '语种(著作)',
    cn_issn       varchar(100) not null comment 'CN或ISSN号',
    isbn          varchar(100) comment 'ISBN号(著作)',
    status        smallint     not null default 0 comment '审核状态 0待审核 -1拒绝 1通过',
    review_time   datetime     null comment '审核日期',
    comment       text         null comment '审核意见'
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '成果表';

drop table if exists `author`;
create table if not exists `author`
(
    id             int primary key auto_increment,
    achievement_id int         not null comment '成果标题',
    user_id        int         not null comment '作者名称',
    gender         smallint    not null comment '作者名称',
    department     varchar(50) not null comment '所属单位',
    contribution   double      not null comment '贡献率'
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '成果-作者表';

drop table if exists `attachment`;
create table if not exists `attachment`
(
    id             int primary key auto_increment,
    achievement_id int not null comment '成果标题',
    url            int not null comment '附件地址'
)DEFAULT CHARSET = utf8mb4
   COLLATE = utf8mb4_bin  comment '成果-附件表';

