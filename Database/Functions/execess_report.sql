-- Description: This function generates a report of ingredients that have been used in product items within a specified time period 
--              and have usage less than 10% of their total stock.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.
-- @end_date: The end date of the time period for which to generate the report.

-- Returns: Table containing the name of each ingredient.

CREATE FUNCTION excess_report(start_date timestamp, end_date timestamp)
RETURNS TABLE (name_of_item text)
AS $$
BEGIN
    -- Query to retrieve the name of each ingredient used within the specified time period
    -- and join it with the stock information from the ingredients table to filter out those with usage less than 10% of their stock
    RETURN QUERY 
    SELECT product_usage.name
    FROM product_usage(start_date, end_date)
    INNER JOIN ingredients ON ingredients.name = product_usage.name
    WHERE ((product_usage.count / ingredients.stock) * 100) < 10;
END;
$$ LANGUAGE plpgsql;
