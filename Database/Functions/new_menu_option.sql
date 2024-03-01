CREATE FUNCTION new_menu_option(new_menu_name text, new_menu_cost float, ingredients_list text[])
RETURNS boolean AS $$
DECLARE
    check_for_menu_item int := 0;
BEGIN 
    SELECT id FROM menu_item WHERE name = new_menu_name INTO check_for_menu_item;
    if NOT check_for_menu_item = 0 then
        RETURN false;
    else 
        INSERT INTO menu_item(name,price) VALUES (new_menu_name,new_menu_cost);
        PERFORM update_menu_item_ingredients(new_menu_name,ingredients_list);
        RETURN true;
    end if;
EXCEPTION
    WHEN OTHERS THEN
		RETURN false;
END;
$$ LANGUAGE plpgsql;