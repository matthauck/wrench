package wrench.orm.select;

import static org.junit.Assert.*;

import org.junit.Test;
import wrench.orm.BaseDBTest;
import wrench.orm.model.BaseTable;
import wrench.orm.model.Columns;

public class WhereBuilderTest extends BaseDBTest {


    @Test
    public void testFindFieldName() {

        WhereBuilder<TestBean> where = db.select(TestBean.class);

        // test strings
        assertEquals("first_name", where.findFieldName(TestBean::getFirstName, String.class));
        assertEquals("last_name", where.findFieldName(TestBean::getLastName, String.class));

        // test int/long/short
        assertEquals("id", where.findFieldName(TestBean::getId, Integer.class));
        assertEquals("long_value", where.findFieldName(TestBean::getLongValue, Long.class));
        assertEquals("short_value", where.findFieldName(TestBean::getShortValue, Short.class));
        assertEquals("float_value", where.findFieldName(TestBean::getFloatValue, Float.class));
        assertEquals("double_value", where.findFieldName(TestBean::getDoubleValue, Double.class));

        // test boolean
        assertEquals("read", where.findFieldName(TestBean::isRead, Boolean.class));
        assertEquals("liked", where.findFieldName(TestBean::isLiked, Boolean.class));

        // test blob / byte array
        assertEquals("data", where.findFieldName(TestBean::getData, byte[].class));
        assertEquals("big_data", where.findFieldName(TestBean::getBigData, byte[].class));
    }


    public static class TestBean extends BaseTable {

        private int id;
        private long longValue;
        private short shortValue;
        private float floatValue;
        private double doubleValue;
        private String firstName;
        private String lastName;
        private boolean read;
        private boolean liked;
        private byte[] data;
        private byte[] bigData;


        public TestBean() {
        }

        @Override
        public Columns<TestBean> getColumns() {
            return new Columns<>(TestBean.class)
                .addColumn("id", Integer.class, TestBean::getId, TestBean::setId)
                .addColumn("long_value", Long.class, TestBean::getLongValue, TestBean::setLongValue)
                .addColumn("short_value", Short.class, TestBean::getShortValue, TestBean::setShortValue)
                .addColumn("float_value", Float.class, TestBean::getFloatValue, TestBean::setFloatValue)
                .addColumn("double_value", Double.class, TestBean::getDoubleValue, TestBean::setDoubleValue)
                .addColumn("first_name", String.class, TestBean::getFirstName, TestBean::setFirstName)
                .addColumn("last_name", String.class, TestBean::getLastName, TestBean::setLastName)
                .addColumn("read", Boolean.class, TestBean::isRead, TestBean::setRead)
                .addColumn("liked", Boolean.class, TestBean::isLiked, TestBean::setLiked)
                .addColumn("data", byte[].class, TestBean::getData, TestBean::setData)
                .addColumn("big_data", byte[].class, TestBean::getBigData, TestBean::setBigData)
                .done();
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public void setId(int id) {
            this.id = id;
        }

        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }

        public short getShortValue() {
            return shortValue;
        }

        public void setShortValue(short shortValue) {
            this.shortValue = shortValue;
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

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public byte[] getBigData() {
            return bigData;
        }

        public void setBigData(byte[] bigData) {
            this.bigData = bigData;
        }

        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(float floatValue) {
            this.floatValue = floatValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }
    }


}
