-- Description: This function creates a new customer order with the specified operator ID and returns the ID of the newly created order.

-- Parameters:
-- @operator_id: The ID of the operator associated with the new order.

-- Returns: The ID of the newly created order.

CREATE FUNCTION new_order(operator_id integer) 
RETURNS integer AS $order_id$
DECLARE
    order_id integer;
BEGIN
    -- Insert a new record into the 'customer_order' table with the current timestamp and the provided operator ID.
    -- Return the ID of the newly created order into the 'order_id' variable.
    INSERT INTO customer_order (date_of_sale, operator_id) VALUES (NOW()::TIMESTAMP, operator_id) RETURNING id INTO order_id;
    
    -- Return the ID of the newly created order.
    RETURN order_id;
END;
$order_id$ LANGUAGE plpgsql;
