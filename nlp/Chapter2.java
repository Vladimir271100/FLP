//package packt;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.EnglishStopTokenizerFactory;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
//import net.didion.jwnl.JWNLException;
//import opennlp.tools.coref.mention.JWNLDictionary;
//import opennlp.tools.ml.model.TrainingParameters;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.InputStreamFactory;
//import opennlp.tools.stemmer.SnowballStemmer;
//import opennlp.tools.stemmer.SnowballStemmer.ALGORITHM;
//import opennlp.tools.util.StreamFactory;

import java.nio.charset.StandardCharsets;

public class Chapter2 {

    private static String paragraph = "Let's pause, \nand then reflect.";

    public static void main(String[] args) {
//		usingTheScannerClass();
//      usingTheSplitMethod();
//      usingTheBreakIterator();
//      usingTheStreamTokenizerClass();
//      usingTheStringTokenizerClass();

System.out.println("\n\nusingTheOpenNLPTokenizers");
        usingTheOpenNLPTokenizers();
        
System.out.println("\n\nusingTheStanfordTokenizer");
        usingTheStanfordTokenizer();
        
System.out.println("\n\nusingTheStanfordLemmatizer");
        usingTheStanfordLemmatizer();
        usingTheTokenizerMEClass();
        usingTheWhitespaceTokenizer();
		usingLingPipeTokenizers();
        stemmerExamples();
        usingLemmatizationExamples();
        
System.out.println("\n\nusingStopWords----------");        
        usingStopwordsExamples();
System.out.println("\nusingStopWords----------\n");

System.out.println("\n\nnormalization----------");            
        normalizationProcessExamples();
//		trainingATokenizer();
    }

    private static void usingTheScannerClass() {
        Scanner scanner = new Scanner("Let's pause, and then reflect.");
        List<String> list = new ArrayList<>();
        // Specifying the delimiters
        scanner.useDelimiter("[ ,.]");

        while (scanner.hasNext()) {
            String token = scanner.next();
            list.add(token);
        }
        for (String token : list) {
            System.out.println(token);
        }
    }

    private static void usingTheSplitMethod() {
        String text = "Mr. Smith went to 123 Washington avenue.";
        String tokens[] = text.split("\\s+");
        for (String token : tokens) {
            System.out.println(token);
        }

        text = "Let's pause, and then reflect.";
        tokens = text.split("[ ,.]");
        for (String token : tokens) {
            System.out.println(token);
        }
    }

    private static void usingTheBreakIterator() {
//        Locale currentLocale = new Locale("en", "US");
        BreakIterator wordIterator
                = BreakIterator.getWordInstance();
        String text = "Let's pause, and then reflect.";
        wordIterator.setText(text);
        int boundary = wordIterator.first();
        while (boundary != BreakIterator.DONE) {
            int begin = boundary;
            System.out.print(boundary + "-");
            boundary = wordIterator.next();
            int end = boundary;
            if(end == BreakIterator.DONE) break;
            System.out.println(boundary + " [" + 
                    text.substring(begin, end) + "]");
        }
    }

