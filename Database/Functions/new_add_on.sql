-- Description: This function adds a new add-on to a specified customer order.

-- Parameters:
-- @add_on: The name of the add-on to be added.
-- @order_id: The ID of the customer order to which the add-on will be added.

-- Returns: Void

CREATE FUNCTION new_add_on(add_on text, order_id integer) 
RETURNS void AS $$
DECLARE
    add_on_store integer;
BEGIN
    -- Retrieve the ID of the add-on from the 'ingredients' table based on its name.
    SELECT id FROM ingredients WHERE name = add_on INTO add_on_store;
    
    -- Insert a new record into the 'add_on_order' table with the retrieved add-on ID and the specified order ID.
    INSERT INTO add_on_order(ingredient_id, customer_order_id) VALUES (add_on_store, order_id);
    
    -- Recalculate the total cost of the order after adding the new add-on.
    PERFORM finding_cost(order_id);
END;
$$ LANGUAGE plpgsql;
