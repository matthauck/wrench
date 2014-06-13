package orm;

import orm.test.model.Book;
import orm.test.model.User;

/**
 * @author mhauck
 */
public class Blueprint {

    private int sn = 1;

    private String sn() {
        return String.format("%d", sn++);
    }

    public User makeUser() {
        return makeUser("email-" + sn() + "@foo.com");
    }

    public User makeUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName("joe");
        user.setLastName("smith");
        user.setPasswordHash("---");
        user.setSalt("salty");

        return user;
    }

    public Book makeBook(String title, User user) {
        Book book = new Book();

        book.setTitle(title);
        book.setDescription("a great book");
        book.setUserId(user.getId());

        return book;
    }




}
