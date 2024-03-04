CREATE FUNCTION restock()
RETURNS TABLE (item text, quantity int) AS $$
BEGIN   
    --returns all stock where the current quantity is less than their minimum stock --
    RETURN QUERY SELECT name, stock FROM ingredients
    WHERE min_stock >= stock;
END;
$$ LANGUAGE plpgsql;
