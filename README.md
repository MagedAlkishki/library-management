# Library Management

A console app for managing a library's books — add, borrow, return, remove, and browse by genre — with live search against the Open Library API so you can pull in a real book's title, author and publish year instead of typing them by hand.

## Stack

Java 21 &middot; Maven &middot; [org.json](https://github.com/stleary/JSON-java) for parsing &middot; `java.net.http.HttpClient` for the Open Library request &mdash; no other dependencies.

## Running it

```
mvn compile exec:java
```

Then just follow the menu.

## Try it

- Pick **8** to search Open Library for a real book (try "Dune" or "Clean Code"), then add one of the results straight into your library with a genre of your choice.
- Pick **1** to add a book by hand instead.
- Borrow and return books by title with **2** and **3**, and list everything currently available with **4**.
- Pick **6** to filter the whole library down to one genre.

## Project layout

```
src/main/java/
  Book.java        A single book: title, author, genre, borrowed state
  Genre.java       The genres a book can be filed under
  Library.java     Holds the books, handles add/remove/borrow/return/listing
  BookSearch.java  Talks to the Open Library API and parses the JSON response
  Main.java        The menu loop that ties it all together
```
