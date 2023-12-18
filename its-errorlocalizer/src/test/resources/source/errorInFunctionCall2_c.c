#include <stdio.h>

int add(int x, int y);

int main() {
    int a, b;
	scanf("%d %d", &a, &b);
	printf("%d", add(a, b));
}

int add(int x, int y) {
    return x + y;
}