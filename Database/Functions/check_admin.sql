-- Description: This function checks whether a given username belongs to an admin user in the 'operator' table.

-- Parameters:
-- @username: The username to check for admin privileges.

-- Returns:
-- Boolean: True if the user is an admin, False otherwise.

CREATE FUNCTION check_admin(username text)
RETURNS boolean AS $$
DECLARE
    is_admin boolean;
BEGIN
    -- Select the 'admin' column value from the 'operator' table where the 'name' matches the provided username.
    -- Assign the result to the 'is_admin' variable.
    SELECT admin FROM operator WHERE name = username INTO is_admin;
    
    -- Return the value of 'is_admin', indicating whether the user is an admin or not.
    RETURN is_admin;
END;
$$ LANGUAGE plpgsql;
