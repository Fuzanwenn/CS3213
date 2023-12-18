#include<stdio.h>
int main()
{
  int i,j;
  int N;
  scanf("%d",&N);
  for(i=1;i<=N;i++)
  {
    for(j=N;j>=1;j--)
    {
      if(i==j)
        printf("*");
      else
        printf("%d",j);
    }
    printf("\n");
  }
  return 0;
}