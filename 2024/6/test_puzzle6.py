import puzzle6 as sut


def test_visited():
    visited, _ = sut.simulate(*sut.parse_map("6/example.txt"))
    actual = len(visited)
    assert actual == 41


def test_loops():
    _, new_obstacles = sut.simulate(*sut.parse_map("6/example.txt"))
    assert new_obstacles == 6
