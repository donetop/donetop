create database if not exists `donetop`;

use `donetop`;

-- drop table if exists `tbUser`;
create table if not exists `tbUser` (
  `id` bigint(20) not null auto_increment,
  `createTime` datetime not null,
  `email` varchar(32) default '' not null,
  `name` varchar(32) default '' not null,
  `password` varchar(512) default '' not null,
  `roleType` varchar(10) default '' not null,
  primary key (`id`),
  unique `uc_email` (`email`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbUser`;
--show indexes from `tbUser`;

-- drop table if exists `tbOSSUser`;
create table if not exists `tbOSSUser` (
  `id` bigint(20) not null auto_increment,
  `createTime` datetime not null,
  `name` varchar(32) default '' not null,
  `password` varchar(512) default '' not null,
  `roleType` varchar(10) default '' not null,
  primary key (`id`),
  unique `uc_name` (`name`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbOSSUser`;
--show indexes from `tbOSSUser`;

-- drop table if exists `tbPaymentInfo`;
create table if not exists `tbPaymentInfo` (
  `id` bigint(20) not null auto_increment,
  `lastTransactionNumber` varchar(128) default '' not null,
  `updateTime` datetime not null,
  primary key (`id`),
  unique `uc_lastTransactionNumber` (`lastTransactionNumber`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbPaymentInfo`;
--show indexes from `tbPaymentInfo`;

-- drop table if exists `tbPaymentHistory`;
create table if not exists `tbPaymentHistory` (
  `id` bigint(20) not null auto_increment,
  `createTime` datetime not null,
  `paymentStatus` varchar(10) default '' not null,
  `pgType` varchar(10) default '' not null,
  `rawData` varchar(1024) default '' not null,
  `paymentInfoId` bigint(20) not null,
  primary key (`id`),
  constraint `fk_paymentHistory_paymentInfo_id` foreign key (`paymentInfoId`) references `tbPaymentInfo` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbPaymentHistory`;
--show indexes from `tbPaymentHistory`;

-- drop table if exists `tbFolder`;
create table if not exists `tbFolder` (
  `id` bigint(20) not null auto_increment,
  `folderType` varchar(10) default '' not null,
  `path` varchar(512) default '' not null,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbFolder`;
--show indexes from `tbFolder`;

-- drop table if exists `tbDraft`;
create table if not exists `tbDraft` (
  `id` bigint(20) not null auto_increment,
  `address` varchar(256) default '' not null,
  `categoryName` varchar(64) default '' not null,
  `companyName` varchar(128) default '' not null,
  `createTime` datetime not null,
  `customerName` varchar(128) default '' not null,
  `detailAddress` varchar(256) default '' not null,
  `draftStatus` varchar(50) default '' not null,
  `email` varchar(128) default '' not null,
  `inChargeName` varchar(128) default '' not null,
  `memo` varchar(3000) default '' not null,
  `password` varchar(512) default '' not null,
  `paymentMethod` varchar(50) default '' not null,
  `phoneNumber` varchar(15) default '' not null,
  `price` bigint(20) default 0 not null,
  `updateTime` datetime not null,
  `folderId` bigint(20),
  `paymentInfoId` bigint(20),
  primary key (`id`),
  unique `uc_folderId` (`folderId`),
  unique `uc_paymentInfoId` (`paymentInfoId`),
  constraint `fk_draft_folder_id` foreign key (`folderId`) references `tbFolder` (`id`),
  constraint `fk_draft_paymentInfo_id` foreign key (`paymentInfoId`) references `tbPaymentInfo` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbDraft`;
--show indexes from `tbDraft`;

-- drop table if exists `tbDraftFolder`;
create table if not exists `tbDraftFolder` (
  `folderType` varchar(50) default '' not null,
  `folderId` bigint(20) not null,
  `draftId` bigint(20) not null,
  primary key (`folderId`),
  unique `uc_draftFolder` (`folderType`, `draftId`),
  constraint `fk_draftFolder_draft_id` foreign key (`draftId`) references `tbDraft` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbDraftFolder`;
--show indexes from `tbDraftFolder`;

-- drop table if exists `tbFile`;
create table if not exists `tbFile` (
  `id` bigint(20) not null auto_increment,
  `mimeType` varchar(128) default '' not null,
  `name` varchar(128) default '' not null,
  `folderId` bigint(20) not null,
  primary key (`id`),
  unique `uc_file` (`name`, `mimeType`, `folderId`),
  constraint `fk_file_folder_id` foreign key (`folderId`) references `tbFolder` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbFile`;
--show indexes from `tbFile`;

-- drop table if exists `tbCategory`;
create table if not exists `tbCategory` (
  `id` bigint(20) not null auto_increment,
  `name` varchar(128) default '' not null,
  `sequence` int(11) default 0 not null,
  `folderId` bigint(20),
  `parentId` bigint(20),
  primary key (`id`),
  unique `uc_name` (`name`),
  constraint `fk_category_category_parentId` foreign key (`parentId`) references `tbCategory` (`id`),
  constraint `fk_category_folder_id` foreign key (`folderId`) references `tbFolder` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbCategory`;
--show indexes from `tbCategory`;

-- drop table if exists `tbComment`;
create table if not exists `tbComment` (
  `id` bigint(20) not null auto_increment,
  `content` varchar(1024) default '' not null,
  `createTime` datetime not null,
  `draftId` bigint(20),
  `folderId` bigint(20),
  primary key (`id`),
  constraint `fk_comment_draft_id` foreign key (`draftId`) references `tbDraft` (`id`),
  constraint `fk_comment_folder_id` foreign key (`folderId`) references `tbFolder` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbDraftComment`;
--show indexes from `tbDraftComment`;

-- drop table if exists `tbCustomerPost`;
create table `tbCustomerPost` (
  `id` bigint(20) not null auto_increment,
  `content` varchar(3000) default '' not null,
  `createTime` datetime not null,
  `customerName` varchar(128) default '' not null,
  `title` varchar(256) default '' not null,
  primary key (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbCustomerPost`;
--show indexes from `tbCustomerPost`;

-- drop table if exists `tbCustomerPostComment`;
create table `tbCustomerPostComment` (
  `id` bigint(20) not null auto_increment,
  `content` varchar(1024) default '' not null,
  `createTime` datetime not null,
  `customerPostId` bigint(20),
  primary key (`id`),
  constraint `fk_customerPostComment_customerPost_id` foreign key (`customerPostId`) references `tbCustomerPost` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbCustomerPostComment`;
--show indexes from `tbCustomerPostComment`;

-- drop table if exists `tbCustomerPostViewHistory`;
create table `tbCustomerPostViewHistory` (
  `id` bigint(20) not null auto_increment,
  `viewerIp` varchar(20) default '' not null,
  `createTime` datetime not null,
  `customerPostId` bigint(20),
  primary key (`id`),
  unique `uc_viewerIp` (`viewerIp`, `customerPostId`),
  constraint `fk_customerPostViewHistory_customerPost_id` foreign key (`customerPostId`) references `tbCustomerPost` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbCustomerPostViewHistory`;
--show indexes from `tbCustomerPostViewHistory`;

-- drop table if exists `tbNotice`;
create table `tbNotice` (
  `id` bigint(20) not null auto_increment,
  `title` varchar(200) default '' not null,
  `createTime` datetime not null,
  `folderId` bigint(20),
  primary key (`id`),
  constraint `fk_notice_folder_id` foreign key (`folderId`) references `tbFolder` (`id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;
--show full columns from `tbNotice`;
--show indexes from `tbNotice`;

-- -----------------------------------------------------
-- -----------------------------------------------------
-- -----------------------------------------------------

-- 2023-11-26 이전 ----------------------------------------------------------------------------------
alter table `tbFolder` change column `folderType` `domainType` varchar(10) default '' not null;
alter table `tbFolder` change column `domainType` `domainType` varchar(20) default '' not null;

alter table `tbFile` add column `size` bigint(20) default 0 not null;
alter table `tbFile` modify column `mimeType` varchar(64) not null default '';
alter table `tbFile` modify column `name` varchar(256) not null default '';

alter table `tbDraft` drop foreign key `fk_draft_folder_id`;
drop index `uc_folderId` on `tbDraft`;
alter table `tbDraft` drop column `folderId`;
alter table `tbDraft` modify column `password` varchar(512) not null default '';

alter table `tbComment` rename `tbDraftComment`;
alter table `tbDraftComment` rename index `fk_comment_draft_id` to `fk_draftComment_draft_id`;
alter table `tbDraftComment` rename index `fk_comment_folder_id` to `fk_draftComment_folder_id`;
-- -------------------------------------------------------------------------------------------------

-- 2023-12-26 --------------------------------------------------------------------------------------
alter table `tbDraft` add column `estimateContent` varchar(3000) not null default '';
-- -------------------------------------------------------------------------------------------------

-- 2024-01-20 --------------------------------------------------------------------------------------
alter table `tbFile` modify column `mimeType` varchar(100) not null default '';
alter table `tbFile` modify column `name` varchar(200) not null default '';
-- -------------------------------------------------------------------------------------------------

-- 2024-02-03 --------------------------------------------------------------------------------------
alter table `tbFile` add column `sequence` int(11) default 0 not null after `size`;
-- -------------------------------------------------------------------------------------------------

-- 2024-03-01 --------------------------------------------------------------------------------------
alter table `tbDraftComment` add column `checked` tinyint(1) default 0 not null after `content`;
-- -------------------------------------------------------------------------------------------------

-- 2024-04-07 --------------------------------------------------------------------------------------
alter table `tbCategory` add column `exposed` tinyint(1) default 0 not null after `sequence`;
update `tbCategory` set `exposed` = 1;
-- -------------------------------------------------------------------------------------------------