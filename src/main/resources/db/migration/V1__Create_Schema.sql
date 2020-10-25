
create table item (
		id bigint not null,
		left_limit integer not null,
		level integer not null,
		right_limit integer not null,
		parent_id bigint, primary key (id))
