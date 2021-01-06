import generate_user as user_generator
import generate_hotspot as hotspot_generator
import generate_log as log_generator

import datetime
import pandas as pd
import argparse


HOTSPOT_FILENAME = 'hotspot.csv'


def data_to_csv(list, filename, headers):
    data = []
    for x in list:
        data.append(x.to_list())

    df = pd.DataFrame(data, columns=headers)
    df.to_csv(filename, index=False, header=True)


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
    number_of_user = 150

    parser = argparse.ArgumentParser()

    parser.add_argument('-su', "--setuserpath", dest='setuserpath', help="Set path to users file")
    parser.add_argument('-sh', "--sethotpath", dest='sethotpath', help="Set path to hotspot file")
    parser.add_argument('-st', "--settracepath", dest='settracepath', help="Set trace to hotspot file")
    parser.add_argument("-u", "--users", dest='users', help="Number of users to generate", type=int)
    parser.add_argument("-hs", "--hotspots",  help="Generate hotspots", action='store_true', default=False)
    parser.add_argument("-l", "--load",  help="Load users and hotspots from file", action='store_true', default=False)
    parser.add_argument("-e", "--execute",  help="Execute trace generator", action='store_true', default=False)

    args = parser.parse_args()

    hotspots = []
    users = []

    if args.setuserpath is not None:
        print("Path user set !")
        users_filename = str(args.setuserpath)
    else:
        users_filename = "user_" + ".csv"

    if args.sethotpath is not None:
        print("Path hotspot set !")
        hotspots_filename = str(args.sethotpath)
    else:
        hotspots_filename = "hotspot" + ".csv"

    if args.settracepath is not None:
        print("Path trace set !")
        traces_filename = str(args.settracepath)
    else:
        traces_filename = "log_" + str(number_of_user)

    if args.hotspots:
        print("Hotspots generating...")
        hotspot_generator.generateHotspotPoint(51.74732490481434, 19.453798422308278, hotspots_filename)
        hotspots = hotspot_generator.return_list_of_hot_spots(hotspots_filename)

    if args.users is not None:
        print("Users generating...")
        number_of_user = args.users
        users = user_generator.generate_list_of_user(args.users)
        data_to_csv(users, filename=users_filename, headers=["Id", "PhoneNumber", "Profile"])

    if args.load:
        print("Loading...")
        users = user_generator.generate_list_of_user(number_of_user)
        hotspots = hotspot_generator.return_list_of_hot_spots(hotspots_filename)
        print("Done")

    if args.execute:
        if hotspots and users:
            logs = log_generator.generate_log_files(number_of_user, hotspots, start_date, end_date)

            traces_to_csv(logs, filename=traces_filename + ".csv",
                          headers=["UserId", "PoisName", "EnterTime", "ExitTime"])

            for log in logs:
                log.count_duration()

            traces_to_csv(logs, filename=traces_filename+"_duration.csv",
                          headers=["UserId", "PoisName", "EnterTime", "ExitTime", "Duration"], duration=True)
        else:
            print("There is no hotspots or user data ...")


if __name__ == '__main__':
    main()


