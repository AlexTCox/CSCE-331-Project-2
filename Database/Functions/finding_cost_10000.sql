CREATE FUNCTION finding_cost_10000()
RETURNS void AS $$
BEGIN

UPDATE customer_order set cost = (
    SELECT SUM (cost)
    FROM (
        SELECT SUM (price) as cost
        FROM menu_order 
        JOIN menu_item 
        ON menu_item_id = menu_item.id 
        WHERE customer_order_id = customer_order.id
        UNION 
        SELECT SUM (add_on_price)
        FROM add_on_order 
        JOIN ingredients
        ON ingredient_id = ingredients.id 
        WHERE customer_order_id = customer_order.id
        UNION 
        SELECT SUM (price)
        FROM drink_order
        JOIN drinks
        ON drink_id= drinks.id
        WHERE customer_order_id = customer_order.id
    ) data 
) WHERE customer_order.id in (SELECT id FROM customer_order WHERE cost IS NULL LIMIT 10000 );
END;
$$ LANGUAGE plpgsql;

