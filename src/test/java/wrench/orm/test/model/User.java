package wrench.orm.test.model;

import wrench.orm.model.BaseTable;
import wrench.orm.model.Columns;
import wrench.orm.model.associations.ForeignKey;
import wrench.orm.model.associations.ForeignKeys;
import wrench.orm.model.associations.HasMany;

/**
 * @author mhauck
 */
public class User extends BaseTable implements HasMany {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String salt;
    private String passwordHash;

    public User() { }

    @Override
    public Columns<User> getColumns() {
        return new Columns<>(User.class)
            .addColumn("id", Integer.class, User::getId, User::setId)
            .addColumn("first_name", String.class, User::getFirstName, User::setFirstName)
            .addColumn("last_name", String.class, User::getLastName, User::setLastName)
            .addColumn("email", String.class, User::getEmail, User::setEmail)
            .addColumn("salt", String.class, User::getSalt, User::setSalt)
            .addColumn("password_hash", String.class, User::getPasswordHash, User::setPasswordHash)
            .done();
    }

    @Override
    public ForeignKeys hasMany() {
        return new ForeignKeys<User>()
            .add(new ForeignKey<>("user_id", User.class, Book.class, this::getId));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "User[id=" + id + "; name=" + firstName + " " + lastName + "; email=" + email + "]";
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }
        User that = (User) obj;

        return email.equals(that.email);
    }
}


