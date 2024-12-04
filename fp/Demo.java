class Book {
	private String title;
	private String author;
	private int pubDate;

	Book(String t, String a, int d) {
		System.out.println("Выполняется конструктор класса Book");
		title = t;
		author = a;
		pubDate = d;
	}
	// Перегрузка конструктора - версия с двумя параметрами String
	 Book(String t, String a) {
                System.out.println("Выполняется конструктор класса Book");
                title = t;
                author = a;
                pubDate = 2000;
        }
	void show() {
		System.out.println("Заглавие: "+ title);
		System.out.println("Заглавие: "+ author);
		System.out.println("Год публикации: "+ pubDate);
		System.out.println();
	}
}
class Demo {
	public static void main(String[] arg) {
		Book[] books = new Book [4];
		
		books[0] = new Book(" Статистика и котики", " Савельев В." , 2024);
		books[1] = new Book(" Энтони: Путь к дракону"," Хорев А.");
		books[2] = new Book(" Преступление и наказание", " Достоевский Ф.");
		books[3] = new Book(" Анна Каренина", " Толстой Л.");

		for(int i=0; i< books.length;i++)
			books[i].show();	


	}
}



