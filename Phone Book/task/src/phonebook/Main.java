package phonebook;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String directoryPath = "D:\\Java\\learning projects\\files for projects\\directory.txt";
    public static final String findPath = "D:\\Java\\learning projects\\files for projects\\find.txt";

    public static void main(String[] args) throws IOException {

        List<String> directory = new ArrayList<>();
        List<String> find = Files.readAllLines(Paths.get(findPath));
        File dir = new File(directoryPath);
        Scanner sc = new Scanner(dir);
        while (sc.hasNextLine()) {
            sc.nextInt();
            String fullName = sc.nextLine();
            fullName = fullName.trim();
            directory.add(fullName);
        }

        SearchAlgorithms newSearch = new SearchAlgorithms(directory, find);

        System.out.println("Start searching (linear search)...");
        newSearch.linearSearch();
        newSearch.output(1);
        newSearch.bubbleSort();
        if (newSearch.isTooLong) {
            newSearch.linearSearch();
        } else {
            newSearch.jumpSearch();
        }
        newSearch.output(2);
        newSearch.startQuickSortAndBinarySearch();
        newSearch.output(3);
        newSearch.makeHashTable();
        newSearch.output(4);

    }
}


