-- Description: This function adds a new menu option with specified ingredients to the system.

-- Parameters:
-- @new_menu_name: The name of the new menu option.
-- @new_menu_cost: The price of the new menu option.
-- @ingredients_list: An array of text representing the ingredients included in the new menu option.

-- Returns: Boolean indicating whether the menu option was successfully added (true) or not (false).

CREATE FUNCTION new_menu_option(new_menu_name text, new_menu_cost float, ingredients_list text[])
RETURNS boolean AS $$
DECLARE
    check_for_menu_item int := 0;
BEGIN 
    -- Check if a menu item with the same name already exists.
    SELECT id FROM menu_item WHERE name = new_menu_name INTO check_for_menu_item;
    
    -- If a menu item with the same name already exists, return false.
    IF NOT check_for_menu_item = 0 THEN
        RETURN false;
    ELSE
        -- If the menu item doesn't exist, insert a new record into the 'menu_item' table with the provided details.
        INSERT INTO menu_item(name, price) VALUES (new_menu_name, new_menu_cost);
        
        -- Call the 'update_menu_item_ingredients' function to update the ingredients associated with the new menu option.
        PERFORM update_menu_item_ingredients(new_menu_name, ingredients_list);
        
        -- Return true to indicate successful addition of the new menu option.
        RETURN true;
    END IF;
EXCEPTION
    -- Exception handling in case of any errors during execution.
    WHEN OTHERS THEN
		RETURN false;
END;
$$ LANGUAGE plpgsql;
