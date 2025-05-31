package org.deltacore.delta.model.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldSetAndGetEmailCorrectly() {
        User user = new User();
        user.setEmail("john@domain.com");

        assertEquals("john@domain.com", user.getEmail());

        System.out.println(user.getEmail());
        List<Email> emails = user.getEmails();
        assertEquals(1, emails.size());

        Email email = emails.getFirst();
        assertEquals("john", email.getUserMail());
        assertEquals("domain.com", email.getDomain());
        assertTrue(email.getIsMain());
        assertFalse(email.getIsVerified());
    }

    @Test
    void shouldReturnNullWhenEmailIsEmpty() {
        User user = new User();
        user.setEmail("");

        assertNull(user.getEmail());
        assertTrue(user.getEmails().isEmpty());
    }

    @Test
    void shouldReturnNullWhenEmailIsNull() {
        User user = new User();
        user.setEmail(null);

        assertNull(user.getEmail());
        assertTrue(user.getEmails().isEmpty());
    }

    @Test
    void shouldReturnOnlyMainEmail() {
        User user = new User();
        user.setEmails(List.of(
                Email.builder().userMail("secondary").domain("x.com").isMain(false).isVerified(true).build(),
                Email.builder().userMail("primary").domain("y.com").isMain(true).isVerified(false).build()
        ));

        assertEquals("primary@y.com", user.getEmail());
    }
}
