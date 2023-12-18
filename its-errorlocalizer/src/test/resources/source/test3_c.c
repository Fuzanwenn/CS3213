#include <stdio.h>
int main()
{
  int n , m = 0;
  int old;
  scanf("%d", &n);

   old = n ;

  while(n > 0)
  {
  	  m = m*10;
  	  m = m + n%10;
      n/=10;             /* n=n/10 */
  }
  printf("Reverse of %d is %d", old, m);
}