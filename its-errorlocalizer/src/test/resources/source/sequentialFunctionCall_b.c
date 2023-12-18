#include <stdio.h>

int echo();
int echoEcho();
int echoEchoEcho();

int main() {
    printf("%d", echoEcho());
    return 0;
}

int echo() {
    return echoEcho();
}

int echoEcho() {
    return echoEchoEcho();
}

int echoEchoEcho() {
    return 2;
}