-- Description: This function checks whether a given hashed PIN matches the stored hashed PIN for a specific username in the 'operator' table.

-- Parameters:
-- @username: The username for which to check the hashed PIN.
-- @hashed_pin: The hashed PIN to compare against the stored hashed PIN.

-- Returns:
-- Boolean: True if the hashed PIN matches the stored hashed PIN for the username, False otherwise.

CREATE FUNCTION check_pin(username text, hashed_pin int)
RETURNS boolean AS $$
DECLARE
    stored_pin_hash integer;
BEGIN
    -- Select the 'pin_hash' column value from the 'operator' table where the 'name' matches the provided username.
    -- Assign the result to the 'stored_pin_hash' variable.
    SELECT pin_hash FROM operator WHERE name = username INTO stored_pin_hash;
    
    -- Compare the provided hashed PIN with the stored hashed PIN.
    -- If they match, return true; otherwise, return false.
    IF hashed_pin = stored_pin_hash THEN
        RETURN true;
    ELSE
        RETURN false;
    END IF;
END;
$$ LANGUAGE plpgsql;
