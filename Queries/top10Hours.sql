SELECT date_trunc('hour',date_of_sale) as hour_of_sale, SUM(cost) as total_cost, COUNT(*) as number_of_orders
FROM customer_order
GROUP BY hour_of_sale
ORDER BY total_cost DESC
LIMIT 10;