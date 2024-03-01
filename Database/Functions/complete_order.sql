CREATE FUNCTION complete_order(order_id int, menuItems text[], drinkItems text[], addOns text[]) 
RETURNS void AS $$
BEGIN
    FOREACH menuItem IN ARRAY menuItemsId LOOP
        PERFORM new_menu_item(menuItem, order_id)
    end loop;
    FOREACH drinkItem IN ARRAY drinkItems LOOP
        PERFORM new_drink(drinkItem, order_id)
    end loop;
    FOREACH addOn IN ARRAY addOns LOOP
        PERFORM new_add_on(addOn, order_id)
    end loop;
    PERFORM dec_stock_order(order_id)
END;
$$ LANGUAGE plpgsql;