-- Description: This function completes an order by adding menu items, drinks, and add-ons to the order and decreasing the stock accordingly.

-- Parameters:
-- @order_id: The ID of the order being completed.
-- @menuItems: An array of text representing the menu items to be added to the order.
-- @drinkItems: An array of text representing the drinks to be added to the order.
-- @addOns: An array of text representing the add-ons to be added to the order.

-- Returns: Void

CREATE FUNCTION complete_order(order_id int, menuItems text[], drinkItems text[], addOns text[]) 
RETURNS void AS $$
DECLARE
    addOn text;
    menuItem text;
    drinkItem text;
BEGIN
    -- Loop through each menu item in the 'menuItems' array and perform the 'new_menu_item' function for each.
    FOREACH menuItem IN ARRAY menuItems LOOP
        PERFORM new_menu_item(menuItem, order_id);
    END LOOP;
    
    -- Loop through each drink item in the 'drinkItems' array and perform the 'new_drink' function for each.
    FOREACH drinkItem IN ARRAY drinkItems LOOP
        PERFORM new_drink(drinkItem, order_id);
    END LOOP;
    
    -- Loop through each add-on in the 'addOns' array and perform the 'new_add_on' function for each.
    FOREACH addOn IN ARRAY addOns LOOP
        PERFORM new_add_on(addOn, order_id);
    END LOOP;
    
    -- Decrease the stock of items associated with the completed order by performing the 'dec_stock_order' function.
    PERFORM dec_stock_order(order_id);
END;
$$ LANGUAGE plpgsql;
