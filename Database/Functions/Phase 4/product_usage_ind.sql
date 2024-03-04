CREATE FUNCTION product_usage_ind(start_date timestamp, end_date timestamp)
RETURNS TABLE (
    name text,
    count bigint)
AS $$
BEGIN
    RETURN QUERY SELECT ingredients.name, COUNT(*) as total_sales
    FROM ingredients
	INNER JOIN menu_item_ingredient ON menu_item_ingredient.ingredient_id = ingredients.id
    INNER JOIN menu_item ON menu_item.id = menu_item_ingredient.menu_id
    INNER JOIN menu_order ON menu_order.menu_item_id = menu_item.id
    INNER JOIN customer_order ON customer_order.id = menu_order.customer_order_id
    WHERE date_of_sale BETWEEN start_date AND end_date
    GROUP BY ingredients.name;
    
END;
$$ LANGUAGE plpgsql;