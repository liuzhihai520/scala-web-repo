package core.utils.xutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * ���ļ���������ṩѹ������ѹ�����Ĺ�����
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-11-22 ����9:51:01 $
 */
public abstract class CompressUtils {

    /**
     * ѹ�������࣬�ͻ���ͨ��ʵ�ִ˷�����������д���������
     */
    public static interface CompressHandler {

        /**
         * ����������д���ķ��������Խ���Ҫ����ѹ��������д���������С�
         * <p>
         * {@link OutputStream#close()} �������Բ����ã���Ϊ�ڹ��߷����л���йرղ�����
         *
         * @param out
         *            �����
         * @throws IOException
         *             ���� I/O ����ʱӦ���׳����쳣
         */
        void handle(OutputStream out) throws IOException;
    }

    private static final int BUFFER_SIZE = 8192;

    /**
     * �� GZip ��ʽ�����������ѹ���󱣴浽�ļ���<strong>ע�⣺���Ŀ���ļ��Ѿ����ڣ���Ḳ�������ļ������ݡ�</strong>
     * <p>
     * ����������ʹ�õ����ӣ�
     *
     * <pre>
     * CompressUtils.gzCompress(&quot;foo.txt&quot;, new CompressHandler() {
     *     &#064;Override
     *     public void handle(OutputStream out) throws IOException {
     *         out.write(&quot;This is a test.&quot;.getBytes());
     *     }
     * });
     * </pre>
     *
     * <pre>
     * CompressUtils.gzCompress(&quot;bar.txt&quot;, new CompressHandler() {
     *     &#064;Override
     *     public void handle(OutputStream out) throws IOException {
     *         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
     *         writer.write(&quot;This is a test.&quot;);
     *         writer.flush(); // ˢ�»���
     *     }
     * });
     * </pre>
     *
     * @param dstFile
     *            Ŀ���ļ�����ѹ������ļ�
     * @param handler
     *            ѹ�������������
     * @throws IOException
     *             ��ѹ�������з��������������׳����쳣
     */
    public static void gzCompress(String dstFile, CompressHandler handler) throws IOException {
        if (!dstFile.endsWith(".gz")) {
            dstFile = dstFile + ".gz";
        }

        GZIPOutputStream gzos = null;
        FileChannel channel = null;
        FileLock lock = null;

        try {
            FileOutputStream fos = new FileOutputStream(dstFile);
            channel = fos.getChannel();
            lock = channel.lock();

            gzos = new GZIPOutputStream(fos);
            handler.handle(gzos);
        }
        catch (IOException e) {
            throw new IOException("Error occurred while compressing stream into [" + dstFile + "].", e);
        }
        finally {
            clean(null, gzos, lock, channel);
        }
    }

    /**
     * ��ָ�����ļ��� GZip ��ʽ����ѹ����<strong>ע�⣺���Ŀ���ļ��Ѿ����ڣ���Ḳ�������ļ������ݡ�</strong>
     *
     * @param srcFile
     *            Դ�ļ�������ѹ�����ļ�
     * @param dstFile
     *            Ŀ���ļ�����ѹ������ļ�
     * @throws IOException
     *             ��Դ�ļ������ڡ�ѹ�������з��������������׳����쳣
     */
    public static void gzCompress(String srcFile, String dstFile) throws IOException {
        File file2Compress = new File(srcFile);
        if (!file2Compress.exists()) {
            throw new IOException("The file to compress named [" + srcFile + "] does not exist.");
        }

        if (!dstFile.endsWith(".gz")) {
            dstFile = dstFile + ".gz";
        }

        FileInputStream fis = null;
        GZIPOutputStream gzos = null;
        FileChannel channel = null;
        FileLock lock = null;

        try {
            FileOutputStream fos = new FileOutputStream(dstFile);

            // ȡ���ļ�������֤��д�ļ������в��ᱻ�������̶���������������
            channel = fos.getChannel();
            lock = channel.lock();

            fis = new FileInputStream(srcFile);
            gzos = new GZIPOutputStream(fos);

            byte[] buffer = new byte[BUFFER_SIZE];
            for (int len = 0; (len = fis.read(buffer)) != -1;) {
                gzos.write(buffer, 0, len);
            }
        }
        catch (IOException e) {
            throw new IOException("Error occurred while compressing [" + srcFile + "] into [" + dstFile + "].", e);
        }
        finally {
            clean(fis, gzos, lock, channel);
        }
    }

