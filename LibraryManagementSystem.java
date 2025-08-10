import java.io.*;
import java.util.*;

class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    int bookId;
    String title;
    String author;
    boolean isIssued;

    Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }
}

class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    int memberId;
    String name;
    List<Integer> issuedBooks;

    Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.issuedBooks = new ArrayList<>();
    }
}

public class LibraryManagementSystem {
    static final String BOOK_FILE = "books.dat";
    static final String MEMBER_FILE = "members.dat";
    static Map<Integer, Book> books = new HashMap<>();
    static Map<Integer, Member> members = new HashMap<>();

    public static void main(String[] args) {
        loadData();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add Book");
            System.out.println("2. Register Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. View All Books");
            System.out.println("6. View All Members");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    int bookId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    books.put(bookId, new Book(bookId, title, author));
                    saveData();
                    System.out.println("Book added successfully.");
                    break;

                case 2:
                    System.out.print("Enter Member ID: ");
                    int memberId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    members.put(memberId, new Member(memberId, name));
                    saveData();
                    System.out.println("Member registered successfully.");
                    break;

                case 3:
                    System.out.print("Enter Member ID: ");
                    memberId = sc.nextInt();
                    System.out.print("Enter Book ID: ");
                    bookId = sc.nextInt();
                    issueBook(memberId, bookId);
                    saveData();
                    break;

                case 4:
                    System.out.print("Enter Member ID: ");
                    memberId = sc.nextInt();
                    System.out.print("Enter Book ID: ");
                    bookId = sc.nextInt();
                    returnBook(memberId, bookId);
                    saveData();
                    break;

                case 5:
                    viewAllBooks();
                    break;

                case 6:
                    viewAllMembers();
                    break;

                case 7:
                    System.out.println("Exiting... Data saved.");
                    saveData();
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 7);

        sc.close();
    }

    static void issueBook(int memberId, int bookId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null) {
            System.out.println("Book not found.");
            return;
        }
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        if (b.isIssued) {
            System.out.println("Book already issued.");
        } else {
            b.isIssued = true;
            m.issuedBooks.add(bookId);
            System.out.println("Book issued successfully.");
        }
    }

    static void returnBook(int memberId, int bookId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null || m == null) {
            System.out.println("Book or member not found.");
            return;
        }
        if (m.issuedBooks.contains(bookId)) {
            b.isIssued = false;
            m.issuedBooks.remove(Integer.valueOf(bookId));
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("This member did not issue this book.");
        }
    }

    static void viewAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            for (Book b : books.values()) {
                System.out.println("ID: " + b.bookId + ", Title: " + b.title +
                                   ", Author: " + b.author + ", Issued: " + b.isIssued);
            }
        }
    }

    static void viewAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            for (Member m : members.values()) {
                System.out.println("ID: " + m.memberId + ", Name: " + m.name +
                                   ", Issued Books: " + m.issuedBooks);
            }
        }
    }

    static void saveData() {
        try (ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(BOOK_FILE));
             ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(MEMBER_FILE))) {
            oos1.writeObject(books);
            oos2.writeObject(members);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    static void loadData() {
        try (ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream(BOOK_FILE));
             ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(MEMBER_FILE))) {
            books = (Map<Integer, Book>) ois1.readObject();
            members = (Map<Integer, Member>) ois2.readObject();
        } catch (FileNotFoundException e) {
            // First run - no files yet
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
