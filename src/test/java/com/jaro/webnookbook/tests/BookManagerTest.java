package com.jaro.webnookbook.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jaro.webnookbook.managers.BookManager;
import com.jaro.webnookbook.models.Book;

public class BookManagerTest {
    @Test
    void testGetBookBySerialNo() {
        Book book = BookManager.getBookBySerialNo("B001");
        assertNotNull(book, "Book should exist");
        assertEquals("Java Book", book.getName(), "Book title should match");
    }

    @Test
    void testUpdateStock() {
        boolean updated = BookManager.updateStock("B001", 5);
        assertTrue(updated, "Stock should be updated");
    }
}
