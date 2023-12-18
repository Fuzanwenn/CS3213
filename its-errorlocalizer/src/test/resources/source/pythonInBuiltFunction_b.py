def odd_index_list(x):
    list = []
    i = 0
    while i < x:
        if i % 2 == 0:
            list.append(i)
        i = i + 1
    return list
