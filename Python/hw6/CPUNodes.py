from ADTs import Node, LinkedList


class CPUNodes:
    def __init__(self, capacity, nodes_number):
        if type(capacity) is not int or type(nodes_number) is not int:
            raise TypeError(f'Invalid input, please enter an integer')
        if nodes_number < 1:
            raise ValueError(f'Invalid input, please enter an integer larger than 1')
        if capacity < nodes_number:
            raise ValueError(f'Invalid input, please enter an integer that is larger or equal to nodes_number')

        self.__capacity = capacity
        self.__nodes_number = nodes_number

        if self.__capacity % self.__nodes_number == 0:
            new_linked_list = LinkedList()
            for i in range(self.__nodes_number):
                new_node = Node([int(self.__capacity / self.__nodes_number), int(self.__capacity / self.__nodes_number)])
                new_linked_list.add_at_end(new_node)
            self.__cpu_nodes = new_linked_list

        else:
            new_linked_list = LinkedList()
            for i in range(self.__nodes_number):
                if i == self.__nodes_number - 1:
                    last_node = Node([int(self.__capacity // self.__nodes_number + self.__capacity % self.__nodes_number),
                                      int(self.__capacity // self.__nodes_number + self.__capacity % self.__nodes_number)])
                    new_linked_list.add_at_end(last_node)
                else:
                    new_node = Node([int(self.__capacity // self.__nodes_number), int(self.__capacity // self.__nodes_number)])
                    new_linked_list.add_at_end(new_node)
            self.__cpu_nodes = new_linked_list

    def get_cpu_capacity(self):
        return self.__capacity

    def get_available_cpu_capacity(self):
        counter = 0
        for i in range(len(self.__cpu_nodes)):
            current_node = self.__cpu_nodes[i]
            if current_node.value.value[1] == 0:
                continue
            else:
                counter = counter + current_node.value.value[1]
        return counter

    def get_cpu_nodes(self):
        return self.__cpu_nodes[0]

    def occupy_available_cpu_capacity(self, requested_capacity):
        i = 0
        j = requested_capacity
        while j > 0:
            current_node = self.__cpu_nodes[i]
            if current_node.value.value[1] > 0:
                current_node.value.value[1] -= 1
                j -= 1
            else:
                i += 1

    def free_occupied_cpu_capacity(self, freed_capacity):
        i = 0
        j = freed_capacity
        while j > 0:
            current_node = self.__cpu_nodes[i]
            if current_node.value.value[1] < current_node.value.value[0]:
                current_node.value.value[1] += 1
                j -= 1
            else:
                i += 1

    def __repr__(self):
        return repr(self.__cpu_nodes)



