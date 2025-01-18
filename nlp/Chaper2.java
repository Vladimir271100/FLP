import opennlp.tools.lemmatizer.DictionaryLemmatizer

private static void usingTheOpenNLPLemmatizer() {
        try {
            // Тестовая строка
            String[] tokens = new String[]{"Пробный", "текст", "для", "проверки", "по", "русски", "."};

            // Чтение модели частей речи
            InputStream posModelIn = new FileInputStream("/home/share/4.142.2.23/openNLPModels/opennlp-ru-ud-gsd-pos-1.2-2.5.0.bin");
            POSModel posModel = new POSModel(posModelIn);
            POSTaggerME posTagger = new POSTaggerME(posModel);

            // Пометка токенов
            String[] tags = posTagger.tag(tokens);

            // Загрузка словаря для лемматизации
            InputStream dictLemmatizer = new FileInputStream("/home/share/4.142.2.23/openNLPModels/en-lemmatizer.txt");
            DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);

            // Получение лемм
            
            String[] tagsN = new String[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
				switch (tags[i]) {
					case "NOUN": tagsN[i] = "NNN";
					break;
					case "DET": tagsN[i] = "DT";
					break;
					case "ADV": tagsN[i] = "RB";
					break;
					case "ADJ": tagsN[i] = "JJ";
					break;
					case "PROPN": tagsN[i] = "JJ";
					break;
					default: tagsN[i] = "PUNCT";
				}
            }
            
            String[] lemmas = lemmatizer.lemmatize(tokens, tags);

            // Вывод результатов
            System.out.println("\nPrinting lemmas for the given sentence...");
            System.out.println("WORD - POSTAG : LEMMA");
            for (int i = 0; i < tokens.length; i++) {
                System.out.println(tokens[i] + " - " + tags[i] + " : " + lemmas[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-------------Завершение OpenNLPLemmatizer-----------\n");
    }
