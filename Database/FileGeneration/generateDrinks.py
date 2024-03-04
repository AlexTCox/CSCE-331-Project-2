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
conn.autocommit = True

# Making the 3 drink sizes:
cur.execute("INSERT INTO drinks (size,price) VALUES ('small', 2.00)")
cur.execute("INSERT INTO drinks (size,price) VALUES ('medium', 4.00)")
cur.execute("INSERT INTO drinks (size,price) VALUES ('large', 5.00)")