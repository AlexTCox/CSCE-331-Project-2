CREATE FUNCTION new_order(operator_id integer) 
RETURNS integer AS $order_id$
declare
	order_id integer;
BEGIN
    INSERT INTO customer_order (date_of_sale, operator_id) VALUES (NOW()::TIMESTAMP, operator_id) returning id INTO order_id;
	RETURN order_id;
END;
$order_id$ LANGUAGE plpgsql;