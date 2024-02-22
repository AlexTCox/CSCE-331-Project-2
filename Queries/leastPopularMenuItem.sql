# Getting the most popular menu items in desending order:
SELECT ij.name AS item_name, COUNT(*) AS total_items
FROM menu_order as i
INNER JOIN menu_item AS ij on i.menu_item_id = ij.id
GROUP BY ij.name
ORDER BY total_items ASC;