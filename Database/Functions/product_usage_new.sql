-- Description: This function generates a report of ingredient usage within a specified time period, aggregated by day.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.
-- @end_date: The end date of the time period for which to generate the report.

-- Returns: Table containing the date, name of each ingredient, and the count of its usage for each day within the specified time period.

CREATE FUNCTION product_usage(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    date timestamp,
    ingredient_name text,
    count bigint
)
AS $$
BEGIN
    -- Query to retrieve ingredient usage within the specified time period, aggregated by day
    RETURN QUERY 
    SELECT date_trunc('day', date_of_sale) AS date, ingredients.name AS ingredient_name, COUNT(*) AS count
    FROM menu_item_ingredient
    INNER JOIN ingredients ON menu_item_ingredient.ingredient_id = ingredients.id
    INNER JOIN menu_order ON menu_order.menu_item_id = menu_item_ingredient.menu_id
    INNER JOIN customer_order ON customer_order.id = menu_order.customer_order_id
    WHERE date_of_sale BETWEEN start_date AND end_date
    GROUP BY date, ingredients.name
    ORDER BY date;
END;
$$ LANGUAGE plpgsql;
