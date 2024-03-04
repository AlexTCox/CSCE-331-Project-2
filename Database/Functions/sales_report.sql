-- Description: This function generates a sales report within a specified time period, showing the count of each menu item sold.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.
-- @end_date: The end date of the time period for which to generate the report.

-- Returns: Table containing the name of each menu item and the count of its sales within the specified time period.

CREATE FUNCTION sales_report(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    item_name text,
    count bigint
) AS $$
BEGIN
    -- Query to retrieve the count of each menu item sold within the specified time period.
    RETURN QUERY SELECT menu_item.name, COUNT(*) AS count
                 FROM menu_order
                 INNER JOIN menu_item ON menu_item.id = menu_order.menu_item_id
                 INNER JOIN customer_order ON customer_order.id = menu_order.customer_order_id
                 WHERE date_of_sale BETWEEN start_date AND end_date
                 GROUP BY menu_item.name
                 ORDER BY count DESC;
END;
$$ LANGUAGE plpgsql;
