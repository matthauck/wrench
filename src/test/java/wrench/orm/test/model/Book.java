package orm.test.model;

import orm.model.BaseTable;
import orm.model.Columns;
import orm.model.associations.BelongsTo;
import orm.model.associations.ForeignKey;
import orm.model.associations.ForeignKeys;

/**
 * @author mhauck
 */
public class Book extends BaseTable implements BelongsTo {

    private int id;
    private int userId;
    private boolean read;
    private String title;
    private String description;

    public Book() { }

    @Override
    public Columns getColumns() {
        return new Columns()
            .addColumn("id", Integer.class, this::getId, this::setId)
            .addColumn("user_id", Integer.class, this::getUserId, this::setUserId)
            .addColumn("read", Boolean.class, this::isRead, this::setRead)
            .addColumn("title", String.class, this::getTitle, this::setTitle)
            .addColumn("description", String.class, this::getDescription, this::setDescription)
            .done();
    }

    @Override
    public ForeignKeys belongsTo() {
        return new ForeignKeys<Book>()
            .add(new ForeignKey<>("id", Book.class, User.class, this::getUserId));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "Book[id=" + id + "; title=" + title + "; userId=" + userId + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Book)) {
            return false;
        }
        Book that = (Book) obj;
        return userId == that.userId && title.equals(that.title);
    }

}
