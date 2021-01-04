import random
import datetime
import pandas as pd
from models.User import User
from models.HotSpot import HotSpot
from models.Log import Log


def return_list_of_hot_spots(file):
    list_of_hot_spots = []
    df = pd.read_csv(file, delimiter=';', header=None)
    index = 1
    for index, row in df.iterrows():
        list_of_hot_spots.append(HotSpot(index, row[0], row[1], row[2], row[3], row[4]))
        index = index + 1
    return list_of_hot_spots


def generate_phone_number(list_of_numbers):
    temp = random.randint(500000000, 999999999)
    if temp not in list_of_numbers:
        return temp
    else:
        generate_phone_number(list_of_numbers)


def generate_phone_number_list(number_of_user):
    list_of_numbers = []
    for i in range(0, number_of_user, 1):
        list_of_numbers.append(generate_phone_number(list_of_numbers))
    return list_of_numbers


def generate_list_of_user(number_of_user, student=0.8, teacher=0.15, staff=0.05):

    list_of_profile_templates = ['Student', 'ServiceStaff', 'Teacher']
    list_of_profiles = []

    n_of_students = number_of_user * student
    n_of_teachers = number_of_user * teacher
    n_of_staff = number_of_user * staff

    n_of_students = round(n_of_students)
    n_of_teachers = round(n_of_teachers+0.1)
    n_of_staff = round(n_of_staff-0.1)

    for i in range(0, number_of_user, 1):
        if i <= n_of_students:
            list_of_profiles.append(list_of_profile_templates[0])
        elif n_of_students < i <= n_of_teachers + n_of_students:
            list_of_profiles.append(list_of_profile_templates[1])
        elif n_of_teachers + n_of_students < i:
            list_of_profiles.append(list_of_profile_templates[2])

    random.shuffle(list_of_profiles)

    numbers = generate_phone_number_list(number_of_user)
    list_of_user = []

    for i in range(0, number_of_user):
        list_of_user.append(User(i + 1, numbers[i], list_of_profiles[i]))
    return list_of_user


def user_to_csv(list_of_users, filename):
    headers = ["Id", "PhoneNumber", "Profile"]
    data = []
    for x in list_of_users:
        data.append(x.to_list())

    df = pd.DataFrame(data, columns=headers)
    print(df)
    df.to_csv(filename, index=False, header=True)


def main():
    # list = return_list_of_hot_spots('hotspot.csv')
    # a = generate_list_of_user(15000)
    b = generate_list_of_user(150)
    # c = generate_list_of_user(7500)

    user_to_csv(b, "user_150.csv")


if __name__ == '__main__':
    main()
