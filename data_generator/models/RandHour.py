class RandHour:
    def __init__(self, hour, minute, second, microsecond):
        self.hour = hour
        self.minute = minute
        self.second = second
        self.microsecond = microsecond

    def get_hour(self):
        return self.hour

    def get_minute(self):
        return self.minute

    def get_second(self):
        return self.second

    def get_microsecond(self):
        return self.microsecond
