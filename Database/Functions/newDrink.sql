CREATE FUNCTION new_drink(drink_size text, order_id integer) 
RETURNS void AS $$
DECLARE
    drink_size_store integer;
BEGIN
    SELECT id FROM drinks WHERE drinks.size = drink_size INTO drink_size_store;
    INSERT INTO  drink_order(drink_id, customer_order_id) VALUES (drink_size_store, order_id);
    PERFORM finding_cost(order_id);
END;
$$ LANGUAGE plpgsql;