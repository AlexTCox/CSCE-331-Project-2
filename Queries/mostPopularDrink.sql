# Getting the most popular menu items in desending order:
SELECT ij.size AS item_name, COUNT(*) AS total_items
FROM drink_order as i
INNER JOIN drinks AS ij on i.drink_id = ij.id
GROUP BY ij.size
ORDER BY total_items DESC;