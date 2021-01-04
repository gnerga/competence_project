class HotSpot:

    def __init__(self, pois_id, pois_name, pois_description, longitude, latitude, spot_type):
        self.pois_id = pois_id
        self.pois_name = pois_name
        self.pois_description = pois_description
        self.longitude = longitude
        self.latitude = latitude
        self.spot_type = spot_type

    def get_id(self):
        return self.pois_id

    def get_name(self):
        return self.pois_name

    def get_description(self):
        return self.pois_description

    def get_longitude(self):
        return self.longitude

    def get_latitude(self):
        return self.latitude

    def get_spot_type(self):
        return self.spot_type