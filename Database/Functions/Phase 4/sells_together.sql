CREATE FUNCTION sells_together(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    item_1 text,
    item_2 text,
    total_times_combined bigint)
AS $$
BEGIN
    RETURN QUERY SELECT * FROM sells_together_helper(start_date, end_date) as helper
    WHERE (helper.first_item, helper.second_item) > (helper.second_item, helper.first_item);
END;
$$ LANGUAGE plpgsql;