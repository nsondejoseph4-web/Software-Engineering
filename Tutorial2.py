country_names = ['England', 'Canada', 'Spain', 'France', 'Germany']
for country in sorted(country_names):
    print(f"Welcome to {country}!")
country_names[1] = 'Italy'
print(country_names)
country_names[1:3] = ['Portugal', 'Netherlands']
print(country_names)

biography = {
    'first_name': 'Joey',
    'last_name': 'Nsonde',
    'age': 20,
    'city': 'Brighton'
}
x = biography['first_name']
y = biography['last_name']
z = biography['age']
w = biography['city']
print(f"{x} {y} is {z} years old and lives in {w}")

river_countries = {
    'Nile': 'Egypt',
    'Amazon': 'Brazil',
    'Yangtze': 'China',
}
for river, country in river_countries.items():
    print(f"The {river} runs through {country}.")
for river in river_countries.keys():
    print(f"The {river} is a major river.")
for country in river_countries.values():
    print(f"{country} is home to a major river.")

# Part 1: Creating and unpacking a 3-item tuple
my_tuple = ("Joey", "Software Engineering", 2026)

# Unpacking into three separate variables
name, course, year = my_tuple

print("--- Part 1 Output ---")
print(name)
print(course)
print(year)


# Part 2: Creating a larger tuple and unpacking parts of it
large_tuple = ("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig")

# Unpacking the first, the last, and grouping the rest into a list
first_fruit, *middle_fruits, last_fruit = large_tuple

print("\n--- Part 2 Output ---")
print(f"First fruit: {first_fruit}")
print(f"Middle fruits (as a list): {middle_fruits}")
print(f"Last fruit: {last_fruit}")
