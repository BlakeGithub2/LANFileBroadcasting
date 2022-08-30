package main.browse.download;

import main.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class DownloadEntry {
    private String currFile;
    private String currDirectory;
    private String projectPath;
    private boolean failed;

    public boolean createProject(String downloadName) {
        boolean success = true;

        DownloadList downloadList = null;
        try {
            downloadList = new DownloadList("downloads");

            if (Main.getBaseFile().exists() && !downloadList.contains(downloadName)) {
                success = downloadList.create(downloadName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (success) {
            projectPath = downloadList.getFile(downloadName).toString();
            currDirectory = "";
        } else {
            projectPath = null;
            failed = true;
        }

        return success;
    }

    public boolean createFile(String fileName) {
        if (this.currDirectory == null) {
            failed = true;
            throw new RuntimeException("Cannot create file " + fileName + " when no directory was established.");
        }
        if (failed) {
            throw new RuntimeException("Download already failed.");
        }

        boolean success = true;

        try {
            File downloadFile = new File(projectPath + "/" + currDirectory + "/" + fileName);
            success = downloadFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        if (success) {
            currFile = fileName;
        } else {
            currFile = null;
            failed = true;
        }

        return success;
    }

    public boolean createDirectory(String directoryName) {
        if (failed) {
            throw new RuntimeException("Download already failed.");
        }

        boolean success = true;

        try {
            File newDirectory = new File(projectPath + "/" + directoryName);
            success = newDirectory.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        if (success) {
            currDirectory = directoryName;
        } else {
            currDirectory = null;
            failed = true;
        }
        currFile = null;

        return success;
    }

    public boolean dropDirectory() {
        int lastSlash = -1;
        for (int i = 0; i < currDirectory.length(); i++) {
            if (currDirectory.charAt(i) == '\\') {
                lastSlash = i;
            }
        }

        if (lastSlash == -1) {
            if (!currDirectory.equals("")) {
                currDirectory = "";
                return true;
            } else {
                throw new RuntimeException("Could not drop down past the root directory.");
            }
        }

        currDirectory = currDirectory.substring(0, lastSlash);
        return true;
    }

    public boolean appendToCurrentFile(byte[] bytes) {
        if (failed) {
            throw new RuntimeException("Download already failed.");
        }
        if (this.currDirectory == null) {
            failed = true;
            throw new RuntimeException("Cannot append to current file when no directory was established.");
        }
        if (this.currFile == null) {
            failed = true;
            throw new RuntimeException("Cannot append to current file when no file was established.");
        }

        boolean success = true;

        File appendToFile = new File(projectPath + "/" + currDirectory + "/" + currFile);
        try {
            Files.write(appendToFile.toPath(), bytes, StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        if (!success) {
            appendToFile.delete();
            currDirectory = null;
            currFile = null;
            failed = true;
        }

        return success;
    }

    public boolean hasFailed() {
        return failed;
    }

    public void markFailed() {
        failed = true;
    }
}
