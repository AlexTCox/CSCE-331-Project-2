-- Description: This function adds a new menu item to a specified customer order.

-- Parameters:
-- @single_menu_item: The name of the menu item to be added.
-- @order_id: The ID of the customer order to which the menu item will be added.

-- Returns: Void

CREATE FUNCTION new_menu_item(single_menu_item text, order_id integer) 
RETURNS void AS $$
DECLARE
    menu_item_store integer;
BEGIN
    -- Retrieve the ID of the menu item from the 'menu_item' table based on its name.
    SELECT id FROM menu_item WHERE name = single_menu_item INTO menu_item_store;
    
    -- Insert a new record into the 'menu_order' table with the retrieved menu item ID and the specified order ID.
    INSERT INTO menu_order(menu_item_id, customer_order_id) VALUES (menu_item_store, order_id);
    
    -- Recalculate the total cost of the order after adding the new menu item.
    PERFORM finding_cost(order_id);
END;
$$ LANGUAGE plpgsql;
