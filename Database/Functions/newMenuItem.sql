CREATE FUNCTION new_menu_item(single_menu_item text, order_id integer) 
RETURNS void AS $$
DECLARE
    menu_item_store integer;
BEGIN
    SELECT id FROM menu_item WHERE menu_item.name = single_menu_item INTO menu_item_store;
    INSERT INTO  menu_order(menu_item_id, customer_order_id) VALUES (menu_item_store, order_id);
    PERFORM finding_cost(order_id);
END;
$$ LANGUAGE plpgsql;