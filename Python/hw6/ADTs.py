class Node:
    def __init__(self, val):
        self.value = val
        self.next = None

    def __repr__(self):
        return str(self.value)  # "[" + str(self.value) + ", nxt:" + str(id(self.next)) + "]"
    # This shows pointers as well for educational purposes


class LinkedList:
    def __init__(self):
        self.head = None
        self.len = 0

    def add_at_start(self, val):
        """ add node with value val at the list head """
        new_node = Node(val)
        if self.len == 0:
            self.head = new_node
        else:
            tmp = self.head
            self.head = new_node
            self.head.next = tmp
        self.len += 1

    def insert(self, loc, val):
        """ add node with value val at location 0<=loc<len of the list """
        new_node = Node(val)
        if loc == 0:
            new_node.next = self.head
            self.head = new_node
        else:
            p = self.head
            for i in range(1, loc):
                p = p.next
            tmp = p.next
            p.next = new_node
            p.next.next = tmp
        self.len += 1

    def delete(self, loc):
        """ delete element at location 0<=loc<len """
        if loc == 0:
            self.head = self.head.next
        else:
            p = self.head
            for i in range(1, loc):
                p = p.next
            if p.next is not None:  # p cannot be None
                p.next = p.next.next
        self.len -= 1

    def add_at_end(self, val):
        """ add node with value val at the list tail """
        new_node = Node(val)
        if self.len == 0:
            self.head = new_node
        else:
            p = self.head
            while p.next is not None:
                p = p.next
            p.next = new_node
        self.len += 1

    def __len__(self):
        """ called when using Python's len() """
        return self.len

    def __getitem__(self, loc):
        """ called when using L[i] for reading
            return node at location 0<=loc<len """
        p = self.head
        for i in range(0, loc):
            p = p.next
        return p

    def __setitem__(self, loc, val):
        """ called when using L[loc]=val for writing
            assigns val to node at location 0<=loc<len """
        p = self.head
        for i in range(0, loc):
            p = p.next
        p.value = val
        return None

    def __repr__(self):
        out = ""
        p = self.head
        while p is not None:
            if p.next is not None:
                out += str(p) + " " + "->"
            else:
                out += str(p) + " "
            p = p.next

        return out  # + "None"

    def __str__(self):
        out = ""
        p = self.head
        while p is not None:
            if p.next != None:
                out += str(p)
            else:
                out += str(p)
            p = p.next
        return out  # + "None"

    def __iter__(self):
        self.runner = self.head
        return self

    def __next__(self):
        if self.runner is None:
            raise StopIteration
        res = self.runner
        self.runner = self.runner.next
        return res

class Stack:
    def __init__(self):
        self.__items = LinkedList()

    def push(self, item):
        self.__items.add_at_start(item)

    def pop(self):
        if self.is_empty():
            raise RuntimeError("Queue is empty")
        res = self.__items[0]
        self.__items.delete(0)
        return res.value

    def peek(self):
        return self.__items[0].value

    def is_empty(self):
        return len(self.__items) == 0

    def size(self):
        return len(self.__items)

    def __repr__(self):
        out = 'top->'
        count = 0
        head = self.__items[0]
        while count < self.size():
            out += str(head.value) + ' | \n'
            out += "_"*len(str(head.value)) + "\n"
            count += 1
            head = self.__items[count]
        out += '|'
        return out

        # return "(Stack, " + repr(self.__items) + ")"


class Queue():
    def __init__(self):
        self.head = None
        self.tail = None
        self.len = 0

    def __repr__(self):
        out = "FirstIn <- "
        p = self.head
        while p != None:
            out += str(p) + " | \n"
            p = p.next
        return out + "<-LastIn"

    def rear(self):
        if self.len == 0:
            raise RuntimeError("Queue is empty")

        p = self
        ret_val = p.head.value
        return ret_val

    def front(self):
        if self.len == 0:
            raise RuntimeError("Queue is empty")
        p = self
        front_val = p.tail.value
        return front_val

    def enqueue(self, val):
        ''' add node with value val at the list tail '''
        p = self
        n = Node(val)
        if self.len == 0:
            p.head = p.tail = Node(val)
        else:
            p.tail.next = Node(val)
            p.tail = p.tail.next
        self.len += 1

    def dequeue(self):
        ''' add node with value val at the list head '''
        if self.len == 0:
            raise RuntimeError("Queue is empty")

        p = self
        ret_val = p.head.value
        p.head = p.head.next
        self.len -= 1

        return ret_val

    def __len__(self):
        ''' called when using Python's len() '''
        return self.len



if __name__ == '__main__':
    q = Queue()
    q.enqueue("hi")
    q.enqueue("hello")
    q.enqueue("roii")
    q.enqueue("ido")
    print(q)
    q.dequeue()
    print(q)
