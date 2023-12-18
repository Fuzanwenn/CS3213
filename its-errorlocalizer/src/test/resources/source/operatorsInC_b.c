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
    int i12 = b + a;
    int i22 = b - a;
    int i32 = b * a;
    int i42 = b / a;
    int i52 = b % a;
    int i13 = b + c;
    int i23 = b - c;
    int i33 = b * c;
    int i43 = b / c;
    int i53 = b % c;

    double d = 3.0, e = 4.0;
    double d11 = d + e;
    float d12 = d + e;

    int t = 1, f = 0, g = 1;
    int b11 = t == f;
    int b12 = f == t;
    int b13 = t == g;
    int b21 = t != f;
    int b22 = f != t;
    int b23 = t != g;
    int b31 = t && f;
    int b32 = f && t;
    int b33 = t && g;
    int b41 = t || f;
    int b42 = f || t;
    int b43 = t || g;
    int b51 = t < f;
    int b52 = f > t;
    int b53 = t < g;
    int b61 = t > f;
    int b62 = f < t;
    int b63 = t > g;
    int b71 = t >= f;
    int b72 = f <= t;
    int b73 = t >= g;
    int b81 = t <= f;
    int b82 = f >= t;
    int b83 = t <= g;

    int op11[3] = {a, b, c};
    int op12[3] = {a, c, b};
    int op13[4] = {a, b, c};
    int op21 = foo(a, b, c);
    int op22 = foo(a, c, b);
    int op23 = foo(c, a, b);
    int op24 = foo(a, b, t);
}