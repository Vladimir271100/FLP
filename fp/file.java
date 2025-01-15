//Пример использования блочного лямбда-выражения

interface NumericFunc {
	int func(int n);
}
class BlockLambda {
	public static void main(String[] args) {
		//Возвращает наименьший положительный делитель для значения
		NumericFunc smallestF = (n) -> {
			int result = 1;

			//Абсолютное значение n
			n = n<0 ? -n : n;
		       for (int i=2; i<=n/i; i++)
		       		if ((n%i) == 0) {
			 		result = i;
					break;
				}
       			return result;
 		};
		System.out.println("Наименьший делитель: " +smallestF.func(12));
	 	System.out.println("Наименьший делитель: " +smallestF.func(11));
		
	}
}	
