from ADTs import *
from CPUNodes import CPUNodes
from Job import Job


class JobRequestSystem():
    def __init__(self, nodes_number=5, system_capacity=30):
        self.__nodes_system_capacity = CPUNodes(system_capacity, nodes_number)
        self.__initialized_job_container = Queue()
        self.__waiting_job_container = Stack()
        self.__storage_stack = Stack()

    def request_job_resources(self, job_request):
        if isinstance(job_request, Job):
            if self.__waiting_job_container.is_empty() is True:
                if self.__nodes_system_capacity.get_available_cpu_capacity() >= job_request.get_job_requested_resources():
                    self.__nodes_system_capacity.occupy_available_cpu_capacity(
                        job_request.get_job_requested_resources())
                    self.__initialized_job_container.enqueue(job_request)
                    print(f'Requested resources for Job {job_request.get_id()} have been initialized.')
                else:
                    self.__waiting_job_container.push(job_request)
                    print(f'Requested resources for Job {job_request.get_id()} must wait for available resources.\n'
                          f'Consider terminating the current request and trying lower resources\n'
                          f'Meanwhile you will be waiting for available resources')
            else:

                if self.__waiting_job_container.peek() <= job_request and self.__nodes_system_capacity.get_available_cpu_capacity() >= job_request.get_job_requested_resources():
                    self.__nodes_system_capacity.occupy_available_cpu_capacity(
                        job_request.get_job_requested_resources())
                    self.__initialized_job_container.enqueue(job_request)
                    print(f'Requested resources for Job {job_request.get_id()} have been initialized.')

                else:
                    while True:
                        if self.__waiting_job_container.size() == 0 or self.__waiting_job_container.peek() < job_request:
                            self.__waiting_job_container.push(job_request)
                            for j in range(self.__storage_stack.size()):
                                self.__waiting_job_container.push(self.__storage_stack.pop())
                            print(
                                f'Requested resources for Job {job_request.get_id()} must wait for available resources.\n'
                                f'Consider terminating the current request and trying lower resources\n'
                                f'Meanwhile you will be waiting for available resources')
                            break
                        else:
                            self.__storage_stack.push(self.__waiting_job_container.pop())

    def terminate_job(self, job_request_id):
        for i in range(self.__waiting_job_container.size()):
            current = self.__waiting_job_container.peek().get_id()
            if self.__waiting_job_container.peek().get_id() == job_request_id:
                terminated_job = self.__waiting_job_container.pop()
                for j in range(self.__storage_stack.size()):
                    self.__waiting_job_container.push(self.__storage_stack.pop())
                return terminated_job
            else:
                self.__storage_stack.push(self.__waiting_job_container.pop())

    def execute_job(self):
        current_execution = self.__initialized_job_container.rear()
        print(f'The current job with the id: {current_execution.get_id()} is being executed....\n'
              f'The Job with the id {current_execution.get_id()} has finished executing successfully.')
        self.__nodes_system_capacity.free_occupied_cpu_capacity(current_execution.get_job_requested_resources())
        self.__initialized_job_container.dequeue()

        for i in range(self.__waiting_job_container.size()):
            if self.__nodes_system_capacity.get_available_cpu_capacity() >= self.__waiting_job_container.peek().get_job_requested_resources():
                self.request_job_resources(self.__waiting_job_container.pop())
            else:
                break

    def total_waiting_requested_job_resources(self):
        counter = 0
        for i in range(self.__waiting_job_container.size()):
            counter = counter + self.__waiting_job_container.peek().get_job_requested_resources()
            self.__storage_stack.push(self.__waiting_job_container.pop())
        for j in range(self.__storage_stack.size()):
            self.__waiting_job_container.push(self.__storage_stack.pop())
        return counter

    def request_new_job(self, job_description="", job_rank=1, job_request_resources=1):

        try:
            if job_request_resources > self.__nodes_system_capacity.get_cpu_capacity():
                raise RuntimeError

            new_job = Job(job_description, job_rank, job_request_resources)
            return new_job

        except RuntimeError:
            print(f'Job\'s requested resources: {job_request_resources} exceeds the maximal number of cpu\n'
                  f'Creating default job...')
            new_job = Job(job_description, 1, 1)
            return new_job

    def get_nodes_system_capacity(self):
        return self.__nodes_system_capacity

    def get_initialized_jobs(self):
        return self.__initialized_job_container

    def get_waiting_jobs(self):
        return self.__waiting_job_container
