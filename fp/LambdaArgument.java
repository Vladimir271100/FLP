interface StringFunc {
	String func(String str);
}
class LambdaArgument {
	//Тип 1-го параметра - функциональный интерфейс. Ему можно передавать ссылку на любой экземпляр
	//интерфейса
	//Второй параметр зкадает строку
	static String changeStr(StringFunc sf, String s) {
		return sf.func(s);
	}
	public static void main(String[] args) {
		String inStr = "Lambda Expressions Expand Java";
		String outStr;
		System.out.println("Входная строка: " + inStr);
		//Изменяем порядок следования символов в строке
		StringFunc reverse = (str) -> {
			String result = "";
			for(int i = str.length()-1; i>=0; i--)
				result += str.charAt(i);
			return result;
		};
		outStr = changeStr(reverse, inStr);
		System.out.println("Обращенная строка: " + outStr);
		//Заменяем пробелы на дефисы
		outStr = changeStr((str) -> str.replace(' ', '_'), inStr);
		System.out.println("Строка с замененными пробелами: " + outStr);
		//Инвертируем регистр букв
		outStr = changeStr((str) -> {
			String result = "";
			char ch;
			for(int i=0; i<str.length(); i++) {
				ch= str.charAt(i);
				if(Character.isUpperCase(ch))
					result += Character.toLowerCase(ch);
				else
					result += Character.toUpperCase(ch);
			}
			return result;
		}, inStr);
		System.out.println("Строка с инвертированием регистра: " + outStr);
	}
}
