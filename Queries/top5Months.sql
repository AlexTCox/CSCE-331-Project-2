SELECT date_trunc('month',date_of_sale) as month, SUM(cost) as total_cost, COUNT(*) as number_of_orders
FROM customer_order
GROUP BY month
ORDER BY total_cost DESC
LIMIT 5;