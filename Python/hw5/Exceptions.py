class InvalidInputException(Exception):
    pass


class NotAvailablePlace(Exception):
    pass


class TooSmallAquariumSize(Exception):
    pass


class InvalidAnimalType(Exception):
    def __init__(self, animal):
        super().__init__()
        self.animal = animal

    def __str__(self):
        return f'Error: "{self.animal}" is an invalid animal type. The valid types are: molly, scalar, ocypode, shrimp'

