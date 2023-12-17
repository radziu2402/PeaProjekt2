import algorithm.SimulatedAnnealing;
import algorithm.TabuSearch;
import data.DataPrinter;
import data.FromFileReader;
import data.Matrix;
import data.RandomDataGenerator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int timeLimit = -1;
        int neighbor = -1;
        double temperature_coefficient = -1;
        Matrix matrix = null;
        TabuSearch tabuSearch;
        SimulatedAnnealing simulatedAnnealing;
        int menu = -1;
        while (menu != 0) {
            System.out.println("Wybierz opcję programu: ");
            System.out.println("1. Wczytaj graf z pliku");
            System.out.println("2. Wygeneruj losowy graf");
            System.out.println("3. Wyświetl aktualny graf");
            System.out.println("4. Kryterium stopu");
            System.out.println("5. Wybór sąsiedztwa dla TS");
            System.out.println("6. Wybór współczynnika zmiany temperatury dla SW");
            System.out.println("7. Uruchom TabuSearch");
            System.out.println("8. Uruchom SimulatedAnnealing");
            System.out.println("9. Wyjdź");
            menu = scanner.nextInt();
            switch (menu) {
                case 1 -> {
                    System.out.println("Podaj nazwę pliku z danymi:");
                    Scanner sc = new Scanner(System.in);
                    matrix = new FromFileReader().loadFromFile(sc.nextLine());
                }
                case 2 -> {
                    System.out.println("Podaj liczbę miast:");
                    matrix = (new RandomDataGenerator()).generateData(scanner.nextInt());
                    System.out.println("Dane zostały wygenerowane losowo.");
                }
                case 3 -> {
                    if(matrix!=null) {
                        DataPrinter dataPrinter = new DataPrinter();
                        dataPrinter.displayData(matrix);
                    }
                    else System.out.println("Najpierw wczytaj lub wygeneruj graf");
                }
                case 4 -> {
                    System.out.println("Podaj limit czasowy w ms: ");
                    timeLimit = scanner.nextInt();
                }
                case 5 -> {
                    System.out.println("Wybierz sąsiedztwo: ");
                    System.out.println("1. Swap()");
                    System.out.println("2. Insert()");
                    System.out.println("3. Inverse()");
                    int choise = scanner.nextInt();
                    switch (choise) {
                        case 1, 2, 3 -> neighbor = choise;
                        default -> System.out.println("Nieprawidłowy wybór. Wybierz ponownie.");
                    }
                }
                case 6 -> {
                    System.out.println("Podaj współczynnik zmiany temperatury (w przedziale 0.0 - 1.0)");
                    temperature_coefficient = scanner.nextDouble();
                }
                case 7 -> {
                    if(matrix == null || timeLimit == -1 || neighbor == -1){
                        System.out.println("Uzupełnij brakujące dane zanim rozpoczniesz");
                    }
                    else{
                        tabuSearch = new TabuSearch(matrix, timeLimit, neighbor);
                        tabuSearch.solve();
                    }
                }
                case 8 -> {
                    if(matrix == null || timeLimit == -1 || temperature_coefficient == -1){
                        System.out.println("Uzupełnij brakujące dane zanim rozpoczniesz");
                    }
                    else{
                        simulatedAnnealing = new SimulatedAnnealing(matrix, timeLimit, temperature_coefficient);
                        simulatedAnnealing.solve();
                    }
                }
                case 9 -> System.exit(0);
                default -> System.out.println("Nieprawidłowy wybór. Wybierz ponownie.");
            }

        }
    }
}
