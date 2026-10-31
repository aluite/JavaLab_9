import java.util.Arrays;
import java.util.Random;

public class SortingTest {

    private static final int SEED = 42; // Фиксированный seed для воспроизводимости данных
    private static final int ITERATIONS = 5; // Число повторов для поиска медианы

    public static void main(String[] args) {
        int[] sizes = {10, 100, 1000, 10000};

        System.out.println("--- ОСНОВНОЙ ЭКСПЕРИМЕНТ (Случайные массивы) ---");
        System.out.printf("%-10s | %-15s | %-15s | %-15s | %-15s%n", 
                "Размер", "Bubble (ns)", "Selection (ns)", "Insertion (ns)", "Quick (ns)");
        System.out.println("---------------------------------------------------------------------------------");

        for (int size : sizes) {
            long[] bubbleTimes = new long[ITERATIONS];
            long[] selectionTimes = new long[ITERATIONS];
            long[] insertionTimes = new long[ITERATIONS];
            long[] quickTimes = new long[ITERATIONS];

            for (int iter = 0; iter < ITERATIONS; iter++) {
                // Прогрев перед первыми значимыми измерениями
                if (size == 10 && iter == 0) {
                    runWarmup();
                }

                int[] baseArray = generateRandomArray(size);

                // Тест Bubble Sort
                int[] arrBubble = baseArray.clone();
                long start = System.nanoTime();
                SortingAlgorithms.bubbleSort(arrBubble);
                bubbleTimes[iter] = System.nanoTime() - start;
                validate(baseArray, arrBubble);

                // Тест Selection Sort
                int[] arrSelection = baseArray.clone();
                start = System.nanoTime();
                SortingAlgorithms.selectionSort(arrSelection);
                selectionTimes[iter] = System.nanoTime() - start;
                validate(baseArray, arrSelection);

                // Тест Insertion Sort
                int[] arrInsertion = baseArray.clone();
                start = System.nanoTime();
                SortingAlgorithms.insertionSort(arrInsertion);
                insertionTimes[iter] = System.nanoTime() - start;
                validate(baseArray, arrInsertion);

                // Тест Quick Sort
                int[] arrQuick = baseArray.clone();
                start = System.nanoTime();
                SortingAlgorithms.quickSort(arrQuick, 0, arrQuick.length - 1);
                quickTimes[iter] = System.nanoTime() - start;
                validate(baseArray, arrQuick);
            }

            System.out.printf("%-10d | %-15d | %-15d | %-15d | %-15d%n",
                    size, getMedian(bubbleTimes), getMedian(selectionTimes), getMedian(insertionTimes), getMedian(quickTimes));
        }

        runSelfStudy();
    }

    // Прогревочный запуск JVM
    private static void runWarmup() {
        int[] warmup = new int[500];
        for (int i = 0; i < 10; i++) {
            SortingAlgorithms.bubbleSort(warmup.clone());
            SortingAlgorithms.quickSort(warmup.clone(), 0, warmup.length - 1);
        }
    }

    // Самостоятельная работа: Сравнение Insertion Sort на отсортированном и обратном массивах
    private static void runSelfStudy() {
        System.out.println("\n--- САМОСТОЯТЕЛЬНАЯ РАБОТА (Insertion Sort, N=1000) ---");
        int size = 1000;
        long[] sortedTimes = new long[ITERATIONS];
        long[] reversedTimes = new long[ITERATIONS];

        // 1. Отсортированный массив
        int[] sortedBase = generateRandomArray(size);
        Arrays.sort(sortedBase);

        // 2. Обратный массив
        int[] reversedBase = new int[size];
        for (int i = 0; i < size; i++) {
            reversedBase[i] = sortedBase[size - 1 - i];
        }

        for (int iter = 0; iter < ITERATIONS; iter++) {
            int[] arrSorted = sortedBase.clone();
            long start = System.nanoTime();
            SortingAlgorithms.insertionSort(arrSorted);
            sortedTimes[iter] = System.nanoTime() - start;

            int[] arrReversed = reversedBase.clone();
            start = System.nanoTime();
            SortingAlgorithms.insertionSort(arrReversed);
            reversedTimes[iter] = System.nanoTime() - start;
        }

        System.out.println("Медианное время на отсортированном массиве: " + getMedian(sortedTimes) + " нс");
        System.out.println("Медианное время на обратном массиве:      " + getMedian(reversedTimes) + " нс");
    }

    private static int[] generateRandomArray(int size) {
        Random rand = new Random(SEED); // Зафиксированный seed
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(10000);
        }
        return array;
    }

    private static void validate(int[] original, int[] sorted) {
        int[] control = original.clone();
        Arrays.sort(control);
        if (!Arrays.equals(control, sorted)) {
            throw new IllegalStateException("Ошибка: Массив отсортирован неверно или нарушена целостность данных!");
        }
    }

    private static long getMedian(long[] times) {
        Arrays.sort(times);
        return times[times.length / 2];
    }
}
