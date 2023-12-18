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
    i12 = b + a
    i22 = b - a
    i32 = b * a
    i42 = b / a
    i52 = b % a
    i13 = b + c
    i23 = b - c
    i33 = b * c
    i43 = b / c
    i53 = b % c

    d = 3.0
    e = 4.0
    d11 = d + e
    d12 = float(d + e)

    t = 1
    f = 0
    g = 1
    b11 = (t == f)
    b12 = (f == t)
    b13 = (t == g)
    b21 = (t != f)
    b22 = (f != t)
    b23 = (t != g)
    b31 = (t and f)
    b32 = (f and t)
    b33 = (t and g)
    b41 = (t or f)
    b42 = (f or t)
    b43 = (t or g)
    b51 = (t < f)
    b52 = (f > t)
    b53 = (t < g)
    b61 = (t > f)
    b62 = (f < t)
    b63 = (t > g)
    b71 = (t >= f)
    b72 = (f <= t)
    b73 = (t >= g)
    b81 = (t <= f)
    b82 = (f >= t)
    b83 = (t <= g)

    op11 = [a, b, c]
    op12 = [a, c, b]
    op13 = [a, b, c, None]
    op21 = foo(a, b, c)
    op22 = foo(a, c, b)
    op23 = foo(c, a, b)
    op24 = foo(a, b, t)

    l11 = [1, 2, 3]
    l11.insert(1, 4)
    l12 = [4, 5, 6]
    l12.insert(2, 7)

    list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    i55 = is_member(5, list)
    i56 = is_member(11, list)
    i57 = is_member(1, list)
    i58 = is_member(-1, list)

    r11 = 2 << 5
    r12 = 2 >> 5
    r21 = 3 << 5
    r22 = 1000 >> 2

    bit11 = 2 & 3
    bit12 = 2 | 3
    bit13 = 2 ^ 3
    bit21 = 0 & 4
    bit22 = 0 | 4
    bit23 = 0 ^ 4

    a11 = a is b
    a12 = a is not b
    a21 = a is c
    a22 = a is not c
