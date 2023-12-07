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



