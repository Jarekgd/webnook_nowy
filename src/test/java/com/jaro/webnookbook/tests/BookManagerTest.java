package com.jaro.webnookbook.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jaro.webnookbook.managers.BookManager;
import com.jaro.webnookbook.models.Book;
import org.junit.jupiter.api.Disabled;

public class BookManagerTest {
    @Disabled
    @Test
    void testGetBookBySerialNo() {
        Book book = BookManager.getBookBySerialNo("B001");
        assertNotNull(book, "Book should exist");
        assertEquals("The Shining", book.getName(), "Book title should match");
    }
@Disabled
    @Test
    void testUpdateStock() {
        boolean updated = BookManager.updateStock("B001", 10);
        assertTrue(updated, "Stock should be updated");
    }
}
