-- What to update:
UPDATE customer_order set cost = (
    -- Saves it as a variable cost:
    SELECT SUM (cost)
    -- How its getting each cost:
    FROM (
        -- Selects the price from the menu item table and then sets an alias cost:
        SELECT SUM (price) as cost
        -- Where it looks for the order id:
        FROM menu_order 
        -- Joins the menu item table so we can match it with the menu_order table to compare ids:
        JOIN menu_item 
        -- Referencing the tables to each other:
        ON menu_item_id = menu_item.id 
        -- Does the same thing but with customer order id:
        WHERE customer_order_id = customer_order.id
        -- Essentially connecting multiple calls to different things:
        UNION 
        -- Selects the price from the ingredients table :
        SELECT SUM (add_on_price)
        FROM add_on_order 
        JOIN ingredients
        ON ingredient_id = ingredients.id 
        WHERE customer_order_id = customer_order.id
        UNION 
        -- Selects the price from the drinks table:
        SELECT SUM (price)
        FROM drink_order
        JOIN drinks
        ON drink_id= drinks.id
        WHERE customer_order_id = customer_order.id
        -- Need to alias because the use of FROM
    ) data 
    
    -- How it decides which values to change: 
) WHERE customer_order.id in (SELECT id FROM customer_order WHERE cost IS NULL LIMIT 10000);

-- Added indexs to speed it up:
-- CREATE INDEX menu_order_order_id_idx ON menu_order (customer_order_id);
-- CREATE INDEX menu_order_menu_order_id_idx ON menu_order (menu_item_id);
-- CREATE INDEX drink_order_order_id_idx ON drink_order (customer_order_id);
-- CREATE INDEX drink_order_drink_id_idx ON drink_order (drink_id);
-- CREATE INDEX drink_order_drink_id_idx ON drink_order (drink_id);
-- CREATE INDEX add_on_order_menu_order_id_idx ON add_on_order (customer_order_id);

