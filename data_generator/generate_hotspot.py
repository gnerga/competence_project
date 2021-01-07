import math
import random
import pandas as pd
import csv
from models.Point import Point
from models.HotSpot import HotSpot

#CDF  cumulative distribution function ( number > 0)
def reverseCDF (number):
    return math.pow(math.log(1 - number), 2)


def generateHotspotPoint(centerLat, centerLong, filename):
   
    numberOfPoints = 10
    centerLat = centerLat * math.pi / 180.0
    centerLong = centerLong * math.pi / 180.0
    earthR = 6371000
    points = list()

    for i in range(numberOfPoints):
        functionArgument = 1.0 / (numberOfPoints + 1) * (i + 1)
        angleRandom = (random.random() * 2 - 1.0) * 2 * math.pi
        randomDistance = reverseCDF(functionArgument) * 1000
        latitude = math.asin((math.sin(centerLat) * math.cos(randomDistance / earthR)) + (math.cos(centerLat) * math.sin(randomDistance / earthR)*math.cos(angleRandom))) * 180 / math.pi
        longitude =  (centerLong  + math.atan2(math.sin(angleRandom) * math.sin(randomDistance / earthR) * math.cos(centerLat),math.cos(randomDistance / earthR) - (math.sin(centerLat) * math.sin(latitude)))) * 180 / math.pi
        points.append(Point(latitude,longitude))
                                    


    with open(filename, mode='w') as hotspot_file:
        hotspot_writer = csv.writer(hotspot_file, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        hotspot_writer.writerow(['Cafeteria','Place for rest between lectures',str(points[0].x),str(points[0].y),'indoor'])
        hotspot_writer.writerow(['Main Library','The biggest library on TUL',str(points[1].x),str(points[1].y),'indoor'])
        hotspot_writer.writerow(['Park','Good place for rest brain before exams',str(points[2].x),str(points[2].y),'outdoor'])
        hotspot_writer.writerow(['Finestra Pub','Pizzeria which is a good friend of the TUL',str(points[3].x),str(points[3].y),'indoor'])
        hotspot_writer.writerow(['Statue before BAIS Faculty','Statue presenting hand',str(points[4].x),str(points[4].y),'outdoor'])
        hotspot_writer.writerow(['Bridge','Bridge between BAIS and CTI buildings',str(points[5].x),str(points[5].y),'outdoor'])
        hotspot_writer.writerow(['Chinese Bar','Place with cheap dinners',str(points[6].x),str(points[6].y),'indoor'])
        hotspot_writer.writerow(['Information Technology Center','Place where most od TUL servers are hosted',str(points[7].x),str(points[7].y),'indoor'])
        hotspot_writer.writerow(['Deans office','Place where students are submitting various applications',str(points[8].x),str(points[8].y),'indoor'])
        hotspot_writer.writerow(['Sports bay','Place where most sport activities have place',str(points[9].x),str(points[9].y),'indoor'])


def return_list_of_hot_spots(file):
    list_of_hot_spots = []
    df = pd.read_csv(file, delimiter=',', header=None)

    for index, row in df.iterrows():
        list_of_hot_spots.append(HotSpot(index+1, row[0], row[1], row[2], row[3], row[4]))
        index = index + 1
    return list_of_hot_spots

#     generateHotspotPoint(51.74732490481434, 19.453798422308278, 'hotspot.csv')




