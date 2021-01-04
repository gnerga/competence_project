import random
import datetime
import pandas as pd
from models.User import User
from models.HotSpot import HotSpot
from models.Log import Log
from models.RandHour import RandHour

NUMBER_OF_USERS = 15000


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


def data_to_csv(list, filename, headers):
    data = []
    for x in list:
        data.append(x.to_list())

    df = pd.DataFrame(data, columns=headers)
    df.to_csv(filename, index=False, header=True)


def get_init_rand_hour():
    university_open_hour = 0
    university_close_hour = 15

    h = random.randint(0, 8)
    m = random.randint(0, 59)
    s = random.randint(0, 59)
    ms = random.randint(0, 1000000)

    return RandHour(h, m, s, ms)


def generate_log_files(number_of_users, hotspots, start_date, end_date, number_of_visited_spots=10):
    list_of_logs = []
    n_days = (end_date - start_date).days
    list_of_days = [start_date]

    for day in range(0, n_days, 1):
        list_of_days.append(list_of_days[day] + datetime.timedelta(days=1))

    for day in list_of_days:
        dt = get_init_rand_hour()
        day = day + datetime.timedelta(
            hours=dt.get_hour(),
            minutes=dt.get_minute(),
            seconds=dt.get_second(),
            microseconds=dt.get_microsecond()
        )
        print(day)

    return list_of_logs


def main():
    start_date = datetime.datetime(year=2020, month=10, day=1, hour=7, minute=0, second=0, microsecond=0)
    end_date = datetime.datetime(year=2020, month=12, day=1, hour=22, minute=0, second=0, microsecond=0)
    hotspots = return_list_of_hot_spots('hotspot.csv')
    users = generate_list_of_user(NUMBER_OF_USERS)
    log = generate_log_files(NUMBER_OF_USERS, hotspots, start_date, end_date)

    # data_to_csv(users, filename="user_"+str(NUMBER_OF_USERS)+".csv", headers=["Id", "PhoneNumber", "Profile"])
    # data_to_csv(log, filename="log_150.csv", headers=["UserId", "PoisName", "EnterTime", "ExitTime"])


if __name__ == '__main__':
    main()
