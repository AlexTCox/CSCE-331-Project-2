SELECT date_trunc('week',date_of_sale) AS week, COUNT (*) as amount_of_orders
FROM customer_order
GROUP BY week
ORDER BY week ASC;