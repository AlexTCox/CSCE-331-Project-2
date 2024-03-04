CREATE FUNCTION product_usage(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    date timestamp,
    count bigint)
AS $$
BEGIN
    RETURN QUERY SELECT date_trunc('day',date_of_sale) as day_of_sale, COUNT(*) as total_sales
    FROM menu_item_ingredient
    INNER JOIN menu_item ON menu_item.id = menu_item_ingredient.menu_id
    INNER JOIN menu_order ON menu_order.menu_item_id = menu_item.id
    INNER JOIN customer_order ON customer_order.id = menu_order.customer_order_id
    WHERE date_of_sale BETWEEN start_date AND end_date
    GROUP BY day_of_sale
    ORDER BY day_of_sale DESC;
    
END;
$$ LANGUAGE plpgsql;