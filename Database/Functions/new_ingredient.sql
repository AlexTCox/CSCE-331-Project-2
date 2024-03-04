-- Description: This function adds a new ingredient to the system with specified properties.

-- Parameters:
-- @new_name: The name of the new ingredient.
-- @set_stock: The initial stock quantity of the new ingredient.
-- @add_price: The additional price of the ingredient when used as an add-on.
-- @set_min_stock: The minimum stock level required for the ingredient.

-- Returns: Boolean indicating whether the ingredient was successfully added (true) or not (false).

CREATE FUNCTION new_ingredient(new_name text, set_stock int, add_price float, set_min_stock int)
RETURNS boolean AS $$
DECLARE
    check_for_ingredient int := 0;
BEGIN
    -- Check if an ingredient with the same name already exists.
    SELECT id FROM ingredients WHERE name = new_name INTO check_for_ingredient;
    
    -- If an ingredient with the same name already exists, return false.
    IF NOT check_for_ingredient = 0 THEN
        RETURN false;
    ELSE
        -- If the ingredient doesn't exist, insert a new record into the 'ingredients' table with the provided details.
        INSERT INTO ingredients(name, stock, add_on_price, min_stock) VALUES (new_name, set_stock, add_price, set_min_stock);
        -- Return true to indicate successful addition of the new ingredient.
        RETURN true;
    END IF;
END;
$$ LANGUAGE plpgsql;
