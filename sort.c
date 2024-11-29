#include <stdio.h>
#include <math.h>


void insertion(int res[],int digits[], int vLeft[], int nLeft, int vRight[], int nRight) {
  int idxRes = 0;
  int iLeft = 0;
  int iRight = 0; 

  while (iLeft < nLeft && iRight < nRight) {
    if (digits[iLeft] <= digits[nLeft+iRight]) {

      res[idxRes] = vLeft[iLeft];
      iLeft++;

    } else {
      res[idxRes] = vRight[iRight];
      iRight++;
    }
    idxRes++;
  }
  while (iLeft < nLeft) {
    res[idxRes] = vLeft[iLeft];
    iLeft++;
    idxRes++;
  }
  while (iRight < nRight) {
    res[idxRes] = vRight[iRight];
    iRight++;
    idxRes++;
  }
}

void insertion_sort(int numbers[],int digits[], int n) {
  if (n < 2)
    return;
  int middle = n / 2;
  int nLeft = middle;
  int nRight = n - middle;
  int vLeft[nLeft];
  int vRight[nRight];
  for (int i = 0; i < nLeft; i++) {
    vLeft[i] = numbers[i];

  }
  for (int i = 0; i < nRight; i++) {
    vRight[i] = numbers[middle + i];

  }
  insertion_sort(vLeft, digits, nLeft);
  insertion_sort(vRight,digits, nRight);
  insertion(numbers,digits, vLeft, nLeft, vRight, nRight);
}

void radix_sort(int numbers[], int size, int digits){
  int digits_read = 0, i;
  int digits_to_order[size];
  while(digits_read < digits){
    for(i = 0; i < size; i++){
      digits_to_order[i] = numbers[i]/(int)pow(10, digits_read) % 10;
    }
    insertion_sort(numbers,digits_to_order, size);
    ++digits_read;
  }
}

int main(void) {
  int numbers[] = {124,357,789,324};
  int digits = 3;
  int size = 4;
  radix_sort(numbers, size, digits);
  printf("Vetor ordenado: ");
  for(int i = 0; i< size; i++){
    printf("%d ", numbers[i]);
  }
  return 0;
}
