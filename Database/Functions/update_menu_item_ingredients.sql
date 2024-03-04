-- Description: This function updates the ingredients associated with a menu item.

-- Parameters:
-- @menu_item_to_change: The name of the menu item whose ingredients are to be updated.
-- @new_ingredients: An array of text representing the names of the new ingredients for the menu item.

-- Returns: Void

CREATE FUNCTION update_menu_item_ingredients(menu_item_to_change text, new_ingredients text[])
RETURNS void AS $$
DECLARE
    single_ingredient text;
    get_id int;
    get_menu_id int;
BEGIN
    -- Retrieve the ID of the menu item based on its name.
    SELECT id FROM menu_item WHERE name = menu_item_to_change INTO get_menu_id;
    
    -- Delete existing records from the 'menu_item_ingredient' table for the specified menu item ID.
    DELETE FROM menu_item_ingredient WHERE menu_id = get_menu_id;
    
    -- Iterate over the array of new ingredient names.
    FOREACH single_ingredient IN ARRAY new_ingredients LOOP
        -- Retrieve the ID of the ingredient based on its name.
        SELECT id FROM ingredients WHERE name = single_ingredient INTO get_id;
        
        -- Insert a new record into the 'menu_item_ingredient' table associating the menu item with the ingredient.
        INSERT INTO menu_item_ingredient(menu_id, ingredient_id) VALUES (get_menu_id, get_id);
    END LOOP;
END;
$$ LANGUAGE plpgsql;
