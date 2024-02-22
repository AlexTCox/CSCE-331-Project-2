import psycopg2
import random
import datetime
from datetime import date, timedelta


# Connecting to database:
conn = psycopg2.connect(
    database = "csce331_550_01_db",
    user = "csce331_550_01_user",
    host = "54.147.249.118",
    password = "cSCUE8w9",
    port = 5432
)

# How we will execute SQL commands:
cur = conn.cursor()

# Removing all the tables:
cur.execute (
    """DROP TABLE drink_order"""
)
# Commit changes to the database
conn.commit()

cur.execute (
    """DROP TABLE add_on_order"""
)
# Commit changes to the database
conn.commit()

cur.execute (
    """DROP TABLE menu_item_ingredient"""
)
# Commit changes to the database
conn.commit()

cur.execute (
    """DROP TABLE menu_order"""
)
# Commit changes to the database
conn.commit()

cur.execute (
    """DROP TABLE customer_order"""
)
# Commit changes to the database
conn.commit()

cur.execute (
    """DROP TABLE ingredients"""
)
# Commit changes to the database
conn.commit()

cur.execute (
    """DROP TABLE operator"""
)
# Commit changes to the database
conn.commit()

cur.execute (
    """DROP TABLE menu_item"""
)
# Commit changes to the database
conn.commit()

cur.execute (
    """DROP TABLE drinks"""
)
# Commit changes to the database
conn.commit()



# Menu_Item table:
cur.execute (
    """CREATE TABLE menu_item (
    id serial primary key,
    name text,
    price float);"""
)
# Commit changes to the database
conn.commit()

# Operator table:
cur.execute (
    """CREATE TABLE operator (
    id serial primary key,
    name text,
    pin_hash int,
    admin bool);"""
)
# Commit changes to the database
conn.commit()

# Ingredients table:
cur.execute (
    """CREATE TABLE ingredients (
    id serial primary key,
    name text,
    stock int,
    add_on_price float,
    min_stock int);"""
)
# Commit changes to the database
conn.commit()

# drinks table
cur.execute (
    """CREATE TABLE drinks (
    id serial primary key,
    name text,
    price float,
    size text);"""
)
# Commit changes to the database
conn.commit()

# Customer Order table:
cur.execute (
    """CREATE TABLE customer_order (
    id serial primary key,
    cost float,
    addOn bool,
    date_of_sale date,
    operator_id serial,
    FOREIGN KEY (operator_id) REFERENCES operator(id));"""
)
# Commit changes to the database
conn.commit()

# menu item and customer_order table
cur.execute (
    """CREATE TABLE menu_order (
    id serial primary key,
    menu_item_id serial,
    customer_order_id serial,
    FOREIGN KEY (menu_item_id) REFERENCES menu_item(id),
    FOREIGN KEY (customer_order_id) REFERENCES customer_order(id));"""
)
# Commit changes to the database
conn.commit()

# drink_order table
cur.execute (
    """CREATE TABLE drink_order (
    id serial primary key,
    drink_id serial,
    customer_order_id serial,
    FOREIGN KEY (drink_id) REFERENCES drinks(id),
    FOREIGN KEY (customer_order_id) REFERENCES customer_order(id));"""
)
# Commit changes to the database
conn.commit()

# menu item and ingredient table:
cur.execute (
    """CREATE TABLE menu_item_ingredient (
    id serial primary key,
    menu_id serial,
    ingredient_id serial,
    FOREIGN KEY (menu_id) REFERENCES menu_item(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id));"""
)
# Commit changes to the database
conn.commit()

# add-on Order
cur.execute (
    """CREATE TABLE add_on_order (
    id serial primary key,
    ingredient_id serial,
    customer_order_id serial,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id),
    FOREIGN KEY (customer_order_id) REFERENCES customer_order(id));"""
)
# Commit changes to the database
conn.commit()

# Close cursor and connection
cur.close()
conn.close()