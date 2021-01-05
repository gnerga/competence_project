import random
import isodate
import datetime
import pandas as pd
import numpy as np
from models.User import User
from models.HotSpot import HotSpot
from models.Traces import Traces
from models.RandHour import RandHour

NUMBER_OF_USERS = 15000


def return_list_of_hot_spots(file):
    list_of_hot_spots = []
    df = pd.read_csv(file, delimiter=';', header=None)

    for index, row in df.iterrows():
        list_of_hot_spots.append(HotSpot(index+1, row[0], row[1], row[2], row[3], row[4]))
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


def get_init_rand_deltatime():

    h = random.randint(0, 8)
    m = random.randint(0, 59)
    s = random.randint(0, 59)

    return datetime.timedelta(
        hours=h,
        minutes=m,
        seconds=s,
        # microseconds=ms
    )


def get_rand_deltatime():

    a = random.randint(0, 2)

    if a == 0:
        m = random.randint(0, 60)
    if a == 1:
        m = random.randint(0, 120)
    if a == 2:
        m = random.randint(0, 180)

    s = random.randint(0, 59)

    # ms = random.randint(0, 1000000)

    return datetime.timedelta(
        minutes=m,
        seconds=s,
        # microseconds=ms
    )

    # return RandHour(hour=null, minute=m, second=s, microsecond=ms)


def choose_spot(hotspots):

    test = np.random.exponential(0.5, size=len(hotspots))
    x = max(test)
    index = -1
    for i in range(0, len(test)):
        if x == test[i]:
            index = i

    return hotspots[index]


def generate_log_files(number_of_users, hotspots, start_date, end_date, number_of_visited_spots=10):

    if number_of_visited_spots < 1:
        number_of_visited_spots = 1

    log_init_id = 1

    list_of_logs = []
    n_days = (end_date - start_date).days
    list_of_days = [start_date]

    for day in range(0, n_days, 1):
        list_of_days.append(list_of_days[day] + datetime.timedelta(days=1))

    for userid in range(1, number_of_users+1, 1):
        personal_user_day = []

        for day in list_of_days:
            dt = get_init_rand_deltatime()
            personal_user_day.append(day + dt)
            #                          datetime.timedelta(
            #     hours=dt.get_hour(),
            #     minutes=dt.get_minute(),
            #     seconds=dt.get_second(),
            #     microseconds=dt.get_microsecond()
            # ))

        for day in personal_user_day:
            enter = day
            visits_in_spot = random.randint(1, number_of_visited_spots)

            for visit in range(0, visits_in_spot, 1):
                spot = choose_spot(hotspots)
                exit = enter + get_rand_deltatime()
                log = Traces(log_init_id, spot.get_name(), userid, enter, exit, spot.get_id())
                list_of_logs.append(log)
                enter = exit + get_rand_deltatime()

    return list_of_logs


def traces_to_csv(logs, filename, headers, duration=False):
    data = []
    for x in logs:
        x.format_enter_and_exit_time_and_duration_to_iso_format()
        if duration:
            data.append(x.to_list())
        else:
            data.append(x.to_list_without_duration())

    df = pd.DataFrame(data, columns=headers)
    df.to_csv(filename, index=False, header=True)


def main():
    start_date = datetime.datetime(year=2020, month=10, day=1, hour=7, minute=0, second=0)
    end_date = datetime.datetime(year=2020, month=12, day=1, hour=22, minute=0, second=0)
    # print(start_date)
    # a = end_date - start_date
    # print(isodate.duration_isoformat(end_date - start_date))
    # print(start_date.isoformat())
    hotspots = return_list_of_hot_spots('hotspot.csv')

    users = generate_list_of_user(NUMBER_OF_USERS)
    logs = generate_log_files(NUMBER_OF_USERS, hotspots, start_date, end_date)

    data_to_csv(users, filename="user_"+str(NUMBER_OF_USERS)+".csv", headers=["Id", "PhoneNumber", "Profile"])
    traces_to_csv(logs, filename="log_"+str(NUMBER_OF_USERS)+".csv", headers=["UserId", "PoisName", "EnterTime", "ExitTime"])

    for log in logs:
        log.count_duration()

    traces_to_csv(logs, filename="log_"+str(NUMBER_OF_USERS)+"_duration.csv", headers=["UserId", "PoisName", "EnterTime", "ExitTime","Duration"], duration=True)



if __name__ == '__main__':
    main()
