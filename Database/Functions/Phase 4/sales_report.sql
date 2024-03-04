CREATE FUNCTION sales_report(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    item_name text,
    count bigint)
AS $$
BEGIN
    RETURN QUERY SELECT menu_item.name, COUNT(*) AS total_items
    FROM menu_order
    INNER JOIN menu_item on menu_item.id = menu_order.menu_item_id
    INNER JOIN customer_order on customer_order.id = menu_order.customer_order_id
    WHERE date_of_sale BETWEEN start_date AND end_date
    GROUP BY menu_item.name
    ORDER BY total_items DESC;
    
END;
$$ LANGUAGE plpgsql;