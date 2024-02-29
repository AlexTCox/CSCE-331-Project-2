CREATE FUNCTION new_add_on(add_on text, order_id integer) 
RETURNS void AS $$
DECLARE
    add_on_store integer;
BEGIN
    SELECT id FROM ingredients WHERE ingredients.name = add_on INTO add_on_store;
    INSERT INTO  add_on_order(ingredient_id, customer_order_id) VALUES (add_on_store, order_id);
    PERFORM finding_cost(order_id);
END;
$$ LANGUAGE plpgsql;