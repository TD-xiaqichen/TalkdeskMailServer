CREATE TABLE james_mail (
	mailbox_id int8 NOT NULL,
	mail_uid int8 NOT NULL,
	mail_is_answered bool NOT NULL,
	mail_body_start_octet int4 NOT NULL,
	mail_content_octets_count int8 NOT NULL,
	mail_is_deleted bool NOT NULL,
	mail_is_draft bool NOT NULL,
	mail_is_flagged bool NOT NULL,
	mail_date timestamp NULL,
	mail_mime_type varchar(200) NULL,
	mail_modseq int8 NULL,
	mail_is_recent bool NOT NULL,
	mail_is_seen bool NOT NULL,
	mail_mime_subtype varchar(200) NULL,
	mail_textual_line_count int8 NULL,
	mail_bytes bytea NOT NULL,
	header_bytes bytea NOT NULL,
	mirror text not null,
    PRIMARY KEY (mailbox_id, mail_uid)
);

CREATE TABLE public.james_mailbox (
	mailbox_id int8 NOT NULL,
	mailbox_highest_modseq int8 NULL,
	mailbox_last_uid int8 NULL,
	mailbox_name varchar(200) NOT NULL,
	mailbox_namespace varchar(200) NOT NULL,
	mailbox_uid_validity int8 NOT NULL,
	user_name varchar(200) NULL,
    PRIMARY KEY (mailbox_id)
);

CREATE TABLE public.james_user (
	user_name varchar(100) NOT NULL,
	password_hash_algorithm varchar(100) NOT NULL,
	"password" varchar(128) NOT NULL,
	"version" int4 NULL,
	PRIMARY KEY (user_name)
);