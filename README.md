# Library Management System

A console-based Java application for managing library database with advanced searching, sorting, filtering, and persistent data storage.

---

## Features

### Book Management
- **Add Books**: Supports Fiction, Non-Fiction, and Textbook types with validated metadata
- **Remove Books**: Remove book from database using ISBN
- **Data Persistence**: Automatic loading/saving to `Books.dat` using Java serialization

### Search & Organization
- Recursive title searching
- Filtering books by:
  - Book type (Fiction/Textbook/Non-Fiction)
  - Specific Genre/Subject for Fiction and Text books, respectively.
- Sorting books by:
  - Title
  - Author
  - Publication Year
  - ISBN

### User Input Validation System
- Input constraints for:
  - Titles (30 character max)
  - Authors (26 character max)
  - Years (0 - current year)
  - ISBNs (14 character max)
  - Genres (alphabetic/hyphen validation)

---

## Requirements
- Java JDK 8 or later
- Terminal/Command Line interface (e.g. Bash, Powershell, etc.)
- git 2.49.0 or later

---

## Quick Start

### Installation and Compiling
In a new Terminal/Command Line interface:
```
git clone https://github.com/L3prech4un/Library-Management-System.git
cd Library-Management-System/
javac *.java
```

### Running
1. Navigate to the program directory
2. Execute the following:
```
java SystemManager
```

---

## Usage

### Follow on-screen menu options
1. **Show Library:** View library database
2. **Add Book:** Add new book to library database
3. **Remove Book:** Remove book from library database via ISBN-13 number
4. **Search:** Uses recursive searching algorithm to search for a book based on a search term
5. **Save Changes:** Save changes to database
6. **Exit:** Close the application

### Sorting
When viewing the library users can sort the order of books displayed based on the following attributes:
1. Title
2. Author
3. Publication Year
4. ISBN

When prompted, choose option "S" to do this and follow on-screen instructions.

### Filtering
Books within the library can be filtered by:
1. All
2. Fiction
3. Non-Fiction
4. User specified genre

To use this feature choose option "F" when prompted and follow on-screen instructions.

### Data Persistance
On initialization, the program will attempt to load a save file. **Changes are not automatically saved. In order to save changes made to the library, select option 5. in the main menu.** If a save file does not exist the program will generate one with several example books included. These can be removed at any time by the user.

### Searching
Users can search for specific books by selecting option 4. in the main menu, then following the on-scree instructions.

### Adding/Removing Books
Users can modify the contents of the library database by adding or removing a book. The program will attempt to validate their entry before adding it the system.
- **Adding Books:** Select option 2. in the main menu and follow the on-screen instructions
- **Removing Books:** Select option 3. in the main menu, then enter the book's ISBN number

---

## UML Class Diagram
![UML Class Diagram](https://github.com/L3prech4un/Library-Management-System/blob/main/UML_Diagram.png)

---

## Credits
Created by Matthew Ingoglia
