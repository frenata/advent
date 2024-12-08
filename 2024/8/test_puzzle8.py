import puzzle8 as sut


def test_count_antinodes():
    actual = sut.count_antinodes("8/example.txt", range(1,2))
    assert actual == 14


def test_count_antinodes_repeat():
    actual = sut.count_antinodes("8/example.txt", range(0,99))
    assert actual == 34
