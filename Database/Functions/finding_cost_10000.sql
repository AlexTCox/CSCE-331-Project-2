-- Description: This function calculates and updates the total cost of up to 10,000 customer orders that have not yet had their cost calculated.

-- Returns: Void

CREATE FUNCTION finding_cost_10000()
RETURNS void AS $$
BEGIN
    -- Update the 'cost' column of up to 10,000 rows in the 'customer_order' table where the cost is currently NULL.
    UPDATE customer_order SET cost = (
        -- Calculate the total cost by summing the prices of menu items, drinks, and add-ons associated with each order.
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
    -- Limit the update to rows where the cost is currently NULL and limit to updating 10,000 rows at a time.
    WHERE customer_order.id IN (SELECT id FROM customer_order WHERE cost IS NULL LIMIT 10000);
END;
$$ LANGUAGE plpgsql;
