-- Description: This function generates a report of menu items that are frequently sold together within a specified time period.

-- Parameters:
-- @start_date: The start date of the time period for which to generate the report.
-- @end_date: The end date of the time period for which to generate the report.

-- Returns: Table containing pairs of menu items and the count of times they were sold together within the specified time period.

CREATE FUNCTION sells_together(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    item_1 text,
    item_2 text,
    total_times_combined bigint
) AS $$
BEGIN
    -- Query to retrieve pairs of menu items and the count of times they were sold together using the 'sells_together_helper' function.
    -- Ensure that only unique pairs are returned by filtering out duplicates where item_1 is greater than item_2.
    RETURN QUERY SELECT * FROM sells_together_helper(start_date, end_date) AS helper
                 WHERE (helper.first_item, helper.second_item) > (helper.second_item, helper.first_item);
END;
$$ LANGUAGE plpgsql;
