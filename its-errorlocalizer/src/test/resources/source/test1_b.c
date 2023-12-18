#include<stdio.h>

int main(){
    int N,w,h;
    scanf("%d%d%d",&N,&w,&h);
    int a,b;
    for(b=1;b<=h;b++){
        for(a=1;a<=w;a++){
            if(a==1){
                printf("%d",N);
            }else if(a==w){
                printf("%d",N);
                printf("\n");
            }else{
                printf(" ");
            }
        }
    }
    return 0;
}