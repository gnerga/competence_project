import random
import datetime
import isodate
import numpy as np
from models.Traces import Traces


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
