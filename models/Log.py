class Log:

    def __init__(self, id, pois_name , user_id, enter_time, exit_time, pois_id):
        self.id = id
        self.pois_name = pois_name
        self.user_id = user_id
        self.enter_time = enter_time
        self.exit_time = exit_time
        self.duration = 0
        self.pois_id = pois_id

    def get_pois_id(self):
        return self.pois_id

    def get_id(self):
        return self.id

    def get_user_id(self):
        return self.user_id

    def get_enter_time(self):
        return self.enter_time

    def get_exit_time(self):
        return self.exit_time

    def get_duration(self):
        return self.duration

    def count_duration(self):
        self.duration = (self.exit_time - self.enter_time)

    def to_list_without_duration(self):
        return [self.user_id, self.pois_name, self.enter_time, self.exit_time]

    def to_list(self):
        return [self.user_id, self.pois_name, self.enter_time, self.exit_time, self.duration]