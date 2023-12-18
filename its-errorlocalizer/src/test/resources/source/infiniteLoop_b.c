#include<stdio.h>

int isEven(int);
int isOdd(int);

int main() {
    int z;
    scanf("%d", &z);
    int a = isEven(z);
    printf("%d\n", a);
}

int isEven(int x) {
    int c = 0 > x;
    while (c == 0) {
        return 0;
    }
    return !isOdd(x - 1);
}

int isOdd(int y) {
    int c = y < 0;
    if (c == 1) {
        return 0;
    }
    return !isEven(y - 2);
}
