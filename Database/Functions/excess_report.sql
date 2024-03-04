-- Description: This function generates a report of products with usage counts less than 10 within a specified time period.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.

-- Returns: Table containing the names of products with usage counts less than 10.

CREATE FUNCTION excess_report(start_date timestamp)
RETURNS TABLE (name text) AS $$
BEGIN   
    -- Call the 'product_usage_ind' function to get product usage information for the specified time period.
    -- Filter the results to include only products with usage counts less than 10.
    RETURN QUERY SELECT name FROM product_usage_ind(start_date, NOW()::TIMESTAMP)
                 WHERE count < 10;
END;
$$ LANGUAGE plpgsql;
