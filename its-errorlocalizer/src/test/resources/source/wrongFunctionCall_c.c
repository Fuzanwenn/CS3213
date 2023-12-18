#include<stdio.h>

int echoechoechoecho() {
    return 2 == 3;
}

int echoechoecho() {
    return echoechoechoecho();
}

int echoecho() {
    return echoechoecho();
}

int echo() {
    return echoecho();
}

int main() {
    printf("%d", echo());
    return 0;
}
