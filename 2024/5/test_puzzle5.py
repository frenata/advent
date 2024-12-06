import puzzle5 as sut


def test_order():
    rules, pages = sut.extract("5/example.txt")
    ordered = sut.middles([page for page in pages if sut.is_ordered(rules, page)])
    unordered = sut.middles(
        [sut.order(rules, page) for page in pages if not sut.is_ordered(rules, page)]
    )
    assert ordered == 143
    assert unordered == 123
