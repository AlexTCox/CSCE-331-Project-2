-- Description: This function generates a report of ingredient usage within a specified time period.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.
-- @end_date: The end date of the time period for which to generate the report.

-- Returns: Table containing the name of each ingredient and the count of its usage within the specified time period.

CREATE FUNCTION product_usage(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    name text,
    count bigint
) AS $$
BEGIN
    -- Query to retrieve the count of ingredient usage within the specified time period.
    RETURN QUERY SELECT ingredients.name, COUNT(*) AS count
                 FROM ingredients
                 INNER JOIN menu_item_ingredient ON menu_item_ingredient.ingredient_id = ingredients.id
                 INNER JOIN menu_item ON menu_item.id = menu_item_ingredient.menu_id
                 INNER JOIN menu_order ON menu_order.menu_item_id = menu_item.id
                 INNER JOIN customer_order ON customer_order.id = menu_order.customer_order_id
                 WHERE date_of_sale BETWEEN start_date AND end_date
                 GROUP BY ingredients.name;
END;
$$ LANGUAGE plpgsql;
