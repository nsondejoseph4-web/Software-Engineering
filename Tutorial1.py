print("Hello World")

# Fixed the variable names here
name, surname, course = "Joey", "Nsonde", "Software Engineering"
print(name, surname, course)

for i in range(6):
    # Updated to use the correct variables
    print(f"My name is {name} {surname} and I am studying {course}.")

count = 0
while count < 6:
    # Updated to use the correct variables
    print(f"My name is {name} {surname} and I am studying {course}.")
    count += 1
def WeekDay():
    day_num = int(input("Enter a number between 1 and 7: "))
    if day_num == 1:
        print("Monday")
    elif day_num == 2:
        print("Tuesday")
    elif day_num == 3:
        print("Wednesday")
    elif day_num == 4:        
        print("Thursday")
    elif day_num == 5:
        print("Friday")
    elif day_num == 6:        
        print("Saturday")
    elif day_num == 7:        
        print("Sunday")
    else:
        print("Invalid input. Please enter a number between 1 and 7.")
WeekDay()
text = 'Artificial Intelligence'
print(text[:3])
print(text[3:])
print(text[:10])
print(text[10:11])
print(text[11:])
def sum_numbers(*args):
    sum = 0
    for n in args:
        sum += n
    return sum
print(sum_numbers(1, 2, 3, 4, 5))