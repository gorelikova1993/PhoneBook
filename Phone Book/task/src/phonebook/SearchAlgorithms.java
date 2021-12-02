package phonebook;

import java.nio.charset.StandardCharsets;
import java.util.*;

class SearchAlgorithms {

    private final List<String> directory;
    private final List<String> find;
    private long searchTime;
    private long sortingTime;
    private int matchCounter;
    boolean isTooLong = false;

    SearchAlgorithms(List<String> directory, List<String> find) {
        this.directory = directory;
        this.find = find;
    }

    protected void linearSearch() {

        matchCounter = 0;

        /* Start time in milliseconds */
        long startTime = System.currentTimeMillis();

        /* Search algorithm */
        for (String findValue : find) {
            for (String directoryValue : directory) {
                if (directoryValue.contains(findValue)) {
                    matchCounter++;
                    break;
                }
            }
        }

        searchTime = System.currentTimeMillis() - startTime;

    }

    protected void bubbleSort() {

        System.out.println("Start searching (bubble sort + jump search)...");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < directory.size() - 1; i++) {
            for (int j = 0; j < directory.size() - i - 1; j++) {
                if (directory.get(j).compareTo(directory.get(j + 1)) > 0) {
                    Collections.swap(directory, j, j + 1);

                    sortingTime = System.currentTimeMillis() - startTime;

                    /* Stops if sorting time takes too long */
                    if (sortingTime > searchTime * 10) {
                        isTooLong = true;
                        return;
                    }
                }
            }
        }
        sortingTime = System.currentTimeMillis() - startTime;
    }

    protected void jumpSearch(){
        matchCounter = 0;
        long startTime = System.currentTimeMillis();

        for(String findValue : find) {
            if (jumpSearch(directory, findValue) > -1) {
                matchCounter++;
            };
        }
        searchTime = System.currentTimeMillis() - startTime;
    }

    private int jumpSearch(List<String> directory, String elem) {
        int size = directory.size();
        int start = 0;
        int end = (int)Math.floor(Math.sqrt(size));

        while (directory.get(end).compareTo(elem) < 0) {
            start = end;
            end += (int)Math.floor(Math.sqrt(size));
            if(end > size - 1)
                end = size;
        }
        for (int i = start; i <= end; i++) {
            if(directory.get(i).contains(elem))
                return i;
        }
        return -1;
    }


    public void startQuickSortAndBinarySearch() {
        matchCounter = 0;
        System.out.println("Start searching (quick sort + binary search)...");
        long startTime = System.currentTimeMillis();
        quickSort(directory, 0, directory.size() - 1);
        sortingTime = System.currentTimeMillis() - startTime;
           for(String findValue : find) {
               if (binarySearch(directory, findValue, 0, directory.size() - 1) > -1) {
                   matchCounter++;
               };
           }
        searchTime = System.currentTimeMillis() - startTime;
    }

    private int binarySearch(List<String> directory, String key, int low, int high) {
        int index = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (directory.get(mid).compareTo(key) < 0) {
                low = mid + 1;
            } else if (directory.get(mid).compareTo(key) > 0) {
                high = mid - 1;
            } else if (directory.get(mid).contains(key)) {
                index = mid;
                break;
            }
        }
        return index;
    }

    public void quickSort(List<String> directory,int low, int high) {
        if (low >= high)
            return;

        int middle = low + (high - low) / 2;
        String opora = directory.get(middle);

        int i = low, j = high;
        while (i <= j) {
            while (directory.get(i).compareTo(opora) < 0) {
                i++;
            }

            while (directory.get(j).compareTo(opora) > 0) {
                j--;
            }

            if (i <= j) {//меняем местами
               Collections.swap(directory,i,j);
                i++;
                j--;
            }
        }

        if (low < j)
            quickSort(directory, low, j);

        if (high > i)
            quickSort(directory, i, high);
    }

    public void makeHashTable() {
        matchCounter = 0;
        System.out.println("Start searching (hash table)...");
        long startTime = System.currentTimeMillis();

        int bucketsCount = (int) Math.ceil(directory.size() / 0.75);
        List<List<String>> hashTable = new ArrayList<>(Collections.nCopies(bucketsCount, null));
        int hash;
        for (String str : directory) {
            hash = getHash(str, bucketsCount);
            if (hashTable.get(hash) == null) {
                hashTable.set(hash, new ArrayList<>());
            }
            hashTable.get(hash).add(str);
        }

        sortingTime = System.currentTimeMillis() - startTime;

        List<String> bucket;
        for(String name : find) {
            hash = getHash(name, bucketsCount);
            bucket = hashTable.get(hash);

            for (String str : bucket) {
                if(name.equals(str)) {
                    matchCounter++;
                    break;
                }
            }
        }
        searchTime = System.currentTimeMillis() - startTime;




//        long startTime = System.currentTimeMillis();
//        Hashtable<Integer, String> dirTable = convertListToHashTable(directory);
//        Hashtable<Integer, String> findTable = convertListToHashTable(find);
//        Set<Integer> setOfKeysFind = findTable.keySet();
//        Set<Integer> setOfNames = dirTable.keySet();
//        sortingTime = System.currentTimeMillis() - startTime;
//        for (Integer key : setOfKeysFind) {
//            for (Integer ke2 : setOfNames) {
//                if(Objects.equals(key, ke2)) {
//                    matchCounter++;
//                }
//            }
//        }
//        searchTime = System.currentTimeMillis() - startTime;

    }

    private int getHash(String string, int bucketsCount) {
        byte[] values = string.getBytes(StandardCharsets.UTF_8);

        int hash = 0;

        for (byte value : values) {
            hash = (hash * 5 + value) % bucketsCount;
        }
        return hash;
    }

    private Map<String, Integer> convertListToMap(List<String> list) {
        Map<String, Integer> map = new HashMap<>(list.size() * 10);
        for(String str : list) {
            map.put(str, str.length());
        }
        return map;
    }

    private Hashtable<Integer, String> convertListToHashTable(List<String> list) {
        Hashtable<Integer, String> hashtable = new Hashtable<>(list.size() * 10);
        for(String str : list) {
            hashtable.put(Math.abs(str.hashCode()),str );
        }
        return hashtable;
        }


    private static String timeConverter(long totalMilliseconds) {

        long minutes = (totalMilliseconds / 1000) / 60;
        long seconds = (totalMilliseconds / 1000) % 60;
        long millis = totalMilliseconds - seconds * 1000;
        return String.format("%2d min. %2d sec. %2d ms.", minutes, seconds, millis);

    }

    protected void output(int type) {
        switch (type) {
            case 1:
                System.out.printf("Found %d / %d entries. ", matchCounter, find.size());
                System.out.println("Time taken: " + timeConverter(searchTime) + "\n");
                break;
            case 2:
                System.out.printf("Found %d / %d entries. ", matchCounter, find.size());
                System.out.println("Time taken: " + timeConverter(sortingTime + searchTime));
                System.out.println("Sorting time: " + timeConverter(sortingTime) +
                        (isTooLong ? "- STOPPED, moved to linear search" : ""));
                System.out.println("Searching time: " + timeConverter(searchTime) + "\n");
                break;
            case 3:
                System.out.printf("Found %d / %d entries. ", matchCounter, find.size());
                System.out.println("Time taken: " + timeConverter(sortingTime + searchTime));
                System.out.println("Sorting time: " + timeConverter(sortingTime));
                System.out.println("Searching time: " + timeConverter(searchTime) + "\n");
                break;
            case 4:
                System.out.printf("Found %d / %d entries. ", matchCounter, find.size());
                System.out.println("Time taken: " + timeConverter(sortingTime + searchTime));
                System.out.println("Creating time: " + timeConverter(sortingTime));
                System.out.println("Searching time: " + timeConverter(searchTime) + "\n");
                break;
        }

    }
}