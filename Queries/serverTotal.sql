SELECT ij.name AS item_name, COUNT(*) AS total_items
FROM customer_order as i
INNER JOIN operator AS ij on i.operator_id = ij.id
GROUP BY ij.name
ORDER BY total_items DESC;