CREATE FUNCTION check_pin(username text, hashed_pin int)
RETURNS boolean AS $$
DECLARE
    stored_pin_hash integer;
BEGIN
    SELECT pin_hash FROM operator WHERE name = username  INTO stored_pin_hash;
    if hashed_pin = stored_pin_hash then 
        RETURN true;
    else 
        RETURN false;
    end if;
END;
$$ LANGUAGE plpgsql;