    private static void usingTheStreamTokenizerClass() {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(
                    new StringReader("Let's pause, and then reflect."));
//            tokenizer.ordinaryChar('\'');
//            tokenizer.ordinaryChar(',');
            boolean isEOF = false;
            while (!isEOF) {

                int token = tokenizer.nextToken();
                switch (token) {
                    case StreamTokenizer.TT_EOF:
                        isEOF = true;
                        break;
                    case StreamTokenizer.TT_EOL:
                        break;
                    case StreamTokenizer.TT_WORD:
                        System.out.println(tokenizer.sval);
                        break;
                    case StreamTokenizer.TT_NUMBER:
                        System.out.println(tokenizer.nval);
                        break;
                    default:
                        System.out.println((char) token);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private static void usingTheStringTokenizerClass() {
        StringTokenizer st
                = new StringTokenizer("Let's pause, and then reflect.");
        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
    }

    private static void usingTheOpenNLPTokenizers() {
        usingTheSimpleTokenizerClass();
        usingTheSimpleTokenizerClass();
        usingTheTokenizerMEClass();
        usingTheWhitespaceTokenizer();
    }

    public static File getModelDir() {
        return new File("/home/share/4.142.2.23/openNLPModels");
    }

    private static void usingTheSimpleTokenizerClass() {
        System.out.println("--- SimpleTokenizer");
        SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;
        String tokens[] = simpleTokenizer.tokenize(paragraph);
        for (String token : tokens) {
            System.out.println(token);
        }
    }

    private static void usingTheTokenizerMEClass() {
        try {
			System.out.println("-------------OpenNLP_ME-------------");
            InputStream modelIn = new FileInputStream(new File(
                    getModelDir(), "opennlp-en-ud-ewt-tokens-1.2-2.5.0.bin"));
            TokenizerModel model = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model);
            String tokens[] = tokenizer.tokenize(paragraph);
            for (String token : tokens) {
                System.out.println(token);
            }
            System.out.println("-------------OpenNLP_ME_Russian-------------");
            InputStream modelIn1 = new FileInputStream(new File(
                    getModelDir(), "opennlp-ru-ud-gsd-tokens-1.2-2.5.0.bin"));
            TokenizerModel model1 = new TokenizerModel(modelIn1);
            Tokenizer tokenizer1 = new TokenizerME(model1);
            String paragraph1 = "Какая-то фраза на русском, случайно залетевшая.";
            String tokens1[] = tokenizer1.tokenize(paragraph1);
            for (String token1 : tokens1) {
                System.out.println(token1);
            }
            System.out.println("\n");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void usingTheWhitespaceTokenizer() {
        String tokens[] = WhitespaceTokenizer.INSTANCE.tokenize(paragraph);
        for (String token : tokens) {
            System.out.println(token);
        }
    }

    private static void stemmerExamples() {
//        usingPorterStemmerExample();
        usingTheLingPipeStemmer();
    }

/*
private static void usingPorterStemmerExample() {
	System.out.println("\n------Стемминг OpenNLP----------");
        // Создание экземпляра SnowballStemmer для английского языка
        SnowballStemmer stemmer = new SnowballStemmer(ALGORITHM.ENGLISH);

        // Пример слов для стемминга
        String[] words = {"running", "runner", "ran", "easily", "fairly"};

        // Стемминг слов
        System.out.println("Стемминг слов:");
        for (String word : words) {
            String stemmedWord = stemmer.stem(word).toString();
            System.out.println(word + " -> " + stemmedWord);
        }
        System.out.println("\n------Стемминг OpenNLP----------");
    }
*/    
/*    private static void usingPorterStemmerExample() {
        String words[] = {"bank", "banking", "banks", "banker",
            "banked", "bankart"};
        PorterStemmer ps = new PorterStemmer();
        for (String word : words) {
            String stem = ps.stem(word);
            System.out.println("Word: " + word + "  Stem: " + stem);
        }
    }
*/

    private static void usingTheLingPipeStemmer() {
        String words[] = {"bank", "banking", "banks", "banker",
            "banked", "bankart"};
        TokenizerFactory tokenizerFactory
                = IndoEuropeanTokenizerFactory.INSTANCE;
        TokenizerFactory porterFactory
                = new PorterStemmerTokenizerFactory(tokenizerFactory);
        String[] stems = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            com.aliasi.tokenizer.Tokenization tokenizer
                    = new com.aliasi.tokenizer.Tokenization(words[i], porterFactory);
            stems = tokenizer.tokens();
            System.out.print("Word: " + words[i]);
            for (String stem : stems) {
                System.out.println("  Stem: " + stem);
            }
        }

    }

    private static void usingTheStanfordTokenizer() {

        // Using PTBTokenizer
        System.out.println("\n\n----PTBTokenizer Example");

        // First example
//        PTBTokenizer ptb = new PTBTokenizer(new StringReader(paragraph),
//                new CoreLabelTokenFactory(),null);
//        while (ptb.hasNext()) {
//            System.out.println(ptb.next());
//        }
        // CoreLabel example
        CoreLabelTokenFactory ctf = new CoreLabelTokenFactory();
        PTBTokenizer ptb = new PTBTokenizer(new StringReader(paragraph),
                ctf, "invertible=true");
//        PTBTokenizer ptb = new PTBTokenizer(new StringReader(paragraph),
//                new WordTokenFactory(), null);
        while (ptb.hasNext()) {
            CoreLabel cl = (CoreLabel) ptb.next();
            System.out.println(cl.originalText() + " ("
                    + cl.beginPosition() + "-" + cl.endPosition() + ")");
        }

        // Using a DocumentPreprocessor
        System.out.println("----DocumentPreprocessor Example");
        Reader reader = new StringReader(paragraph);
        DocumentPreprocessor documentPreprocessor
                = new DocumentPreprocessor(reader);

        Iterator<List<HasWord>> it = documentPreprocessor.iterator();
        while (it.hasNext()) {
            List<HasWord> sentence = it.next();            for (HasWord token : sentence) {
                System.out.println(token);
            }
        }

//        for (List<HasWord> sentence : documentP

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
					case "NOUterate over all tokens in a sentence
            for (CoreLabel word : sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                lemmas.add(wor
N": tagsN[i] = "NNN";
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

    private static void usingStopwordsExamples() {
//        usingStopWordsClassExample();
        usingLingpipeStopWord();
    }
/*
    private static void usingStopWordsClassExample() {
        StopWords stopWords = new StopWords("stopwords.txt");
        SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;
        paragraph = "A simple approach is to create a class "
                + "to hold and remove stopwords.";
        String tokens[] = simpleTokenizer.tokenize(paragraph);
        String list[] = stopWords.removeStopWords(tokens);
        for (String word : list) {
            System.out.println(word);
        }
        stopWords.displayStopWords();
    }

*/
    private static void usingLingpipeStopWord() {
        paragraph = "A simple approach is to create a class "
                + "to hold and remove stopwords.";
        TokenizerFactory factory = IndoEuropeanTokenizerFactory.INSTANCE;
//        factory = new LowerCaseTokenizerFactory(factory);
        factory = new EnglishStopTokenizerFactory(factory);
//         factory = new PorterStemmerTokenizerFactory(factory);
        System.out.println(factory.tokenizer(paragraph.toCharArray(), 0, paragraph.length()));
        com.aliasi.tokenizer.Tokenizer tokenizer
                = factory.tokenizer(paragraph.toCharArray(), 0, paragraph.length());
        for (String token : tokenizer) {
            System.out.println(token);
        }
    }


    private static void normalizationProcessExamples() {
        usingLingPipeForNormalization();
        String text = "A Sample string with acronyms, IBM, and UPPER "
                + "and lower case letters.";
        String result = text.toLowerCase();
        System.out.println(result + "\nЗавершено ЛингПайп--------");
        
    }


    private static void usingLingPipeForNormalization() {
        paragraph = "A simple approach is to create a class "
                + "to hold and remove stopwords.";
        TokenizerFactory factory = IndoEuropeanTokenizerFactory.INSTANCE;
        factory = new LowerCaseTokenizerFactory(factory);
        factory = new EnglishStopTokenizerFactory(factory);
        factory = new PorterStemmerTokenizerFactory(factory);
        System.out.println(factory.tokenizer(paragraph.toCharArray(), 0, paragraph.length()));
        com.aliasi.tokenizer.Tokenizer tokenizer
                = factory.tokenizer(paragraph.toCharArray(), 0, paragraph.length());
        for (String token : tokenizer) {
            System.out.println(token);
        }
        System.out.println("\nЗавершено ЛингПайп2--------\n");
    }

/*
    private static void trainingATokenizer() {
        createOpenNLPModel();
        try {
            paragraph = "A demonstration of how to train a tokenizer.";
            InputStream modelInputStream = new FileInputStream(new File(".", "mymodel.bin"));
            TokenizerModel model = new TokenizerModel(modelInputStream);
            Tokenizer tokenizer = new TokenizerME(model);
            String tokens[] = tokenizer.tokenize(paragraph);
            for (String token : tokens) {
                System.out.println(token);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
*/    
/*	try {
            // Укажите путь к вашему текстовому файлу
            File file = new File("path/to/your/textfile.txt");

            // Создание InputStreamFactory
            InputStreamFactory inputStreamFactory = new InputStreamFactory() {
                @Override
                public InputStream createInputStream() throws IOException {
                    return new FileInputStream(file);
                }
            };

            // Создание объекта ObjectStream с использованием PlainTextByLineStream
            ObjectStream<String> lineStream = new PlainTextByLineStream(modelInputStream, StandardCharsets.UTF_8.name());

            String line;
            while ((line = lineStream.read()) != null) {
                System.out.println(line);
                // Здесь можно добавить логику для обработки строк
            }

            lineStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/    
/*
    private static void createOpenNLPModel() {
        try {
            // Загрузка данных для обучения
            InputStreamFactory inputStreamFactory = new InputStreamFactory() {
                @Override
                public InputStream createInputStream() throws IOException {
                    return new FileInputStream("./training-data.train");
                }
            };

            ObjectStream<TokenSample> sampleStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");

            // Создание фабрики токенизаторов
            TokenizerFactory factory = TokenizerFactory.create("en", true);

            // Параметры обучения
            TrainingParameters params = TrainingParameters.defaultParams();
            params.put(TrainingParameters.ITERATIONS_PARAM, "100");
            params.put(TrainingParameters.CUTOFF_PARAM, "1");

            // Обучение модели токенизатора
            TokenizerModel model = TokenizerME.train(sampleStream, factory, params);

            // Сохранение модели в файл
            // (добавьте код для сохранения модели в файл здесь)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
/*    
        try {
			File file = new File("./training-data.train");

            // Создание InputStreamFactory
            InputStreamFactory inputStreamFactory = new InputStreamFactory() {
                @Override
                public InputStream createInputStream() throws IOException {
                    return new FileInputStream(file);
                }
            };

            ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8.name());
            ObjectStream<TokenSample> sampleStream
                    = new TokenSampleStream(lineStream);
            TokenizerModel model = TokenizerME.train("en", sampleStream, true, 5, 100);
            BufferedOutputStream modelOutputStream = new BufferedOutputStream(
                    new FileOutputStream(new File("./mymodel.bin")));
            model.serialize(modelOutputStream);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
*/

}
