graph = {
    "A": ["B", "C", "D"],
    "B": ["A", "G"],
    "C": ["A", "F"],
    "D": ["A", "E", "F"],
    "E": ["D", "F", "G"],
    "F": ["C", "D", "E"],
    "G": ["B", "H"],
    "H": ["E", "G"],
}
weighted_graph = {
    "E": {"F": 3, "G": 5, "H": 9},
    "F": {"E": 3, "K": 2, "L": 8},
    "G": {"E": 5, "H": 6, "K": 2, "L": 1},
    "H": {"E": 9, "G": 6, "L": 10},
    "K": {"F": 2, "G": 2},
    "L": {"F": 8, "G": 1, "H": 10},
}

def BFS(start_node, goal, graph):
    frontier = [start_node]
    explored = []
    while len(frontier) > 0:
        if not frontier:
            return "Fail: Goal not found"
        current_node = frontier.pop(0)
        if current_node == goal:
            return f"Success! Pasth explored to goal: {explored + [current_node]}"
        explored.append(current_node)
        childnodes = graph[current_node]
        for x in childnodes:
            if x not in explored and x not in frontier:
                frontier.append(x)
                return "Fail: Goal not found"
            result = BFS("A", "H", graph)
            print(result)
