CREATE FUNCTION new_ingredient(new_name text, set_stock int, add_price float, set_min_stock int)
RETURNS boolean AS $$
DECLARE
    check_for_ingredient int := 0;
BEGIN
    SELECT id FROM ingredients WHERE name = new_name INTO check_for_ingredient;
    if NOT check_for_ingredient = 0 then
        RETURN false;
    else 
        INSERT INTO ingredients(name, stock, add_on_price, min_stock) VALUES (new_name, set_stock, add_price, set_min_stock);
        RETURN true;
    end if;
END;
$$ LANGUAGE plpgsql;
