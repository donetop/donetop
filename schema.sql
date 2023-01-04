show databases;

use `donetop`;

-- drop database if exists `donetop`;

show tables;

select * from `tbUser`;
select * from `tbDraft`;
select * from `tbFolder`;
select * from `tbFile`;

-- drop table if exists `tbDraft`;
-- drop table if exists `tbFolder`;
-- drop table if exists `tbFile`;

show full columns from `tbUser`;
show full columns from `tbDraft`;
show full columns from `tbFolder`;
show full columns from `tbFile`;

show indexes from `tbUser`;
show indexes from `tbDraft`;
show indexes from `tbFolder`;
show indexes from `tbFile`;