import java.util.*;

import java.io.*;

@FunctionalInterface                                                       // Sorting Interface
interface ChoiceSelection {
    Comparator<Book> getComparator();
}

@FunctionalInterface
interface BookType {
    boolean matchesFilter(String criteria);                                // Filtered Interface
}

public class SystemManager {
    private static final String SAVE_FILE = "Books.dat";                   // Save File Constant
    private static ArrayList<Book> bookList;                               // Load data
    private static Scanner scanner = new Scanner(System.in);               // Create Scanner
    private static ArrayList<Book> searchList;
    private static ArrayList<Book> filteredList;

    // main
    public static void main(String[] args) {        
        // Library initialization
        initializeLibrary();

        // Display main menu
        clearScreen();
        showCredits();
        waitForEnter();
        clearScreen();
        displayMenu();
        
        // Close scanner before exiting
        scanner.close();
    }

    // Main Menu
    private static void displayMenu() {
        clearScreen();
        while (true) {
            System.out.println("╔════╦═══════════════╗");
            System.out.println("║ 1. ║ Show Library  ║");
            System.out.println("║ 2. ║ Add Book      ║");
            System.out.println("║ 3. ║ Remove Book   ║");
            System.out.println("║ 4. ║ Search        ║");
            System.out.println("║ 5. ║ Save Changes  ║");
            System.out.println("║ 6. ║ Exit          ║");
            System.out.println("╚════╩═══════════════╝");
            System.out.print("Select an option: ");
            
            try {
                int userSelection = UserInput.getInt("", 1, 6);

                if (userSelection == 1) {
                    clearScreen();
                    showLibrary();
                    filteredList = bookList;
                } else if (userSelection == 2) {
                    addBook();
                } else if (userSelection == 3) {
                    clearScreen();
                    removeBook();
                } else if (userSelection == 4) {
                    clearScreen();
                    searchList = new ArrayList<>();
                    searchMenu();
                    showLibrary();
                    filteredList = bookList;
                } else if (userSelection == 5) {
                    clearScreen();
                    exportFile(bookList, SAVE_FILE);
                } else if (userSelection == 6) {
                    clearScreen();
                    goodbye();
                    System.exit(0);
                }
            } catch (ValidationException e) {
                clearScreen();
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    // Search for book recursively
    private static void searchMenu() {
        clearScreen();
        System.out.println("╔═════════════════════╗");
        System.out.println("║   SEARCH FOR BOOK   ║");
        System.out.println("╚═════════════════════╝");
        try {
            String searchTerm = UserInput.getNonEmptyString("Search for: ");
            filteredList = search(searchTerm, bookList.size());
        } catch (ValidationException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static ArrayList<Book> search(String term, int n) {
        if (n == 0) {
            return searchList;
        }

        if (bookList.get(n-1).getTitle().toLowerCase().contains(term.toLowerCase())){
            searchList.add(bookList.get(n-1));
        }
        return search(term, n - 1);
    }

    // Add Book
    private static void addBook() {
        clearScreen();
        System.out.println("╔════════════════════╗");
        System.out.println("║    ADD NEW BOOK    ║");
        System.out.println("╚════════════════════╝");

        try {
            String title = UserInput.getNonEmptyString("Enter Title: ");
            String author = UserInput.getPatternMatchingString("Enter Author: ", "^[\\p{L} .'-]+$", "Author cannot contain special characters or numbers.");
            int year = UserInput.getInt("Enter Publication Year: ", 0, java.time.Year.now().getValue());
            String isbn = UserInput.getNonEmptyString("Enter ISBN: ");

            System.out.println("\nBook Type: ");
            System.out.println("1. Fiction");
            System.out.println("2. Textbook");
            System.out.println("3. Non-Fiction");
            int bookType = UserInput.getInt("Choose book type (1-3): ", 1, 3);

            Book newBook;
            switch(bookType) {
                case 1:
                    String genre = UserInput.getPatternMatchingString("Enter Genre: ", "^[\\p{L} -]+$", "Genre cannot contain special characters");
                    newBook = new FictionBook(title, author, year, isbn, genre);
                    break;
                case 2:
                    String subject = UserInput.getPatternMatchingString("Enter textbook subject: ", "^[\\p{L}0-9 &/-]+$", "Subject cannot contain special characters or numbers");
                    newBook = new Textbook(title, author, year, isbn, subject);
                    break;
                default:
                    newBook = new Book(title, author, year, isbn);
            }
            bookList.add(newBook);
        } catch (ValidationException e) {
            System.err.println("\nError: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nUnexpected error: " + e.getMessage());
        }

        waitForEnter();
        clearScreen();
    }

    // Remove Book
    private static void removeBook() {
        clearScreen();
        System.out.println("╔════════════════════╗");
        System.out.println("║     REMOVE BOOK    ║");
        System.out.println("╚════════════════════╝");
        try {
            String isbn = UserInput.getNonEmptyString("Enter ISBN of Book to Remove: ");
            Iterator<Book> iterator = bookList.iterator();
            while (iterator.hasNext()) {
                Book book = iterator.next();
                if (book.getISBN().equals(isbn)) {
                    System.out.println("Removing " + book.getTitle() + " from library...");
                    iterator.remove();
                }
            }
        } catch (ValidationException e) {
            System.err.println("\nError: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nUnexpected Error: " + e.getMessage());
        }
    }

    // Show Library
    private static void showLibrary() {
        clearScreen();
        listAll(filteredList);
        while (true) {
            try {
                System.out.println("F = Filter books        S = Sort Books      R = Return to Menu");
                System.out.print("Select an option: ");
                String choice = scanner.nextLine().toLowerCase();
                if (choice.equals("f")) {
                    clearScreen();
                    filteredList = filterBooks(bookList);
                    listAll(filteredList);
                } else if (choice.equals("s")) {
                    clearScreen();
                    sortBooks(filteredList);
                } else if (choice.equals("r")) {
                    clearScreen();
                    break;
                } else {
                    clearScreen();
                    System.err.println("Invalid option!");
                    listAll(bookList);
                }
            } catch (Exception e) {
                System.err.println("Error!");
            }
        }
    }

    // Goodbye Message
    private static void goodbye() {
        System.out.println("Quitting...\nGoodbye!");

    }

    private static void showCredits() {
        System.out.println("***************************************************");
        System.out.println("*     ╔═════════════════════════════════════╗     *");
        System.out.printf("*     ║%-5s %s %5s║     *%n", " ", "LIBRARY MANAGEMENT SYSTEM", " ");
        System.out.println("*     ╚═════════════════════════════════════╝     *");
        System.out.printf("*%30s                   *%n*%32s                 *%n*%30s                   *%n", "Created by:", "Matthew Ingoglia", "Ethan Perez");
        System.out.println("***************************************************");
    }

    // Initialize Library
    private static void initializeLibrary() {
        bookList = importFile(SAVE_FILE);
        if (bookList.isEmpty()) {
            try {
                bookList.add(new FictionBook("Salamander", "Kyne", 2009, "978-1844167401", "Sci-Fi"));
                bookList.add(new FictionBook("Horus Rising", "Abnett", 2006, "978-1849707435", "Sci-Fi"));
                bookList.add(new FictionBook("False Gods", "McNeil", 2006, "978-1844163700", "Sci-Fi"));
                bookList.add(new Book("The Bible", "Apostles", 0, "0"));
                bookList.add(new Textbook("Intro to Software Development", "Ingoglia", 2025, "1", "Computer Science"));
                exportFile(bookList, "Books.dat");
            } catch (ValidationException e) {
                System.err.println("Error creating default books: " + e.getMessage());
                bookList = new ArrayList<>();
            }
        }
        filteredList = bookList;
    }

    // Export book library to save file
    private static void exportFile(ArrayList<Book> list, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            out.writeObject(list);
            System.out.println("Successfully saved library to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving library: " + e.getMessage());
        }
    }

    // Import book library from save file
    @SuppressWarnings("unchecked")
    private static ArrayList<Book> importFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            return (ArrayList<Book>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing library file found. Creating default library.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error importing library: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Lists all books in system
    private static void listAll(ArrayList<Book> list) {
        // Table header:
        System.out.println("╔════╦════════════════════════════════╦════════════════════════════╦════════════════╦════════════════════╗");
        System.out.printf("║ %-2s ║ %30s ║ %26s ║ %14s ║ %18s ║%n", " ", "TITLE", "AUTHOR", "YEAR", "ISBN");
        System.out.println("╠════╬════════════════════════════════╬════════════════════════════╬════════════════╬════════════════════╣");
        
        // Table content:
        for (Book book : list) {
            System.out.printf("║ %s ║ %30s ║ %26s ║ %14d ║ %18s ║%n", Integer.toString(list.indexOf(book) + 1)+".", book.getTitle(), book.getAuthor(), book.getYear(), book.getISBN());
        }
        System.out.println("╚════╩════════════════════════════════╩════════════════════════════╩════════════════╩════════════════════╝");
    }

    // Sorts books by either title, author, publication year, or ISBN
    private static void sortBooks(ArrayList<Book> list) {
        // Sorting options
        Map<String, ChoiceSelection> sortOptions = new LinkedHashMap<>();
        sortOptions.put("1", () -> Comparator.comparing(Book::getTitle).thenComparing(Book::getAuthor).thenComparingInt(Book::getYear));    // Sort by Title
        sortOptions.put("2", () -> Comparator.comparing(Book::getAuthor).thenComparingInt(Book::getYear).thenComparing(Book::getTitle));    // Sort by Author
        sortOptions.put("3", () -> Comparator.comparingInt(Book::getYear).thenComparing(Book::getAuthor).thenComparing(Book::getTitle));    // Sort by Publication Year
        sortOptions.put("4", () -> Comparator.comparing(Book::getISBN));                                                                    // Sort by ISBN
        String[] optionList = {"Title", "Author", "Publication Year", "ISBN"};

        while (true) {
            // Display sorting options to user
            System.out.println("╔═══════════════════════╗");
            System.out.printf("%s %-22s%1$s%n", "║", "Sort by:");
            System.out.println("╠═══════════════════════╬═══╗");
            for (int i = 0; i < optionList.length; i++) {
                System.out.printf("║ %-21s ║ %d ║%n", optionList[i], i + 1);
            }
            System.out.println("╚═══════════════════════╩═══╝");
            
            // Get User Input
            System.out.print("Choose sorting method: ");
            String input = scanner.nextLine();
            if (sortOptions.containsKey(input)) {
                list.sort(sortOptions.get(input).getComparator()); // Sorts book list based on user input
                clearScreen();

                System.out.println("Displaying all results sorted by " + optionList[Integer.parseInt(input) - 1] + ": ");
                listAll(list);
                break;
            } else {
                clearScreen();
                System.out.println("Invalid choice!");
            }
        }
    }

    // Filters books by Title, Author, Publication Year, ISBN, or Genre
    private static ArrayList<Book> filterBooks(ArrayList<Book> list) {
        @SuppressWarnings("resource")

        // Filtering Options
        String[] filterToggles = {"All Books", "Fiction", "Non-Fiction", "Textbooks", "Genre"};

        while (true) {
            // Display Filtering Options
            System.out.println("╔════════════════════╗");
            System.out.printf("║ %-18s ║%n", "Filter by:");
            System.out.println("╠════════════════════╬═══╗");
            for (int i = 0; i < filterToggles.length; i++) {
                System.out.printf("║ %-18s ║ %d ║%n", filterToggles[i], i + 1);
            }
            System.out.println("╚════════════════════╩═══╝");

            // Input Filter
            System.out.print("Select Filter: ");
            String input = scanner.nextLine();

            // Verify input
            String criteria = switch(input) {
                case "1" -> "All";
                case "2" -> "Fiction";
                case "3" -> "Non-Fiction";
                case "4" -> "Textbooks";
                case "5" -> {
                    System.out.print("Search for genre: ");
                    yield scanner.nextLine();
                }
                default -> {
                    yield null;
                }
            };

            try {
                if (criteria != null && !criteria.trim().isEmpty() && criteria != "All") {
                    ArrayList<Book> filteredList = new ArrayList<>();
                    for (Book book : list) {
                        if (book.matchesFilter(criteria)) {
                            filteredList.add(book);
                        }
                    }
                    clearScreen();
                    System.out.println("Displaying results filtered by " + criteria + ": ");
                    return filteredList;
                } else if (criteria == "All") {
                    clearScreen();
                    filteredList = bookList;
                    System.out.println("Displaying all results: ");
                    return filteredList;
                } else if (criteria.trim().isEmpty()) {
                    clearScreen();
                    System.err.println("Invalid filtering option! Genre cannot be blank!\nPlease try again:");
                }
            } catch (NullPointerException e) {
                clearScreen();
                System.err.println("Error: Invalid filtering" + "\nPlease enter a valid option:");
            }
        }
    }

    // Attempt to clear screen based on operation system
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }

    // Waits for user to press enter before continuing
    private static void waitForEnter() {
        System.out.print("\nPress  < ENTER >  to continue...");
        scanner.nextLine();
    }
}


class Book implements BookType, Serializable {
    // Initialize Variables
    private String title;
    private String author;
    private int year;
    private String isbn; // --> ISBN
    private static final long serialVersionUID = 1L;

    public Book(String title, String author, int year, String isbn) throws ValidationException{
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setISBN(isbn);
    }
    
    // Setters
    public void setTitle(String title) throws ValidationException {
        this.title = title;
        if (this.title.length() > 30) {
            throw new ValidationException("Title cannot exceed 30 characters");
        }
    } // Sets book title
    public void setAuthor(String author) throws ValidationException {
        this.author = author;
        if (this.author.length() > 26) {
            throw new ValidationException("Author Name cannot exceed 26 characters");
        }
    } // Sets author name
    public void setYear(int year) throws ValidationException {
        int currentYear = java.time.Year.now().getValue();
        if (year < 0 || year > currentYear) {
            throw new ValidationException("Publication Year must be between 0 and " + currentYear);
        }
        this.year = year;
    } // Sets year of publication
    public void setISBN(String isbn) throws ValidationException {
        this.isbn = isbn;
        if (this.isbn.length() > 14) {
            throw new ValidationException("ISBN cannot exceed 13 characters (excluding the '-')");
        }
    }


    // Getters
    public String getTitle() {
        return title;
    } // Returns book title
    public String getAuthor() {
        return author;
    } // Returns author name
    public int getYear() {
        return year;
    } // Returns year of publication
    public String getISBN() {
        return isbn;
    } // Returns ISBN number
    public String getGenre() {
        return "Non-Fiction";
    }

    @Override
    public boolean matchesFilter(String criteria) {
        return "Non-Fiction".equalsIgnoreCase(criteria) || "All".equalsIgnoreCase(criteria);
    }
}

class FictionBook extends Book {
    private String genre;
    
    public FictionBook(String title, String author, int year, String isbn, String genre) throws ValidationException {
        super(title, author, year, isbn);
        setGenre(genre);
    }


    // Sets genre or throws error
    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty or NULL");
        }
        this.genre = genre;
    }

    // Override getGenre and returns specified genre
    @Override
    public String getGenre() {
        return genre;
    } // Returns genre type

    // Checks if the object matches filter
    @Override
    public boolean matchesFilter(String criteria) {
        return "Fiction".equalsIgnoreCase(criteria) || "All".equalsIgnoreCase(criteria) || this.genre.equalsIgnoreCase(criteria);
    }
}

class Textbook extends Book{
    private String genre;
    
    public Textbook(String title, String author, int year, String isbn, String subject) throws ValidationException {
        super(title, author, year, isbn);
        setGenre(subject);
    }
    
    // Set genre to "[Subject] Textbook" or throw error
    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty or NULL");
        }
        this.genre = genre;
    }

    // Overrides and returns subject
    @Override
    public String getGenre() {
        return genre;
    } // Returns genre

    // Checks if the textbook matches filter
    @Override
    public boolean matchesFilter(String criteria) {
        return "Textbooks".equalsIgnoreCase(criteria) || "All".equalsIgnoreCase(criteria) || getGenre().equalsIgnoreCase(criteria);
    }
}

class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}

class UserInput {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static String getNonEmptyString(String prompt) throws ValidationException {
        String input = getString(prompt).trim();
        if (input.isEmpty()) {
            throw new ValidationException("This Field cannot be empty");
        }
        return input;
    }

    public static int getInt(String prompt, int min, int max) throws ValidationException {
        while (true) {
            try {
                String input = getString(prompt).trim();
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    throw new ValidationException(String.format("Please enter a number between %d and %d", min, max));
                }
                return value;
            } catch (NumberFormatException e) {
                throw new ValidationException("Please enter a valid integer");
            }
        }
    }

    public static String getPatternMatchingString(String prompt, String pattern, String errorMessage) throws ValidationException {
        String input = getNonEmptyString(prompt);
        if (!input.matches(pattern)) {
            throw new ValidationException(errorMessage);
        }
        return input;
    }
}