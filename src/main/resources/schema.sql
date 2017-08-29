drop table device if exists;
drop table folk if exists;

create table folk (
  id bigint not null,
  birth_date date,
  company varchar(255),
  name varchar(255),
  position varchar(255),
  primary key (id)
);

create table device (
  id bigint not null,
  model varchar(255),
  serial_number varchar(255),
  folk bigint,
  primary key (id)
);

alter table device add constraint fk_device2folk foreign key (folk) references folk;
