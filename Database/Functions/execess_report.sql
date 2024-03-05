-- Description: This function generates a report of ingredients that have been used in product items within a specified time period 
--              and have usage less than 10% of their total stock.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.
-- @end_date: The end date of the time period for which to generate the report.

-- Returns: Table containing the name of each ingredient.

CREATE FUNCTION excess_report(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    name text
)
AS $$
BEGIN
    -- Query to retrieve ingredients with usage less than 10% of their stock within the specified time period
    RETURN QUERY 
    SELECT name
    FROM product_usage_ind(start_date, end_date)
    WHERE count::float / stock * 100 < 10;
END;
$$ LANGUAGE plpgsql;
