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
    public Columns<Logo> getColumns() {
        return new Columns<>(Logo.class)
            .addColumn("id", Integer.class, Logo::getId, Logo::setId)
            .addColumn("book_id", Integer.class, Logo::getBookId, Logo::setBookId)
            .addColumn("data", byte[].class, Logo::getData, Logo::setData)
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
