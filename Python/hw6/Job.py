import copy
from functools import total_ordering


@total_ordering
class Job:
    last_job_id = 0

    def __init__(self, job_description, job_rank, job_request_resources):

        if type(job_description) is not str:
            raise TypeError(f'Invalid input, please enter a string')
        if type(job_rank) is not int or type(job_request_resources) is not int:
            raise TypeError(f'Invalid input, please enter an integer')
        if job_rank < 1 or job_request_resources < 1:
            raise ValueError(f'Invalid input, please enter an integer larger than 1')

        self.__job_id = Job.last_job_id
        Job.last_job_id += 1
        self.__description = job_description
        self.__rank = job_rank
        self.__job_requested_resources = job_request_resources

    def get_rank(self):
        return self.__rank

    def get_description(self):
        return self.__description

    def get_id(self):
        return self.__job_id

    def set_description(self, new_description):
        if type(new_description) is not int:
            raise TypeError
        self.__description = new_description

    def set_rank(self, new_rank):
        if type(new_rank) is not int:
            raise TypeError
        if new_rank < 1:
            raise ValueError
        self.__rank = new_rank

    def set_job_requested_resources(self, new_job_requested_resources):
        if type(new_job_requested_resources) is not int:
            raise TypeError
        if new_job_requested_resources < 1:
            raise ValueError
        self.__job_requested_resources = new_job_requested_resources

    def get_job_requested_resources(self):
        return self.__job_requested_resources

    def __repr__(self):
        return f'ID: {self.__job_id}, Rank: {self.__rank}, Resources: {self.__job_requested_resources}'

    def __eq__(self, other):
        return self.__rank == other.__rank

    def __lt__(self, other):
        return self.__rank < other.__rank


