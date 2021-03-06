drop table device if exists;
drop table folk if exists;

create table folk (
  id bigint not null,
  surname VARCHAR(255),
  name VARCHAR(100),
  patronymic VARCHAR(100),
  preferred_name VARCHAR(100),
  gender INTEGER DEFAULT 0 ,
  birth_date DATE,
  birth_place VARCHAR(50),
  first_conf_date DATE,
  company VARCHAR(255),
  position VARCHAR(255),
  job_start_date DATE,
  author VARCHAR(20),
  city VARCHAR(20),
  speaker INTEGER DEFAULT 0 ,
  organizator INTEGER DEFAULT 0 ,
  vip_flag INTEGER DEFAULT 0 ,
  visit_count INTEGER DEFAULT 0 ,
  average_rating NUMERIC(19,2) DEFAULT 0,
  language VARCHAR(10),
  actuality_date DATE,
  primary key (id)
);

create table device (
  id bigint not null,
  type varchar(10),
  os varchar(10),
  model varchar(255),
  serial_number varchar(255),
  author varchar(20),
  folk bigint,
  actuality_date DATE,
  primary key (id)
);

alter table device add constraint fk_device2folk foreign key (folk) references folk;
