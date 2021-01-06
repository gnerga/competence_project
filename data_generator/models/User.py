class User:

    def __init__(self, user_id, phone_number, profile):
        self.user_id = user_id
        self.phone_number = phone_number
        self.profile = profile

    def get_id(self):
        return self.user_id

    def get_phone_number(self):
        return self.phone_number

    def profile(self):
        return self.profile

    def to_string(self):
        print(self.user_id)
        print(self.phone_number)
        print(self.profile)

    def to_list(self):
        return [self.user_id, self.phone_number, self.profile]