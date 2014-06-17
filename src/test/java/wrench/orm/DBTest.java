package wrench.orm;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;
import wrench.orm.select.WhereOps;
import wrench.orm.test.model.*;

/**
 * @author mhauck
 */
public class DBTest extends BaseDBTest {

    @Test
    public void testInsert() throws SQLException {
        User user = new User();
        user.setEmail("email1");
        user.setFirstName("joe");
        user.setLastName("smith");
        user.setPasswordHash("---");
        user.setSalt("salty");

        db.insert(user);

        User found = db.find(user.getId(), User.class);
        assertEquals(found.getId(), user.getId());
        assertEquals(found.getEmail(), "email1");
        assertEquals(found.getFirstName(), "joe");
        assertEquals(found.getLastName(), "smith");
        assertEquals(found.getPasswordHash(), "---");
        assertEquals(found.getSalt(), "salty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertWithNonNewRecord() throws SQLException {
        User u1 = blueprint.makeUser();
        db.insert(u1);

        db.insert(u1);
    }

    @Test
    public void testUpdate() throws SQLException {
        User user = new User();
        user.setEmail("joe@foo.com");
        user.setFirstName("joe");
        user.setLastName("smith");
        user.setPasswordHash("hash1");
        user.setSalt("sea");

        db.insert(user);

        user.setEmail("bob@foo.com");
        user.setFirstName("bob");
        user.setLastName("jones");
        user.setPasswordHash("hash2");
        user.setSalt("rock");

        int changed = db.update(user);

        assertEquals("update should succeed", 1, changed);

        User found = db.find(user.getId(), User.class);
        assertEquals(found.getId(), user.getId());
        assertEquals(found.getEmail(), "bob@foo.com");
        assertEquals(found.getFirstName(), "bob");
        assertEquals(found.getLastName(), "jones");
        assertEquals(found.getPasswordHash(), "hash2");
        assertEquals(found.getSalt(), "rock");
    }

    @Test
    public void testUpdateWithWrongId() throws SQLException {
        User u1 = blueprint.makeUser();
        db.insert(u1);

        u1.setId(23);

        int changed = db.update(u1);

        assertEquals("update should fail for invalid primary key", 0, changed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNewRecord() throws SQLException {
        User newUser = new User();
        db.update(newUser);
    }

    @Test
    public void testBoolean() throws SQLException {
        User u1 = blueprint.makeUser();
        db.insert(u1);

        Book b1 = blueprint.makeBook("book1", u1);
        b1.setRead(true);
        db.insert(b1);

        Book b2 = blueprint.makeBook("book2", u1);
        b2.setRead(false);
        db.insert(b2);

        assertTrue(db.find(b1.getId(), Book.class).isRead());
        assertFalse(db.find(b2.getId(), Book.class).isRead());
    }

    @Test
    public void testBlob() throws SQLException {
        User u1 = blueprint.makeUser();
        db.insert(u1);

        Book b1 = blueprint.makeBook("book1", u1);
        db.insert(b1);

        Logo logo = blueprint.makeLogo(b1, new byte[]{1, 2, 3});
        db.insert(logo);

        Logo found = db.find(logo.getId(), Logo.class);

        assertArrayEquals(found.getData(), new byte[]{1, 2, 3});
    }

    @Test
    public void testHasMany() throws SQLException {
        User u1 = blueprint.makeUser();
        db.insert(u1);

        User u2 = blueprint.makeUser();
        db.insert(u2);

        Book b1 = blueprint.makeBook("book1", u1);
        db.insert(b1);

        Book b2 = blueprint.makeBook("book2", u1);
        db.insert(b2);

        assertEquals(db.join(u1, Book.class), Arrays.asList(b1, b2));
        assertEquals(db.join(u2, Book.class), new ArrayList<Book>());
    }

    @Test
    public void testBelongsTo() throws SQLException {
        User u1 = blueprint.makeUser();
        db.insert(u1);

        User u2 = blueprint.makeUser();
        db.insert(u2);

        Book b1 = blueprint.makeBook("book1", u1);
        db.insert(b1);

        Book b2 = blueprint.makeBook("book2", u2);
        db.insert(b2);

        assertEquals(db.join(b1, User.class), u1);
        assertEquals(db.join(b2, User.class), u2);
    }

    @Test
    public void testSelect() throws SQLException {
        User user = new User();
        user.setEmail("joe@foo.com");
        user.setFirstName("joe");
        user.setLastName("smith");
        user.setPasswordHash("hash1");
        user.setSalt("sea");

        db.insert(user);

        List<User> foundList;

        foundList = db.select(User.class)
            .where(User::getFirstName, "joe", WhereOps.EQL)
            .find();

        assertEquals(1, foundList.size());

        foundList = db.select(User.class)
            .where(User::getFirstName, "joe", WhereOps.EQL)
            .where(User::getEmail, "joe%", WhereOps.LIKE)
            .find();

        assertEquals(1, foundList.size());

        foundList = db.select(User.class)
            .where(User::getFirstName, "bob", WhereOps.EQL)
            .find();

        assertEquals(0, foundList.size());

        foundList = db.select(User.class)
            .where(User::getFirstName, "joe", WhereOps.EQL)
            .where(User::getEmail, "bob%", WhereOps.LIKE)
            .find();

        assertEquals(0, foundList.size());

    }
}
