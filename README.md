# Library Management

A console app for managing a library's books — add, borrow, return, remove, edit, and browse by genre — with live search against the Open Library API so you can pull in a real book's title, author and publish year instead of typing them by hand. Your library is saved to disk automatically, due dates are tracked on anything you borrow, and overdue books are flagged.

## Stack

Java 21 &middot; Maven &middot; [org.json](https://github.com/stleary/JSON-java) for parsing and local storage &middot; `java.net.http.HttpClient` for the Open Library request &middot; JUnit 5 for tests &mdash; no other dependencies.

## Running it

```
mvn compile exec:java
```

Then just follow the menu. Everything you do is saved to `library-data.json` next to the project as you go, so closing and reopening the app picks up right where you left off.

## Running the tests

```
mvn test
```

Covers the borrowing/returning rules, duplicate-title rejection, overdue detection, local search, editing, and the save/load round trip (including recovering gracefully from a missing or corrupted save file).

## Try it

- Pick **8** to search Open Library for a real book (try "Dune" or "Clean Code"), then add one of the results straight into your library with a genre of your choice.
- Pick **1** to add a book by hand instead — adding a title that's already in the library is rejected rather than creating a duplicate.
- Borrow and return books by title with **2** and **3** — borrowing sets a 14-day due date, and **10** lists anything currently overdue.
- Pick **9** to search your own library by title or author instead of Open Library.
- Pick **11** to fix a typo in a book's author or move it to a different genre.
- Pick **6** to filter the whole library down to one genre.

## Project layout

```
src/main/java/
  Book.java           A single book: title, author, genre, borrowed state, due date
  Genre.java          The genres a book can be filed under
  Library.java        Holds the books, handles add/remove/borrow/return/search/edit/listing
  LibraryStorage.java Saves and loads the library as JSON so it survives between runs
  BookSearch.java     Talks to the Open Library API and parses the JSON response
  Main.java           The menu loop that ties it all together
src/test/java/
  LibraryTest.java        Unit tests for the library's borrowing/search/edit rules
  LibraryStorageTest.java Unit tests for the save/load round trip and error recovery
```
