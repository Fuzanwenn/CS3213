int foo(int a, int b, int c) {
    return 0;
}

int main() {
    int a = 3, b = 4, c = 8;

    int i11 = a + b;
    int i21 = a - b;
    int i31 = a * b;
    int i41 = a / b;
    int i51 = a % b;
    int i12 = a + b;
    int i22 = a - b;
    int i32 = a * b;
    int i42 = a / b;
    int i52 = a % b;
    int i13 = a + c;
    int i23 = a - c;
    int i33 = a * c;
    int i43 = a / c;
    int i53 = a % c;

    double d = 3.0, e = 4.0;
    double d11 = d + e;
    double d12 = d + e;

    int t = 1, f = 0, g = 1;
    int b11 = t == f;
    int b12 = t == f;
    int b13 = t == f;
    int b21 = t != f;
    int b22 = t != f;
    int b23 = t != f;
    int b31 = t && f;
    int b32 = t && f;
    int b33 = t && f;
    int b41 = t || f;
    int b42 = t || f;
    int b43 = t || f;
    int b51 = t < f;
    int b52 = t < f;
    int b53 = t < f;
    int b61 = t > f;
    int b62 = t > f;
    int b63 = t > f;
    int b71 = t >= f;
    int b72 = t >= f;
    int b73 = t >= f;
    int b81 = t <= f;
    int b82 = t <= f;
    int b83 = t <= f;

    int op11[3] = {a, b, c};
    int op12[3] = {a, b, c};
    int op13[3] = {a, b, c};
    int op21 = foo(a, b, c);
    int op22 = foo(a, b, c);
    int op23 = foo(a, b, c);
    int op24 = foo(a, b, c);
}
