import random
import datetime
import psycopg2

from datetime import date, timedelta

# Connecting to database:
conn = psycopg2.connect(
    database = "csce331_550_01_db",
    user = "csce331_550_01_user",
    host = "54.147.249.118",
    password = "cSCUE8w9",
    port = 5432
)

cur = conn.cursor()
conn.autocommit = True

# Two peak days that will have a x2 amount of customers:
peakDayOne = datetime.datetime(2023,5,5)
peakDayTwo = datetime.datetime(2023,7,4)


oldDate = datetime.datetime(2023,2,21)

# The dates for the data:
for i in range(365):
    currentDate = (oldDate + timedelta(i))

    # Getting a random amount of people:
    peopleRand = random.randint(0,100)

    if peopleRand <= 10:
        peopleAmount = 200
    elif peopleRand > 10 and peopleRand <= 25:
        peopleAmount = 225
    elif peopleRand > 25 and peopleRand <= 75:
        peopleAmount = 250
    elif peopleRand > 75 and peopleRand <= 89:
        peopleAmount = 275
    else:
        peopleAmount = 300

    # If the date is one of the peak days:
    if (currentDate == peakDayOne) or (currentDate == peakDayTwo):
        peopleAmount = peopleAmount*2


    # The amount of orders in a day:
    for j in range(peopleAmount):

        # Random values for menu, drink, add-on amounts and operator:
        menuRand = random.randint(0,100)
        drinkRand = random.randint(0,100)
        addOnRand = random.randint(0,100)
        operatorRand = random.randint(1,2)


        # Finding the amount of menu items:
        if menuRand <= 10:
            menuAmount = 1
        elif menuRand > 10 and menuRand <= 25:
            menuAmount = 3
        elif menuRand > 25 and menuRand <= 75:
            menuAmount = 2
        elif menuRand > 75 and menuRand <= 89:
            menuAmount = 3
        else:
            menuAmount = 5

        print(menuAmount)

        # Finding the amount of drinks:
        if drinkRand <= 10:
            drinkAmount = 0
        elif drinkRand > 10 and drinkRand <= 25:
            drinkAmount = 1
        elif drinkRand > 25 and drinkRand <= 75:
            drinkAmount = 1
        elif drinkRand > 75 and drinkRand <= 89:
            drinkAmount = 2
        else:
            drinkAmount = 0

        print(drinkAmount)

        # Add-on amount:
        if addOnRand <= 10:
            addOnAmount = 0
        elif addOnRand > 10 and addOnRand <= 25:
            addOnAmount = 1
        elif addOnRand > 25 and addOnRand <= 75:
            addOnAmount = 2
        elif addOnRand > 75 and addOnRand <= 89:
            addOnAmount = 1
        else:
            addOnAmount = 0

        print(addOnAmount)


        # Randomizing the time of the order:
        randHour = random.randint(11,20)
        randMin = random.randint(0,59)

        randTime = datetime.time(hour=randHour, minute=randMin)
            
        timestamp = (datetime.datetime.combine(currentDate, randTime))   

        # String that will execute everything at one time
        finalExecutable = ""   


        # Making the orders with blank total cost:
        executableOrder = f"INSERT INTO customer_order (date_of_sale, operator_id) VALUES ('{timestamp}', {operatorRand}) returning id;"

        cur.execute (executableOrder)


        orderId = cur.fetchone()[0]

        # Making the menu item and order junction table:
        for k in range(menuAmount):

            randMenuitem = random.randint(1,20)

            executableMenuItem = f"INSERT INTO menu_order (menu_item_id,customer_order_id) VALUES ({randMenuitem}, {orderId});"

            finalExecutable = finalExecutable + "\n" + executableMenuItem
        
        # Making the add on and order junction table:
        for l in range(addOnAmount):
            randAddOn = random.randint(1,32)

            executableAddOn = f"INSERT INTO add_on_order (ingredient_id,customer_order_id) VALUES ({randAddOn}, {orderId});"

            finalExecutable = finalExecutable + "\n"  + executableAddOn
        
        # Making the drink and order junction table:
        for m in range(drinkAmount):
            randDrink = random.randint(1,3)

            executableDrink = f"INSERT INTO drink_order (drink_id,customer_order_id) VALUES ({randDrink}, {orderId});"

            finalExecutable = finalExecutable + "\n"  + executableDrink
        
        # Executes the final output in one go so there is less back and forth
        cur.execute(finalExecutable)


        

    


