package wrench.orm.test.model;

import wrench.orm.model.BaseTable;
import wrench.orm.model.Columns;

/**
 * @author mhauck
 */
public class Logo extends BaseTable {

    private int id;
    private int bookId;
    private byte[] data;

    @Override
    public Columns getColumns() {
        return new Columns()
            .addColumn("id", Integer.class, this::getId, this::setId)
            .addColumn("book_id", Integer.class, this::getBookId, this::setBookId)
            .addColumn("data", byte[].class, this::getData, this::setData)
            .done();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
