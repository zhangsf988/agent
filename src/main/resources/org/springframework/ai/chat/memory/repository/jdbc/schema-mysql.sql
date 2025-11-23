create table agent.spring_ai_chat_memory
(
    id              varchar(100)       not null
        primary key,
    conversation_id varchar(36) not null,
    content         text        not null,
    type            varchar(10) not null,
    timestamp       timestamp   not null,
    function_type   varchar(30) not null comment '功能'
);

create index SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX
    on agent.spring_ai_chat_memory (conversation_id, timestamp);

