package orm.types;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * @author mhauck
 */
public class BlobTypeMapping implements TypeMapping<byte[]> {

    @Override
    public Class<byte[]> getTargetType() {
        return byte[].class;
    }

    @Override
    public Object fromJava(byte[] value) {
        try {
            return new SerialBlob(value);
        } catch (SQLException e) {
            // what do do here?
            return null;
        }
    }

    @Override
    public byte[] fromSql(Object value, Class<byte[]> javaType) {
        if (value instanceof Blob) {
            Blob blob = (Blob) value;
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            InputStream stream = null;

            try {
                stream = blob.getBinaryStream();

                byte[] buff = new byte[8192];
                int read;
                while ((read = stream.read(buff)) >= 0) {
                    bout.write(buff, 0, read);
                }

            } catch (SQLException | IOException e) {
                // what to do here?
                return null;

            } finally {
                if (stream != null) {
                    try { stream.close(); } catch (IOException e) {}
                }

            }

            return bout.toByteArray();

        } else {
            return javaType.cast(value);
        }
    }
}
