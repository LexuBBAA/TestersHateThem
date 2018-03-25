drop function login;
delimiter //
create function login(p_email varchar(60), p_password varchar(255)) returns int
BEGIN
DECLARE v_ret int;
SELECT uid 
FROM users 
WHERE 
	email = p_email AND 
    password = p_password
INTO v_ret;
IF v_ret IS NULL THEN
	SELECT 0 INTO v_ret;
END IF;
RETURN v_ret;
END;
//
delimiter ;

drop function register;
delimiter //
create function register(p_email varchar(60), 
						p_password varchar(255), 
                        p_name varchar(255), 
                        p_phone varchar(45),
                        p_user_type enum('consumer', 'provider'), 
                        p_description text,
                        p_ciid int,
                        p_coid int,
                        p_address text) RETURNS int
BEGIN
declare v_ret int;
INSERT INTO users (email, password, name, phone, user_type, description) VALUES (p_email, p_password, p_name, p_phone, p_user_type, p_description);
SELECT LAST_INSERT_ID() into v_ret;
IF v_ret IS NULL THEN
	SELECT 0 INTO v_ret;
ELSE
	INSERT INTO addresses(uid, city_id, country_id, address) VALUES (v_ret, p_ciid, p_coid, p_address);
END IF;
RETURN v_ret;
END;
//
delimiter ;


delimiter //
create function add_address(p_uid int, p_city_id int, p_country_id int, p_address text) returns int
BEGIN
declare v_ret int;
INSERT INTO addresses (uid, city_id, country_id, address) VALUES (p_uid, p_city_id, p_country_id, p_address);
SELECT LAST_INSERT_ID() into v_ret;
IF v_ret IS NULL THEN
	SELECT 0 INTO v_ret;
END IF;
RETURN v_ret;
END
//
DELIMITER ;

drop function update_address;
delimiter //
create function update_address(p_aid int, p_city_id int, p_country_id int, p_address text) returns int
BEGIN
	declare v_ret int;
	UPDATE addresses SET city_id = p_city_id, country_id = p_country_id, address = p_address WHERE aid = p_aid;
    SELECT p_aid INTO v_ret;
    RETURN v_ret;
END
//
DELIMITER ;


drop function add_transaction;
DELIMITER //
create function add_transaction(p_cid int, p_pid int, p_q int, p_ddate date) RETURNS int
BEGIN
	declare v_ret int;
	INSERT INTO transactions (cid, pid, state, quantity, start_date, due_date) VALUES (p_cid, p_pid, -1, p_q, date(now()), p_ddate);
    SELECT LAST_INSERT_ID() into v_ret;
    IF v_ret IS NULL THEN
		SELECT 0 INTO v_ret;
	END IF;
    RETURN v_ret;
END
//
DELIMITER ;

drop function approve_transaction;
DELIMITER //
CREATE function approve_transaction(p_uid int, p_tid int) RETURNS int
BEGIN
	DECLARE v_ret int;
    DECLARE v_verif int;
    SELECT tid INTO v_verif FROM transactions WHERE tid = p_tid AND cid = p_uid AND state = -1;
    IF v_verif IS NULL THEN
		RETURN -1;
	END IF;
	UPDATE transactions set state = 0 WHERE tid = p_tid AND cid = p_uid AND state = -1;
    SELECT p_tid INTO v_ret;
    RETURN v_ret;
END
//
DELIMITER ;

drop function finish_transaction;
DELIMITER //
CREATE function finish_transaction(p_uid int, p_tid int) RETURNS int
BEGIN
	DECLARE v_ret int;
    DECLARE v_verif int;
    SELECT tid INTO v_verif FROM transactions WHERE tid = p_tid AND cid = p_uid AND state = 0;
    IF v_verif IS NULL THEN 
		RETURN -1;
	END IF;
    UPDATE transactions SET state = 1, finished_date = date(now()) WHERE tid = p_tid AND cid = p_uid AND state = 0;
    SELECT p_tid INTO v_ret;
	RETURN v_ret;
END
//
DELIMITER ;

drop procedure listing;
DELIMITER //
CREATE PROCEDURE `listing`(p_uid int, p_pag int)
BEGIN
	declare v_user_type ENUM('provider', 'consumer');
	SELECT user_type INTO v_user_type FROM users WHERE uid = p_uid;
	IF v_user_type = 'consumer' then
		SELECT u.uid, u.name, a.address, ci.name city
		FROM users u
		JOIN addresses a ON (a.uid = u.uid)
        JOIN cities ci ON (a.city_id = ci.cid)
		WHERE
			a.city_id IN (SELECT city_id FROM addresses WHERE uid = p_uid) AND
			u.user_type = 'provider'
		ORDER BY u.score DESC, u.name ASC;
	ELSE
		SELECT u.uid, u.name, a.address, ci.name city
		FROM users u
		JOIN addresses a ON (a.uid = u.uid)
        JOIN cities ci ON (a.city_id = ci.cid)
		WHERE
			a.city_id IN (SELECT city_id FROM addresses WHERE uid = p_uid) AND
			u.user_type = 'consumer'
		ORDER BY u.score DESC, u.name ASC;
	END IF;
END
//
DELIMITER ;

call listing(8, 0);