public class TimSort {
    private static int iterationsCount = 0;
    private static final int MIN_MERGE = 32;

    // Стек для хранения прогонов
    private static int[] runStart;
    private static int[] runLength;
    private static int stackSize;

    public static void sort(int[] array) {
        int n = array.length;
        if (n < 2) return;

        runStart = new int[n];
        runLength = new int[n];
        stackSize = 0;

        int minRun = minRunLength(n);
        int i = 0;

        // Основной цикл: создание прогонов
        while (i < n) {
            int runBegin = i;
            i = findRun(array, i); // Находим естественный прогон

            // Если прогон слишком короткий - расширяем
            if (i - runBegin < minRun) {
                int end = Math.min(runBegin + minRun - 1, n - 1);
                insertionSort(array, runBegin, end);
                i = end + 1;
            }

            // Добавляем прогон в стек
            runStart[stackSize] = runBegin;
            runLength[stackSize] = i - runBegin;
            stackSize++;

            /* Проверяем условия слияния
            (Не слишком ли маленький пред-предпоследний бег относительно суммы двух последних?)
             Обеспечивает O(n log n) в худшем случае и предотвращает слишком глубокий стек.
             */
            while (stackSize > 1) {
                int x = runLength[stackSize - 1];
                int y = runLength[stackSize - 2];

                // Упрощенное условие: сливаем если прогоны примерно равны или один слишком маленький
                if (stackSize > 2 && runLength[stackSize - 3] <= y + x) {
                    if (runLength[stackSize - 3] < x) {
                        merge(array, stackSize - 3);
                    } else {
                        merge(array, stackSize - 2);
                    }
                } else if (y <= x) {
                    merge(array, stackSize - 2);
                } else {
                    break;
                }
            }
        }

        // Финальное слияние всех прогонов
        while (stackSize > 1) {
            merge(array, stackSize - 2);
        }
    }

    /**
     * Поиск естественного прогона (уже отсортированной последовательности)
     */
    private static int findRun(int[] array, int start) {
        int i = start + 1;
        int n = array.length;

        if (i >= n) return start;

        // Определяем направление
        if (array[i] >= array[i - 1]) {
            // Возрастающая последовательность
            while (i < n && array[i] >= array[i - 1]) {
                iterationsCount++;
                i++;
            }
        } else {
            // Убывающая последовательность
            while (i < n && array[i] < array[i - 1]) {
                iterationsCount++;
                i++;
            }
            // Разворачиваем убывающий прогон
            reverse(array, start, i - 1);
        }

        return i;
    }

    /**
     * Разворот отрезка массива
     */
    private static void reverse(int[] array, int left, int right) {
        while (left < right) {
            iterationsCount++;
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Слияние двух прогонов по индексу в стеке
     */
    private static void merge(int[] array, int index) {
        int left = runStart[index];
        int mid = runStart[index + 1] - 1;
        int right = runStart[index + 1] + runLength[index + 1] - 1;

        mergeArrays(array, left, mid, right);

        // Обновляем стек
        runLength[index] = runLength[index] + runLength[index + 1];

        // Сдвигаем элементы стека
        for (int i = index + 1; i < stackSize - 1; i++) {
            runStart[i] = runStart[i + 1];
            runLength[i] = runLength[i + 1];
        }
        stackSize--;
    }

    /**
     * Сортировка вставками
     */
    private static void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = array[i];

            // Бинарный поиск позиции для вставки
            int insertPos = binarySearch(array, left, i - 1, key);
            iterationsCount++;
            // Сдвигаем элементы, если нужно
            if (insertPos < i) {
                System.arraycopy(array, insertPos, array, insertPos + 1, i - insertPos);
                array[insertPos] = key;
            }
        }
    }

    private static int binarySearch(int[] array, int left, int right, int key) {
        while (left <= right) {
            iterationsCount++;
            int mid = left + (right - left) / 2;
            if (array[mid] <= key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    /**
     * Стандартное слияние двух отсортированных массивов
     */
    private static void mergeArrays(int[] array, int left, int mid, int right) {
        int len1 = mid - left + 1;
        int len2 = right - mid;

        int[] leftArr = new int[len1];
        int[] rightArr = new int[len2];

        // Копирование данных
        for (int i = 0; i < len1; i++) {
            iterationsCount++;
            leftArr[i] = array[left + i];
        }
        for (int j = 0; j < len2; j++) {
            iterationsCount++;
            rightArr[j] = array[mid + 1 + j];
        }

        // Слияние
        int i = 0, j = 0, k = left;
        while (i < len1 && j < len2) {
            iterationsCount++;
            if (leftArr[i] <= rightArr[j]) {
                array[k++] = leftArr[i++];
            } else {
                array[k++] = rightArr[j++];
            }
        }

        // Копирование остатков
        while (i < len1) {
            iterationsCount++;
            array[k++] = leftArr[i++];
        }
        while (j < len2) {
            iterationsCount++;
            array[k++] = rightArr[j++];
        }
    }

    /**
     * Вычисление оптимального minRun
     */
    private static int minRunLength(int n) {
        int r = 0;
        while (n >= MIN_MERGE) {
            r |= (n & 1);
            n >>= 1;
        }
        return n + r;
    }

    public static void setIterationsCount(int count) {
        iterationsCount = count;
    }

    public static int getIterationsCount() {
        return iterationsCount;
    }
}