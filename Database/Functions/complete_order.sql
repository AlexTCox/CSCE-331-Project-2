CREATE FUNCTION complete_order(order_id int, menuItems text[], drinkItems text[], addOns text[]) 
RETURNS void AS $$
DECLARE
	addOn text;
	menuItem text;
	drinkItem text;
BEGIN
    FOREACH menuItem IN ARRAY menuItemsId LOOP
<<<<<<< HEAD
        PERFORM new_menu_item(menuItem, order_id)
    end loop;
    FOREACH drinkItem IN ARRAY drinkItems LOOP
        PERFORM new_drink(drinkItem, order_id)
    end loop;
    FOREACH addOn IN ARRAY addOns LOOP
        PERFORM new_add_on(addOn, order_id)
    end loop;
    PERFORM dec_stock_order(order_id)
=======
        PERFORM new_menu_item(menuItem, order_id);
    end loop;
    FOREACH drinkItem IN ARRAY drinkItems LOOP
        PERFORM new_drink(drinkItem, order_id);
    end loop;
    FOREACH addOn IN ARRAY addOns LOOP
        PERFORM new_add_on(addOn, order_id);
    end loop;
    PERFORM dec_stock_order(order_id);
>>>>>>> 085fffb6e2d76fa5f1ae1463ff66c54711bb9b7a
END;
$$ LANGUAGE plpgsql;