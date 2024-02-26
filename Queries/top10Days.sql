SELECT CAST(date_of_sale as date), SUM(cost) as total_cost
from customer_order
group by CAST(date_of_sale as date)
ORDER BY total_cost DESC
LIMIT 10;


