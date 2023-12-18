#include <stdio.h>

int main() {
    int a, b;
    scanf("%d %d", &a, &b);
    if (b >= a) {
        printf("a <= b");
        while (b > a) {
            a = a + 1;
            b = b - 1;
        }
    } else {
        printf("a > b");
        while (b < a) {
            a = a - 1;
            b = b + 1;
        }
    }
    return 0;
}