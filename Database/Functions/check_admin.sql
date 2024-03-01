CREATE FUNCTION check_admin(username text)
RETURNS boolean AS $$
DECLARE
    is_admin boolean;
BEGIN
    SELECT admin FROM operator WHERE name = username  INTO is_admin;
    RETURN is_admin;
END;
$$ LANGUAGE plpgsql;