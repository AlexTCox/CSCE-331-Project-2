-- Description: This function is a helper function for generating a report of menu items that are frequently sold together within a specified time period.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.
-- @end_date: The end date of the time period for which to generate the report.

-- Returns: Table containing pairs of menu items and the count of times they were sold together within the specified time period.

CREATE FUNCTION sells_together_helper(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    first_item text,
    second_item text,
    total bigint
) AS $$
BEGIN
    -- Query to retrieve pairs of menu items and the count of times they were sold together within the specified time period.
    RETURN QUERY SELECT item1.name AS first_item, item2.name AS second_item, COUNT(*) AS total
                 FROM menu_order id1
                 INNER JOIN menu_order id2 ON id1.id != id2.id
                 INNER JOIN customer_order ON customer_order.id = id1.customer_order_id
                 INNER JOIN menu_item item1 ON item1.id = id1.menu_item_id
                 INNER JOIN menu_item item2 ON item2.id = id2.menu_item_id
                 WHERE id1.menu_item_id != id2.menu_item_id 
                    AND id1.customer_order_id = id2.customer_order_id 
                    AND date_of_sale BETWEEN start_date AND end_date
                 GROUP BY item1.name, item2.name
                 ORDER BY total DESC;
END;
$$ LANGUAGE plpgsql;
