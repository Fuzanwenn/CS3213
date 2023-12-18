#include<stdio.h>

int echoechoechoecho() {
    return 3 == 2;
}

int echoechoecho() {
    return echoechoechoecho();
}

int echo() {
    return echoechoecho();
}

int echoecho() {
    return echo();
}

int main() {
    printf("%d", echoecho());
    return 0;
}

