-- Description: This function generates a report of ingredients that have been used in product items within a specified time period 
--              and have usage less than 10% of their total stock.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.
-- @end_date: The end date of the time period for which to generate the report.

-- Returns: Table containing the name of each ingredient, its current stock, and the total usage within the specified time period.

CREATE FUNCTION excess_report_new(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    name text,
    stock bigint,
    total_usage bigint
)
AS $$
BEGIN
    -- Query to retrieve ingredients and their usage within the specified time period
    RETURN QUERY 
    SELECT i.name, i.stock, p.total_sales
    FROM ingredients i
    INNER JOIN (
        -- Subquery to calculate total usage of each ingredient within the specified time period
        SELECT ingredient_id, SUM(count) AS total_sales
        FROM product_usage(start_date, end_date)
        GROUP BY ingredient_id
    ) p ON i.id = p.ingredient_id
    -- Filter to include only ingredients with usage less than 10% of their stock
    WHERE (p.total_sales::float / i.stock) * 100 < 10;
END;
$$ LANGUAGE plpgsql;
