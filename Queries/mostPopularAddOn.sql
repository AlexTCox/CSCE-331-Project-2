# Getting the most popular menu items in desending order:
SELECT ij.name AS item_name, COUNT(*) AS total_items
FROM add_on_order as i
INNER JOIN ingredients AS ij on i.ingredient_id = ij.id
GROUP BY ij.name
ORDER BY total_items DESC;