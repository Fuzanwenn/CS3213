#include <stdio.h>

int main() {
    int a, b;
    scanf("%d %d", &a, &b);
    if (a <= b) {
        printf("a <= b");
        while (a <= b) {
            a = a + 1;
            b = b - 1;
        }
    } else {
        printf("a > b");
        while (a > b) {
            a = a - 1;
            b = b + 1;
        }
    }
    return 0;
}