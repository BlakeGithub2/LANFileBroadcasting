package main.browse.download;

import main.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadList {
    private File file;
    private List<String> downloads;

    public DownloadList(String directoryInBaseFile) throws IOException {
        if (!Main.getBaseFile().exists()) {
            throw new FileNotFoundException("Base file not found.");
        }
        this.file = Main.getBaseFile().getDirectoryAt(directoryInBaseFile);
        this.downloads = loadDownloads();
    }
    private List<String> loadDownloads() throws IOException {
        List<String> downloads = new ArrayList<>();

        String[] projectNames = file.list();

        for (String name : projectNames) {
            downloads.add(name);
        }

        return downloads;
    }

    public boolean create(String name) {
        if (getFile(name).mkdir()) {
            downloads.add(name);
            return true;
        } else {
            System.out.println("failed to create download: " + name);
        }
        return false;
    }

    public File getFile(String name) {
        return new File(file + "/" + name);
    }
    public boolean contains(String name) {
        return downloads.contains(name);
    }
}
