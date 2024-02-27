SELECT date_trunc('week',date_of_sale) as week_of_sale, SUM(cost) as total_cost, COUNT(*) as number_of_orders
FROM customer_order
GROUP BY week_of_sale
ORDER BY total_cost DESC
LIMIT 10;