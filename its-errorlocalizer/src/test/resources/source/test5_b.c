#include<stdio.h>

int main(){
	float a,b;
	scanf("%f %f",&a,&b);
	float area=(1.0/2)*a*b;
	if (area<0) 
        area=-area;
	printf("The area is %.4f",area);
	return 0;
}
