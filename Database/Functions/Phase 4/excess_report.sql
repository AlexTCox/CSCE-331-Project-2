CREATE FUNCTION excess_report(start_date timestamp)
RETURNS TABLE (name text)
AS $$
BEGIN   
    SELECT name from product_usage_ind(start_date, NOW()::TIMESTAMP)
    WHERE count < 10;
END;
$$ LANGUAGE plpgsql;

-- How to determine starter inventory?
-- Hard code or get from parameters