CREATE DATABASE IF NOT EXISTS subsentry_db;
USE subsentry_db;
CREATE TABLE users (
user_id int auto_increment primary key,
email varchar(255) not null unique,
password_hash varchar(255) not null,
created_at timestamp default current_timestamp
);
CREATE TABLE subscriptions (
subscription_id int auto_increment primary key,
user_id int not null,
service_name varchar(100) not null,
cost decimal(10, 2) not null,
currency varchar(3) default 'USD',
billing_cycle enum('monthly', 'yearly') not null,
next_billing_date date not null,
foreign key (user_id) references users(user_id) on delete cascade
);
CREATE TABLE alerts (
alert_id int auto_increment primary key,
subscription_id int not null,
alert_date date not null,
is_sent BOOLEAN default false,
foreign key (subscription_id) references subscriptions(subscription_id) on delete cascade
);
insert into users (email, password_hash)
values ('alex@university.edu', 'hashed_secure_password_123');
insert into subscriptions (user_id, service_name, cost, currency, billing_cycle, next_billing_date)
values 
(1, 'Netflix Premium', 15.99, 'USD', 'monthly', '2026-06-15'),
(1, 'Spotify Student', 5.99, 'USD', 'monthly', '2026-06-20'),
(1, 'Amazon Prime Student', 69.00, 'USD', 'monthly', '2027-01-05');
insert into alerts (subscription_id, alert_date, is_sent)
values
(1, '2026-06-12', false),
(2, '2026-06-17', false);
select 
user_id,
sum(
case 
when billing_cycle = 'monthly' then cost
when billing_cycle = 'yearly' then round(cost / 12, 2)
else 0
end
) as total_monthly_spend
from subscriptions
where user_id = 1
group by user_id; 
select users.email, subscriptions.service_name, subscriptions.cost, subscriptions.billing_cycle
from subscriptions
inner join users on subscriptions.user_id = users.user_id;
SET SQL_SAFE_UPDATES = 0;
UPDATE subscriptions SET currency = 'USD' WHERE currency IS NULL;
SET SQL_SAFE_UPDATES = 1;
SET SQL_SAFE_UPDATES = 0;
ALTER TABLE subscriptions
ADD COLUMN category ENUM('Entertainment', 'Software', 'Gym', 'Food', 'Utilities') NOT NULL DEFAULT 'Entertainment';
UPDATE subscription SET category = 'Entertainment' WHERE service_name LIKE '%Netflix%' OR service_name LIKE '%Spotify%' OR service_name LIKE '%Disney%' OR service_name LIKE '%Now%'; 
UPDATE subscription SET category = 'Utilities' WHERE service_name LIKE '%Prime%';
SET SQL_SAFE_UPDATES = 1;
SET SQL_SAFE_UPDATES = 0;
UPDATE subscriptions SET category = 'Entertainment' WHERE category IS NULL OR category = '';
UPDATE subscriptions SET category = 'Utilities' WHERE service_name LIKE '%Prime%' OR service_name LIKE '%Mobile%';
SET SQL_SAFE_UPDATES = 1;
