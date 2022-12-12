package cn.apisium.library;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;
import java.util.Scanner;

/**
 * Base item
 */
@JsonPropertyOrder({ "barcode", "author", "title", "type", "year", "isbn" })
public sealed class Item permits Book, Multimedia {
    @JsonProperty("Barcode")
    private String barcode;
    @JsonProperty("Author/Artist")
    private String author;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("ISBN")
    private String isbn;

    public Item() { }
    public Item(Scanner in) {
        System.out.print("Creating Item:");
        System.out.print("Barcode: ");
        setBarcode(in.nextLine());
        System.out.print("Author/Artist: ");
        setAuthor(in.nextLine());
        System.out.print("Title: ");
        setTitle(in.nextLine());
        System.out.print("Type: ");
        setType(in.nextLine());
        System.out.print("Year: ");
        setYear(in.nextLine());
        System.out.print("ISBN: ");
        setIsbn(in.nextLine());
    }

    protected Item(Item other) {
        this.barcode = other.barcode;
        this.author = other.author;
        this.title = other.title;
        this.type = other.type;
        this.year = other.year;
        this.isbn = other.isbn;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @SuppressWarnings("unused")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SuppressWarnings("unused")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SuppressWarnings("unused")
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @SuppressWarnings("unused")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @JsonIgnore
    public boolean isBook() {
        return type.equals("Book");
    }

    @Override
    public String toString() {
        return "Item{" +
                "barcode='" + barcode + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", year='" + year + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!Objects.equals(barcode, item.barcode)) return false;
        if (!Objects.equals(author, item.author)) return false;
        if (!Objects.equals(title, item.title)) return false;
        if (!Objects.equals(type, item.type)) return false;
        if (!Objects.equals(year, item.year)) return false;
        return Objects.equals(isbn, item.isbn);
    }

    @Override
    public int hashCode() {
        int result = barcode != null ? barcode.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
        return result;
    }
}
