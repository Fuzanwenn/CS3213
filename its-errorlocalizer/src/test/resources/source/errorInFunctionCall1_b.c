#include <stdio.h>

int factorial(int x);

int main() {
    int a, b;
	scanf("%d %d", &a, &b);
	if (a > b) {
	    printf("%d", factorial(a));
	} else {
	    printf("%d", 0);
	}
}

int factorial(int x) {
    if (x == 0) {
        return 1;
    } else {
        return x + factorial(x - 1);
    }
}