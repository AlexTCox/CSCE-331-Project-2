CREATE FUNCTION update_menu_item_ingredients(menu_item_to_change text, new_ingredients text[])
RETURNS void AS $$
DECLARE
    single_ingredient text;
    get_id int;
    get_menu_id int;
BEGIN
    SELECT id FROM menu_item WHERE menu_item.name = menu_item_to_change INTO get_menu_id;
    DELETE FROM menu_item_ingredient WHERE get_menu_id = menu_id;
    FOR single_ingredient IN ARRAY new_ingredients LOOP
        SELECT id FROM ingredients WHERE single_ingredient = ingredients.name INTO get_id;
        INSERT INTO menu_item_ingredient(menu_id, ingredient_id) VALUES (get_menu_id, get_id);
    end loop;
END;
$$ LANGUAGE plpgsql;
    