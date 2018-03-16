https://sqliteonline.com/#fiddle-5aabb1c06ac2be6jetw5mth

create table categories_table (
  id integer primary key autoincrement,
  name text not null,
  icon text not null,
  color text not null
);

create table priorities_table (
  id integer primary key autoincrement,
  name text not null,
  mark text not null,
  color text not null
);

create table tasks_table (
	id integer primary key autoincrement,
	name 		text not null,
	details 	text,
	startTime 	text not null,
	endTime 	text,
	status 		integer not null,
	category_id integer not null,
	priority_id integer not null,
	FOREIGN KEY (category_id) REFERENCES categories_table (id)
	FOREIGN KEY (priority_id) REFERENCES priorities_table (id)
);

INSERT INTO priorities_table (name, mark, color) VALUES ("Low", "!", "#FFD600");
INSERT INTO priorities_table (name, mark, color) VALUES ("Normal", "!!", "#FFD600");
INSERT INTO priorities_table (name, mark, color) VALUES ("High", "!!!", "#FF3500");

INSERT INTO categories_table (name, icon, color) VALUES ("Default", "default_task_category", "#FFB873");
INSERT INTO categories_table (name, icon, color) VALUES ("Favourite", "favourite_task_category", "#F2FD3F");
INSERT INTO categories_table (name, icon, color) VALUES ("Home", "home_task_category", "#A9F16C");
INSERT INTO categories_table (name, icon, color) VALUES ("Shopping", "shopping_task_category", "#7373D9");


INSERT INTO tasks_table (name, details, startTime, endTime, status, category_id, priority_id) 
VALUES ("Task1", "", "timeStart", "timeEnd", 0, 1, 1);
INSERT INTO tasks_table (name, details, startTime, endTime, status, category_id, priority_id) 
VALUES ("Task12", "", "timeStart", "timeEnd", 0, 2, 1);
INSERT INTO tasks_table (name, details, startTime, endTime, status, category_id, priority_id) 
VALUES ("Task13", "", "timeStart", "timeEnd", 0, 3, 1);
INSERT INTO tasks_table (name, details, startTime, endTime, status, category_id, priority_id) 
VALUES ("Task2", "", "timeStart", "timeEnd", 0, 2, 2);
INSERT INTO tasks_table (name, details, startTime, endTime, status, category_id, priority_id) 
VALUES ("Task3", "", "timeStart", "timeEnd", 0, 1, 3);

SELECT 
	priorities_table.id AS priority_id,
    priorities_table.name AS priority_name,
    categories_table.id AS category_id,
    tasks_table.id as task_id,
    COUNT(priorities_table.id) AS count_,
    COUNT(tasks_table.id) AS count_tasks
FROM priorities_table
LEFT JOIN tasks_table ON priorities_table.id = tasks_table.priority_id
LEFT JOIN categories_table ON tasks_table.category_id = categories_table.id
GROUP BY (priority_name);

