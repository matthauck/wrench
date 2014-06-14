package orm.test.model;

import orm.model.BaseTable;
import orm.model.Columns;
import orm.model.associations.ForeignKeys;
import orm.model.associations.HasMany;
import orm.model.associations.ForeignKey;

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
    public Columns getColumns() {
        return new Columns()
            .addColumn("id", Integer.class, this::getId, this::setId)
            .addColumn("first_name", String.class, this::getFirstName, this::setFirstName)
            .addColumn("last_name", String.class, this::getLastName, this::setLastName)
            .addColumn("email", String.class, this::getEmail, this::setEmail)
            .addColumn("salt", String.class, this::getSalt, this::setSalt)
            .addColumn("password_hash", String.class, this::getPasswordHash, this::setPasswordHash)
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


