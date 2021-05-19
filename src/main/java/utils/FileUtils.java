package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;

public final class FileUtils {
    public static long BYTES_PER_KILOBYTE = 1024;
    public static long BYTES_PER_MEGABYTE = 1024 * BYTES_PER_KILOBYTE;
    public static long BYTES_PER_GIGABYTE = 1024 * BYTES_PER_MEGABYTE;
    public static long BYTES_PER_TERABYTE = 1024 * BYTES_PER_GIGABYTE;
    public static long BYTES_PER_PETABYTE = 1024 * BYTES_PER_TERABYTE;

    public static String KILOBYTE_UNIT_APPEND = "KB";
    public static String MEGABYTE_UNIT_APPEND = "MB";
    public static String GIGABYTE_UNIT_APPEND = "GB";
    public static String TERABYTE_UNIT_APPEND = "TB";
    public static String PETABYTE_UNIT_APPEND = "PB";

    public static String getByteValueString(long numBytes) {

        if (numBytes >= BYTES_PER_PETABYTE) {
            return getUnitStr(numBytes, BYTES_PER_PETABYTE, PETABYTE_UNIT_APPEND);
        } else if (numBytes >= BYTES_PER_TERABYTE) {
            return getUnitStr(numBytes, BYTES_PER_TERABYTE, TERABYTE_UNIT_APPEND);
        } else if (numBytes >= BYTES_PER_GIGABYTE) {
            return getUnitStr(numBytes, BYTES_PER_GIGABYTE, GIGABYTE_UNIT_APPEND);
        } else if (numBytes >= BYTES_PER_MEGABYTE) {
            return getUnitStr(numBytes, BYTES_PER_MEGABYTE, MEGABYTE_UNIT_APPEND);
        } else if (numBytes >= BYTES_PER_KILOBYTE) {
            return getUnitStr(numBytes, BYTES_PER_KILOBYTE, KILOBYTE_UNIT_APPEND);
        }

        return numBytes + " B";
    }
    private static String getUnitStr(long numBytes, long bytesPerUnit, String appendToNumber) {
        DecimalFormat format = new DecimalFormat("#0.00");
        return format.format(getUnitsOf(numBytes, bytesPerUnit)) + " " + appendToNumber;
    }
    private static double getUnitsOf(long numBytes, long bytesPerUnit) {
        double units = numBytes / ((double) bytesPerUnit);
        return units;
    }

    // Copied from https://stackoverflow.com/questions/2149785/get-size-of-folder-or-file
    public static long directorySize(File directory) {
        return directorySize(directory, 10000);
    }
    public static long directorySize(File directory, long timeout) {
        final AtomicLong size = new AtomicLong(0);

        try {
            Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    size.addAndGet(attrs.size());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // skip folders that can't be traversed
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    // Ignore errors traversing a folder
                    return FileVisitResult.CONTINUE;
                }


            });
        } catch (IOException e) {
            throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not.");
        }

        return size.get();
    }
}
