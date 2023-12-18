#include<stdio.h>

int main(){
    int a,b,c=0,e;
    scanf("%d",&a);
    printf("%d ",a);
    while(a>0){
        b=a%10;
        c=c*10+b;
        a=a/10;
    }
    printf("Reverse of %d is %d",e,c);
    return 0;
}