import csv
import random
import datetime
from datetime import date, timedelta


# Random values for menu, drink, add-on amounts:
menuRand = random.randint(0,100)
drinkRand = random.randint(0,100)
add_onRand = random.randint(0,100)
operatorRand = random.randint(0,1)


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

# Add-on amount:
if add_onRand <= 10:
    add_onAmount = 0
elif add_onRand > 10 and add_onRand <= 25:
    add_onAmount = 1
elif add_onRand > 25 and add_onRand <= 75:
    add_onAmount = 2
elif add_onRand > 75 and add_onRand <= 89:
    add_onAmount = 1
else:
    add_onAmount = 0

peakDayOne = datetime.datetime(2023,5,5)
peakDayTWo = datetime.datetime(2023,7,4)



oldDate = datetime.datetime(2023,2,22)
order_id = 0 #Maybe change to 1 based on serial id
# The dates for the data:
for i in range(365):
    currentDate = (oldDate + timedelta(i)).strftime("%Y-%m-%d")

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
        peoplemount = 300

    # The amount of orders in a day:
    for j in range(peopleAmount):

        # Random values for menu, drink, add-on amounts:
        menuRand = random.randint(0,100)
        drinkRand = random.randint(0,100)
        add_onRand = random.randint(0,100)
        operatorRand = random.randint(0,1)


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

        # Add-on amount:
        if add_onRand <= 10:
            add_onAmount = 0
        elif add_onRand > 10 and add_onRand <= 25:
            add_onAmount = 1
        elif add_onRand > 25 and add_onRand <= 75:
            add_onAmount = 2
        elif add_onRand > 75 and add_onRand <= 89:
            add_onAmount = 1
        else:
            add_onAmount = 0
        #Call function to get price amount
        #ID is i+j
        #Operator id is operatorRand
        #Date of sale is currentDate
        ++order_id

        # Creating the junction table for drinkAmount
        def rand_drink():
            drink = random.randint(1, 3)  # Generates a random drink ID between 1 and 3 (inclusive)
            return drink

        def insert_order(order_id, drink):
            with open('drink_order.csv', 'a', newline='') as csvfile:
                fieldnames = ['order_id', 'drink_id']
                writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
                
                # Write the order to the CSV file
                writer.writerow({'order_id': order_id, 'drink_id': drink})

        for k in range(drinkAmount):
            drink = rand_drink()
            insert_order(order_id, drink)   

        # Creating the junction table entries for menu items:
        def rand_menu_item():
            return random.randint(1, 20)  # Generates a random menu item ID between 1 and 20 (inclusive)

        def insert_menu_item(order_id, menu_item):
            with open('menu_order.csv', 'a', newline='') as csvfile:
                fieldnames = ['order_id', 'menu_item_id']
                writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
                
                # Write the menu item to the CSV file
                writer.writerow({'order_id': order_id, 'menu_item_id': menu_item})

        for l in range(menuAmount):
            menu_item = rand_menu_item()
            insert_menu_item(order_id, menu_item)

        # Creating the junction table entries for add_ons:
        def rand_addon():
            return random.randint(1, 22)  # Generates a random addon ID between 1 and 22 (inclusive)

        def insert_addon(order_id, addon):
            with open('add_on_order.csv', 'a', newline='') as csvfile:
                fieldnames = ['order_id', 'addon_id']
                writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
                
                # Write the addon to the CSV file
                writer.writerow({'order_id': order_id, 'addon_id': addon})

        for k in range(add_onAmount):
            addon = rand_addon()
            insert_addon(order_id, addon)
        
        # Adding the cost of all the items together
        def get_price(id, filename):
            with open(filename, 'r') as csvfile:
                reader = csv.DictReader(csvfile)
                for row in reader:
                    if int(row['id']) == id:
                        return float(row['price'])
            return None  # Return None if item id is not found

        def calculate_order_cost(order_id):
            total_cost = 0.0
            
            # Look into the junction table for menu items
            with open('menu_order.csv', 'r') as csvfile:
                reader = csv.DictReader(csvfile)
                for row in reader:
                    if int(row['order_id']) == order_id:
                        menu_item_id = int(row['menu_item_id'])
                        price = get_price(menu_item_id, 'menu.csv')
                        if price:
                            total_cost += price
            
            # Look into the junction table for drinks
            with open('drink_order.csv', 'r') as csvfile:
                reader = csv.DictReader(csvfile)
                for row in reader:
                    if int(row['order_id']) == order_id:
                        drink_id = int(row['drink_id'])
                        price = get_price(drink_id, 'drink.csv')
                        if price:
                            total_cost += price
            
            # Look into the junction table for addons
            with open('addon_order.csv', 'r') as csvfile:
                reader = csv.DictReader(csvfile)
                for row in reader:
                    if int(row['order_id']) == order_id:
                        addon_id = int(row['addon_id'])
                        price = get_price(addon_id, 'addon.csv')
                        if price:
                            total_cost += price
            
            return total_cost
