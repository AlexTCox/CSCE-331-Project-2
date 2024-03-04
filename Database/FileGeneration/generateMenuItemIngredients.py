import psycopg2

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

# Inserting menu items into the menu_item table
cur.execute(
    """
    INSERT INTO menu_item (name, price)
    VALUES 
    ('Cool Chicken Taco', 2),
    ('Crazy Camel Taco', 2),
    ('Super Shark Taco', 6),
    ('Buckin Bull Taco', 2),
    ('Wild Wombat Taco', 7),
    ('Kicken Kangaroo Taco', 7),
    ('Pretty Pork Taco', 2),
    ('Impossible Taco', 3.5),
    ('Tootin Turtle Taco', 8),
    ('Perky Pengiuin Taco', 8),
    ('Massive Monkey Taco', 6),
    ('Very Vegeterian Taco', 2.25),
    ('Savory Seal Taco', 5),
    ('Flappin Fish Taco', 3),
    ('Bean Taco', .5),
    ('Zesty Zebra', 7),
    ('Lovely Lamb Taco', 4),
    ('Daddys Delicious Dinosaur Taco', 5),
    ('Jiggy Jaguar Taco' , 5),
    ('Cuttin Catctus Taco', 2.5);
    """
)
# Inserting ingredients into the ingredients table
cur.execute(
    """
    INSERT INTO ingredients (name, stock, add_on_price, min_stock)
    VALUES 
        ('lettuce',100,1,10),
        ('cheese',100,1.50,10),
        ('sour_cream',100,1,10),
        ('tomato',100,1,10),
        ('salsa_verde',100,.5,10),
        ('salsa_roja',100,.5,10),
        ('onion',100,1,10),
        ('cilantro',100,1,10),
        ('guacamole',100,3,10),
        ('bean',100,3,10),
        ('hot_sauce',100,1,10),
        ('pineapple',100,3,10),
        ('tortilla',100,1,10),
        ('chicken', 100, 4, 10),
        ('camel', 100, 4, 10),
        ('shark', 100, 4, 10),
        ('beef', 100, 4, 10),
        ('wombat', 100, 4, 10),
        ('kangaroo', 100, 4, 10),
        ('pork', 100, 4, 10),
        ('vegan_meat', 100, 4, 10),
        ('turtle', 100, 4, 10),
        ('monkey', 100, 4, 10),
        ('penguin', 100, 4, 10),
        ('seal', 100, 4, 10),
        ('fish', 100, 4, 10),
        ('zebra', 100, 4, 10),
        ('lamb', 100, 4, 10),
        ('dinosaur_nugget', 100, 4, 10),
        ('jaguar', 100, 4, 10),
        ('cactus', 100, 4, 10),
        ('jalapeno',100,2,10);
    """
)
# Commit changes to the database
conn.commit()

# Associating menu items with ingredients in the menu_item_ingredient table
cur.execute(
    """
INSERT INTO menu_item_ingredient (menu_id, ingredient_id)
    VALUES 
    (1, 14), (1, 3), (1, 4), (1, 2), 
    (2, 15), (2, 9), (2, 4), 
    (3, 16), (3, 7), (3, 8), 
    (4, 17), (4, 11), (4, 1), (4, 2), 
    (5, 18), (5, 11), (5, 32), 
    (6, 19), (6, 32), (6, 2), 
    (7, 20), (7, 1), (7, 4), 
    (8, 21), (8, 1), (8, 4), (8, 7), 
    (9, 22), (9, 11), (9, 7), 
    (10, 24), (10, 1), (10, 3), (10, 5), 
    (11, 23), (11, 12), (11, 8), 
    (12, 1), (12, 4), (12, 7), (12, 9), 
    (13, 25), (13, 6), (13, 1), 
    (14, 26), (14, 12), (14, 8), 
    (15, 10), (15, 2), 
    (16, 27), (16, 8), (16, 2), (16, 7), 
    (17, 28), (17, 3), (17, 1), (17, 2), 
    (18, 29), (18, 2), (18, 1), 
    (19, 30), (19, 11), (19, 2), (19, 1), 
    (20, 31), (20, 7), (20, 1);
    """
)

# Commit changes again
conn.commit()

# Close cursor and connection
cur.close()
conn.close()