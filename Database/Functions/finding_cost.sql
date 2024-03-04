-- Description: This function calculates and updates the total cost of a customer order based on the prices of menu items, drinks, and add-ons associated with the order.

-- Parameters:
-- @order_id: The ID of the customer order for which to calculate and update the cost.

-- Returns: Void

CREATE FUNCTION finding_cost(order_id integer)
RETURNS void AS $$
BEGIN
    -- Update the 'cost' column of the 'customer_order' table for the specified order ID with the calculated total cost.
    UPDATE customer_order SET cost = (
        -- Calculate the total cost by summing the prices of menu items, drinks, and add-ons associated with the order.
        SELECT SUM(cost)
        FROM (
            SELECT SUM(price) AS cost
            FROM menu_order 
            JOIN menu_item ON menu_item_id = menu_item.id 
            WHERE customer_order_id = customer_order.id
            UNION 
            SELECT SUM(add_on_price)
            FROM add_on_order 
            JOIN ingredients ON ingredient_id = ingredients.id 
            WHERE customer_order_id = customer_order.id
            UNION 
            SELECT SUM(price)
            FROM drink_order
            JOIN drinks ON drink_id = drinks.id
            WHERE customer_order_id = customer_order.id
        ) AS data
    ) 
    -- Limit the update to the specific customer order ID provided.
    WHERE customer_order.id IN (SELECT id FROM customer_order WHERE id = order_id);
END;
$$ LANGUAGE plpgsql;
