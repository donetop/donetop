show databases;

use `donetop`;

-- drop database if exists `donetop`;

show tables;

select * from `tbUser`;
select * from `tbDraft`;
select * from `tbFolder`;
select * from `tbFile`;
select * from `tbPaymentInfo`;
select * from `tbPaymentHistory`;
select * from `tbCategory`;

-- drop table if exists `tbUser`;
-- drop table if exists `tbDraft`;
-- drop table if exists `tbFolder`;
-- drop table if exists `tbFile`;
-- drop table if exists `tbPaymentInfo`;
-- drop table if exists `tbPaymentHistory`;
-- drop table if exists `tbCategory`;

-- alter table `tbDraft` modify column `password` varchar(512) not null default '';

show full columns from `tbUser`;
show full columns from `tbDraft`;
show full columns from `tbFolder`;
show full columns from `tbFile`;
show full columns from `tbPaymentInfo`;
show full columns from `tbPaymentHistory`;
show full columns from `tbCategory`;

show indexes from `tbUser`;
show indexes from `tbDraft`;
show indexes from `tbFolder`;
show indexes from `tbFile`;
show indexes from `tbPaymentInfo`;
show indexes from `tbPaymentHistory`;
show indexes from `tbCategory`;