-- Description: This function adds a new drink to a specified customer order.

-- Parameters:
-- @drink_size: The size of the drink to be added.
-- @order_id: The ID of the customer order to which the drink will be added.

-- Returns: Void

CREATE FUNCTION new_drink(drink_size text, order_id integer) 
RETURNS void AS $$
DECLARE
    drink_size_store integer;
BEGIN
    -- Retrieve the ID of the drink from the 'drinks' table based on its size.
    SELECT id FROM drinks WHERE size = drink_size INTO drink_size_store;
    
    -- Insert a new record into the 'drink_order' table with the retrieved drink ID and the specified order ID.
    INSERT INTO drink_order(drink_id, customer_order_id) VALUES (drink_size_store, order_id);
    
    -- Recalculate the total cost of the order after adding the new drink.
    PERFORM finding_cost(order_id);
END;
$$ LANGUAGE plpgsql;
