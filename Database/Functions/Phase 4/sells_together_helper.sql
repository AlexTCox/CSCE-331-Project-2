CREATE FUNCTION sells_together_helper(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    first_item text,
    second_item text,
    total bigint)
AS $$
BEGIN
    RETURN QUERY SELECT item1.name as first_item, item2.name as second_item, COUNT(*) as total
    FROM menu_order id1
    INNER JOIN menu_order id2 ON id1.id != id2.id
    INNER JOIN customer_order ON customer_order.id = id1.customer_order_id
    INNER JOIN menu_item item1 ON item1.id = id1.menu_item_id
    INNER JOIN menu_item item2 ON item2.id = id2.menu_item_id
    WHERE id1.menu_item_id != id2.menu_item_id 
        AND id1.customer_order_id = id2.customer_order_id 
        AND	date_of_sale BETWEEN start_date AND end_date
    GROUP BY item1.name, item2.name
    ORDER BY total DESC;
END;
$$ LANGUAGE plpgsql;