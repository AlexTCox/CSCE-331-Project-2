-- Active: 1708483120968@@CSCE-315-db.engr.tamu.edu@5432@csce331_550_01_db@public
SELECT ij.name AS item_name, COUNT(*) AS total_items
FROM menu_item_ingredient as i
INNER JOIN menu_item AS ij on i.menu_id = ij.id
GROUP BY ij.name;



 