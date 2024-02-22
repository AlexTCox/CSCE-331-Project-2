# Getting the most popular menu items in desending order:
SELECT ij.name AS item_name, COUNT(*) AS total_items
FROM drink_order as i
INNER JOIN drinks AS ij on i.drink_id = ij.id
GROUP BY ij.name
ORDER BY total_items DESC;