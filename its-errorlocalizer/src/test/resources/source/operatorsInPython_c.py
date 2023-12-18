def foo(a, b, c):
    return 0

def is_member(value, iterable):
     for item in iterable:
         if value is item or value == item:
             return True
     return False

def main():
    a = 3
    b = 4
    c = 8

    i11 = a + b
    i21 = a - b
    i31 = a * b
    i41 = a / b
    i51 = a % b
    i12 = a + b
    i22 = a - b
    i32 = a * b
    i42 = a / b
    i52 = a % b
    i13 = a + c
    i23 = a - c
    i33 = a * c
    i43 = a / c
    i53 = a % c

    d = 3.0
    e = 4.0
    d11 = d + e
    d12 = int(d + e)

    t = 1
    f = 0
    g = 1
    b11 = (t == f)
    b12 = (t == f)
    b13 = (t == f)
    b21 = (t != f)
    b22 = (t != f)
    b23 = (t != f)
    b31 = (t and f)
    b32 = (t and f)
    b33 = (t and f)
    b41 = (t or f)
    b42 = (t or f)
    b43 = (t or f)
    b51 = (t < f)
    b52 = (t < f)
    b53 = (t < f)
    b61 = (t > f)
    b62 = (t > f)
    b63 = (t > f)
    b71 = (t >= f)
    b72 = (t >= f)
    b73 = (t >= f)
    b81 = (t <= f)
    b82 = (t <= f)
    b83 = (t <= f)

    op11 = [a, b, c]
    op12 = [a, b, c]
    op13 = [a, b, c]
    op21 = foo(a, b, c)
    op22 = foo(a, b, c)
    op23 = foo(a, b, c)
    op24 = foo(a, b, c)

    l11 = [1, 2, 3]
    l11.insert(1, 4)
    l12 = [4, 5, 6]
    l12.insert(2, 6)

    list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    i55 = is_member(5, list)
    i56 = is_member(11, list)
    i57 = is_member(2, list)
    i58 = is_member(-2, list)

    r11 = 2 << 5
    r12 = 2 >> 5
    r21 = 3 << 4
    r22 = 1000 >> 3

    bit11 = 2 & 3
    bit12 = 2 | 3
    bit13 = 2 ^ 3
    bit21 = 1 & 4
    bit22 = 2 | 4
    bit23 = 3 ^ 4

    a11 = a is not b
    a12 = a is b
    a21 = a is c
    a22 = a is not c