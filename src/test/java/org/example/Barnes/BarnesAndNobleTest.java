package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BarnesAndNobleTest {


    @Test
    @DisplayName("specification-based")
    void specificationBasedTest() {
        // creates new book
        Book book = new Book("100", 20, 3); // ISBN 100, price $20, quantity 3


        // book database
        BookDatabase database = new BookDatabase() {
            @Override
            public Book findByISBN(String ISBN) {
                return book;
            }
        };

        BuyBookProcess process = new BuyBookProcess() {
            @Override
            public void buyBook(Book book, int amount) {
                // does nothing
            }
        };


        BarnesAndNoble store = new BarnesAndNoble(database, process);
        // creates cart
        Map<String, Integer> cart = new HashMap<>();
        cart.put("100", 2);

        PurchaseSummary summary = store.getPriceForCart(cart);

        assertEquals(40, summary.getTotalPrice()); // 2 books $20 each = $40
        assertTrue(summary.getUnavailable().isEmpty());


    }


    @Test
    @DisplayName("structural-based")
    void structuralBasedTest() {
        // creates new book
        Book book = new Book("101", 25, 3); // 3 books in stock

        // book database
        BookDatabase database = new BookDatabase() {
            @Override
            public Book findByISBN(String ISBN) {
                return book;
            }
        };

        BuyBookProcess process = new BuyBookProcess() {
            @Override
            public void buyBook(Book book, int amount) {
                // does nothing
            }
        };

        BarnesAndNoble store = new BarnesAndNoble(database, process);

        // requests more books than in stock
        Map<String, Integer> cart = new HashMap<>();
        cart.put("101", 5);

        PurchaseSummary summary = store.getPriceForCart(cart);

        // assert verify total price of books from available stock
        assertEquals(75, summary.getTotalPrice()); // 3 books in stock $25 each = $75

        // assert unavailable items recorded
        assertFalse(summary.getUnavailable().isEmpty());
        assertEquals(2, summary.getUnavailable().get(book)); // 5 requested / 3 available / 2 unavailable



    }


}