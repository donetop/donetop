-- 2023-11-26 이전 ----------------------------------------------------------------------------------
-- alter table `tbFolder` change column `folderType` `domainType` varchar(10) default '' not null;
-- alter table `tbFolder` change column `domainType` `domainType` varchar(20) default '' not null;

-- alter table `tbFile` add column `size` bigint(20) default 0 not null;
-- alter table `tbFile` modify column `mimeType` varchar(64) not null default '';
-- alter table `tbFile` modify column `name` varchar(256) not null default '';

-- alter table `tbDraft` drop foreign key `fk_draft_folder_id`;
-- drop index `uc_folderId` on `tbDraft`;
-- alter table `tbDraft` drop column `folderId`;
-- alter table `tbDraft` modify column `password` varchar(512) not null default '';

-- alter table `tbComment` rename `tbDraftComment`;
-- alter table `tbDraftComment` rename index `fk_comment_draft_id` to `fk_draftComment_draft_id`;
-- alter table `tbDraftComment` rename index `fk_comment_folder_id` to `fk_draftComment_folder_id`;
-- -------------------------------------------------------------------------------------------------

-- 2023-12-26 --------------------------------------------------------------------------------------
-- alter table `tbDraft` add column `estimateContent` varchar(3000) not null default '';
-- -------------------------------------------------------------------------------------------------

-- 2024-01-20 --------------------------------------------------------------------------------------
-- alter table `tbFile` modify column `mimeType` varchar(100) not null default '';
-- alter table `tbFile` modify column `name` varchar(200) not null default '';
-- -------------------------------------------------------------------------------------------------

-- 2024-02-03 --------------------------------------------------------------------------------------
-- alter table `tbFile` add column `sequence` int(11) default 0 not null after `size`;
-- -------------------------------------------------------------------------------------------------


