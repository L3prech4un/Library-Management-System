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
- Java JDK 8 or newer
- Terminal/Command Line interface

---

## Getting Started

### Installation Instructions:

