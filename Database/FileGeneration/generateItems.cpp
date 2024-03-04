#include <iostream>
#include <vector>

// Define enum for ingredients
enum ingredient
{
    zero,
    lettuce,
    cheese,
    sour_cream,
    tomato,
    salsa_verde,
    salsa_roja,
    onion,
    cilantro,
    guacamole,
    bean,
    hot_sauce,
    pineapple,
    tortilla,
    chicken,
    camel,
    shark,
    beef,
    wombat,
    kangaroo,
    pork,
    vegan_meat,
    turtle,
    monkey,
    penguin,
    seal,
    fish,
    zebra,
    lamb,
    dinosaur_nugget,
    jaguar,
    cactus,
    jalapeno
};

int main()
{

    // Create a vector of vectors to represent menu items and their ingredients
    std::vector<std::vector<int>> x;
    x.push_back(std::vector<int>{chicken, sour_cream, tomato, cheese});
    x.push_back(std::vector<int>{camel, guacamole, tomato});
    x.push_back(std::vector<int>{shark, onion, cilantro});
    x.push_back(std::vector<int>{beef, hot_sauce, lettuce, cheese});
    x.push_back(std::vector<int>{wombat, hot_sauce, jalapeno});
    x.push_back(std::vector<int>{kangaroo, jalapeno, cheese});
    x.push_back(std::vector<int>{pork, lettuce, tomato});
    x.push_back(std::vector<int>{vegan_meat, lettuce, tomato, onion});
    x.push_back(std::vector<int>{turtle, hot_sauce, onion});
    x.push_back(std::vector<int>{penguin, lettuce, sour_cream, salsa_verde});
    x.push_back(std::vector<int>{monkey, pineapple, cilantro});
    x.push_back(std::vector<int>{lettuce, tomato, onion, guacamole});
    x.push_back(std::vector<int>{seal, salsa_roja, lettuce});
    x.push_back(std::vector<int>{fish, pineapple, cilantro});
    x.push_back(std::vector<int>{bean, cheese});
    x.push_back(std::vector<int>{zebra, cilantro, cheese, onion});
    x.push_back(std::vector<int>{lamb, sour_cream, lettuce, cheese});
    x.push_back(std::vector<int>{dinosaur_nugget, cheese, lettuce});
    x.push_back(std::vector<int>{jaguar, hot_sauce, cheese, lettuce});
    x.push_back(std::vector<int>{cactus, onion, lettuce});

    // Print SQL-like insert statements to associate each menu item with its ingredients
    std::cout << "INSERT INTO menu_item_ingredient (menu_id, ingredient_id)";
    std::cout << "\n VALUES ";

    int id = 1;
    for (std::vector<int> item : x)
    {
        // Move to the next line for the next menu item
        std::cout << "\n";

        for (int ingredient_id : item)
        {
            // Print the menu ID and ingredient ID for each ingredient in menu item
            std::cout << "(" << id << ", " << ingredient_id << "), ";
        }

        id++;
    }
    return 0;
};
