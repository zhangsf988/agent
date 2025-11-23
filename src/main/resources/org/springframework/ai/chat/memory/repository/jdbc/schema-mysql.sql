create table agent.SPRING_AI_CHAT_MEMORY
(
    id              int         not null
        primary key,
    conversation_id varchar(36) not null,
    content         text        not null,
    type            varchar(10) not null,
    timestamp       timestamp   not null,
    function_type   varchar(30) not null comment '功能'
);

create index SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX
    on agent.SPRING_AI_CHAT_MEMORY (conversation_id, timestamp);

