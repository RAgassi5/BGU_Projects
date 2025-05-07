## This file contains functions for the representation of binary trees.
## used in class Binary tree / search tree's __repr__
## Written by a former student in the course (at TAU) - thanks to Amitai Cohen
from ADTs import Stack


def printree(t, bykey=True):
    """Print a textual representation of t
    bykey=True: show keys instead of values"""
    # for row in trepr(t, bykey):
    #        print(row)
    return trepr(t, bykey)


def trepr(t, bykey=False):
    """Return a list of textual representations of the levels in t
    bykey=True: show keys instead of values"""
    if t == None:
        return ["#"]

    thistr = str(t.key) if bykey else str(t.val)

    return conc(trepr(t.left, bykey), thistr, trepr(t.right, bykey))


def conc(left, root, right):
    """Return a concatenation of textual represantations of
    a root node, its left node, and its right node
    root is a string, and left and right are lists of strings"""

    lwid = len(left[-1])
    rwid = len(right[-1])
    rootwid = len(root)

    result = [(lwid + 1) * " " + root + (rwid + 1) * " "]

    ls = leftspace(left[0])
    rs = rightspace(right[0])
    result.append("".join([ls * " ", (lwid - ls) * "_", "/",
                           rootwid * " ", "\\", rs * "_", (rwid - rs) * " "]))

    for i in range(max(len(left), len(right))):
        row = ""
        if i < len(left):
            row += left[i]
        else:
            row += lwid * " "

        row += (rootwid + 2) * " "

        if i < len(right):
            row += right[i]
        else:
            row += rwid * " "

        result.append(row)

    return result


def leftspace(row):
    """helper for conc"""
    # row is the first row of a left node
    # returns the index of where the second whitespace starts
    i = len(row) - 1
    while row[i] == " ":
        i -= 1
    return i + 1


def rightspace(row):
    """helper for conc"""
    # row is the first row of a right node
    # returns the index of where the first whitespace ends
    i = 0
    while row[i] == " ":
        i += 1
    return i


class TreeNode():
    def __init__(self, key, val):
        self.key = key
        self.val = val
        self.left = None
        self.right = None

    def __repr__(self):
        return str(self.key) + ":" + str(self.val)

    def is_Leef(self):
        return self.left is None and self.right is None


class BinaryTree():
    def __init__(self):
        self.root = None

    def __repr__(self):
        # no need to understand the implementation of this one
        out = ""
        # need printree.py file or make sure to run it in the NB
        for row in printree(self.root):
            out = out + row + "\n"
        return out

    def root_to_leaf_path_rec(self):
        """
        this function calls the recursive root to leaf function
        :return:
        """
        return self.rec_path_leaf_to_root(self.root)

    def rec_path_leaf_to_root(self, node):
        """
        this recursive function finds all the possible paths from the root to the leaves
        :param node: a TreeNode
        :return: returns a list of lists representing the possible paths
        """
        if node is None:
            return []
        if node.left is None and node.right is None:
            return [[node.key]]
        paths = []
        left_paths = self.rec_path_leaf_to_root(node.left)
        right_paths = self.rec_path_leaf_to_root(node.right)
        for path in left_paths + right_paths:
            paths.append([node.key] + path)
        return paths

    def root_to_leaf_path_iter(self):
        """
        this function checks in an iterative method the paths from the root to the leaves, using the Stack class
        provided in the ADT file
        :return: returns a list of lists representing the possible paths
        """
        if not self.root:
            return []
        stack = Stack()
        stack.push((self.root, [self.root.key]))
        paths = []
        while not stack.is_empty():
            node, path = stack.pop()
            if not node.left and not node.right:
                paths.append(path)
            if node.right:
                stack.push((node.right, path + [node.right.key]))
            if node.left:
                stack.push((node.left, path + [node.left.key]))
        return paths

    def print_all_neighbors(self, node):
        """
        this function checks for the current node's neighbors
        :param node:a TreeNode
        :return: prints out to the user the current node's neighbors
        """
        depth_stack = Stack()

        current_tree_order = self.root_to_leaf_path_iter()
        for path in current_tree_order:
            if node.val in path:
                for i in range(len(path)):
                    if path[i] == node.val:
                        break
                    else:
                        depth_stack.push(path[i])

        father_level = depth_stack.size()
        neighbors_stack = Stack()
        organization_stack = Stack()
        for new_path in current_tree_order:
            if new_path[father_level - 1] == depth_stack.peek():
                continue
            else:
                if father_level == len(new_path):
                    continue
                else:
                    neighbors_stack.push(new_path[father_level])

        for j in range(neighbors_stack.size()):
            organization_stack.push(neighbors_stack.pop())

        if organization_stack.is_empty():
            print("")
        else:
            while not organization_stack.is_empty():
                if organization_stack.size() == 1:
                    print(organization_stack.pop())
                else:
                    print(organization_stack.pop(), end=" ")

    def is_symmetric_tree(self):
        """
        this function checks if my binary tree is symmetric
        :return: returns a boolean if tree is symmetric or not
        """
        if not self.root:
            return True

        symmetry_stack = Stack()
        symmetry_stack.push((self.root.left, self.root.right))

        while not symmetry_stack.is_empty():
            left, right = symmetry_stack.pop()

            if not left and not right:
                continue

            if not left or not right:
                return False

            symmetry_stack.push((left.left, right.right))
            symmetry_stack.push((left.right, right.left))

        return True