    /**
     * ��ָ�����ļ��� GZip ��ʽ���н�ѹ����<strong>ע�⣺���Ŀ���ļ��Ѿ����ڣ���Ḳ�������ļ������ݡ�</strong>
     *
     * @param srcFile
     *            Դ�ļ���������ѹ���ļ�
     * @param dstFile
     *            Ŀ���ļ�������ѹ����ļ�
     * @throws IOException
     *             ��Դ�ļ������ڡ���ѹ�������з��������������׳����쳣
     */
    public static void gzDecompress(String srcFile, String dstFile) throws IOException {
        File compressedFile = new File(srcFile);
        if (!compressedFile.exists()) {
            throw new IOException("The file to decompress named [" + srcFile + "] does not exist.");
        }

        GZIPInputStream gzis = null;
        FileOutputStream fos = null;

        try {
            gzis = new GZIPInputStream(new FileInputStream(srcFile));
            fos = new FileOutputStream(dstFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            for (int len = 0; (len = gzis.read(buffer)) != -1;) {
                fos.write(buffer, 0, len);
            }
        }
        catch (IOException e) {
            throw new IOException("Error occurred while decompressing [" + srcFile + "] into [" + dstFile + "].", e);
        }
        finally {
            clean(gzis, fos);
        }
    }

    /**
     * ��ָ�����ļ��� Zip ��ʽ����ѹ����Ŀǰֻ֧�ֵ����ļ�����֧�ֶ�Ŀ¼����ѹ��������
     * <p>
     * <strong>ע�⣺���Ŀ���ļ��Ѿ����ڣ���Ḳ�������ļ������ݡ�</strong>
     *
     * @param srcFile
     *            Դ�ļ�������ѹ�����ļ�
     * @param dstFile
     *            Ŀ���ļ�����ѹ������ļ�
     * @throws IOException
     *             ��Դ�ļ������ڡ�ѹ�������з��������������׳����쳣
     */
    public static void zipCompress(String srcFile, String dstFile) throws IOException {
        File file2Compress = new File(srcFile);
        if (!file2Compress.exists()) {
            throw new IOException("The file to compress named [" + srcFile + "] does not exist.");
        }

        if (!dstFile.endsWith(".zip")) {
            dstFile = dstFile + ".zip";
        }

        FileInputStream fis = null;
        ZipOutputStream zos = null;
        FileChannel channel = null;
        FileLock lock = null;

        try {
            FileOutputStream fos = new FileOutputStream(dstFile);
            channel = fos.getChannel();
            lock = channel.lock();

            fis = new FileInputStream(srcFile);
            zos = new ZipOutputStream(fos);

            ZipEntry entry = new ZipEntry(file2Compress.getName());
            entry.setCompressedSize(file2Compress.length());
            entry.setTime(file2Compress.lastModified());
            zos.putNextEntry(entry);

            byte[] buffer = new byte[BUFFER_SIZE];
            for (int len = 0; (len = fis.read(buffer)) != -1;) {
                zos.write(buffer, 0, len);
            }
        }
        catch (IOException e) {
            throw new IOException("Error occurred while compressing [" + srcFile + "] into [" + dstFile + "].");
        }
        finally {
            clean(fis, zos, lock, channel);
        }
    }

    /**
     * ��ָ�����ļ��� Zip ��ʽ���н�ѹ����Ŀǰֻ֧�ֵ����ļ�����֧�ֶ�Ŀ¼���н�ѹ������
     * <p>
     * <strong>ע�⣺���Ŀ���ļ��Ѿ����ڣ���Ḳ�������ļ������ݡ�</strong>
     *
     * @param srcFile
     *            Դ�ļ���������ѹ���ļ�
     * @param dstFile
     *            Ŀ���ļ�������ѹ����ļ�
     * @throws IOException
     *             ��Դ�ļ������ڡ���ѹ�������з��������������׳����쳣
     */
    public static void zipDecompress(String srcFile, String dstFile) throws IOException {
        File compressedFile = new File(srcFile);
        if (!compressedFile.exists()) {
            throw new IOException("The file to decompress named [" + srcFile + "] does not exist.");
        }

        ZipInputStream zis = null;
        FileOutputStream fos = null;

        try {
            zis = new ZipInputStream(new FileInputStream(srcFile));
            fos = new FileOutputStream(dstFile);

            ZipEntry entry = zis.getNextEntry();
            if (entry == null) {
                throw new IOException("The file to decompress named [" + srcFile + "] has no zip entry.");
            }

            byte[] buffer = new byte[BUFFER_SIZE];
            for (int len = 0; (len = zis.read(buffer)) != -1;) {
                fos.write(buffer, 0, len);
            }
        }
        catch (IOException e) {
            throw new IOException("Error occurred while decompressing [" + srcFile + "] into [" + dstFile + "].");
        }
        finally {
            clean(zis, fos);
        }
    }

    /**
     * �ͷ���Դ���ر������������
     */
    private static void clean(InputStream in, OutputStream out) throws IOException {
        FileUtils.close(in);
        FileUtils.close(out);
    }

    /**
     * �ͷ���Դ�������ر�������������ر��ļ�ͨ�����ͷ��ļ�����
     */
    private static void clean(InputStream in, OutputStream out, FileLock lock, FileChannel channel) throws IOException {
        FileUtils.close(in);

        // �ͷ��ļ���
        if (lock != null) {
            lock.release();
        }

        // �ڹر�ѹ�������֮���ٹر�ͨ��������ȹر�ͨ���ᵼ�� ѹ���ļ��ĸ�ʽ����
        FileUtils.close(out);
        if (channel != null) {
            channel.close();
        }
    }

}
