#include<stdio.h>

int isEven(int);
int isOdd(int);

int main() {
    int i;
    scanf("%d", &i);
    int r = isEven(i);
    printf("%d\n", r);
}

int isEven(int i) {
    int a = i < 0;
    while (a == 1) {
        return 0;
    }
    return !isOdd(i - 1);
}

int isOdd(int i) {
    int a = i < 0;
    if (a == 1) {
        return 0;
    } else {
        return !isEven(i - 1);
    }
}

