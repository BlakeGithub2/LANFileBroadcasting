package main;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileSystem {
    private File baseFile;

    public void saveBasePath(String dirStr) {
        File addressFile = new File(Main.LOCAL_ADDRESS_FILE_PATH);

        createAddressFileIfNeeded(addressFile);

        printToAddressFile(addressFile, dirStr);
    }
    private void createAddressFileIfNeeded(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                showCouldNotCreateAddressFileMessage();
                e.printStackTrace();
            }
        }
    }
    private void printToAddressFile(File file, String info) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(file);
            out.println(info);
        } catch (FileNotFoundException e) {
            showCouldNotCreateAddressFileMessage();
            System.out.println("Could not find file.");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
    private void showCouldNotCreateAddressFileMessage() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Could not load base address of program.\n" +
                "Exiting the program.");
        a.showAndWait();
    }

    public boolean addressFileExists() {
        return (new File(Main.LOCAL_ADDRESS_FILE_PATH)).exists();
    }

    public void loadBasePath() {
        showMessageIfBaseAddressDoesNotExist();

        BufferedReader br = initializeBufferedReader();
        if (br == null) {
            showCouldNotLoadBaseAddressMessage();
        }

        String filePath = loadString(br);
        if (filePath == null) {
            showCouldNotLoadBaseAddressMessage();
        }

        assignToBaseFileIfExists(filePath);
    }
    private BufferedReader initializeBufferedReader() {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(Main.LOCAL_ADDRESS_FILE_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return br;
    }
    private String loadString(BufferedReader br) {
        String filePath = null;

        try {
            filePath = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }
    private void assignToBaseFileIfExists(String filePath) {
        File newFile = new File(filePath);

        if (newFile.exists() && newFile != null) {
            baseFile = newFile;
        } else {
            showMessageIfBaseAddressDoesNotExist();
        }
    }
    private void showMessageIfBaseAddressDoesNotExist() {
        if (!addressFileExists()) {
            showCouldNotLoadBaseAddressMessage();
            System.exit(1);
        }
    }
    private void showCouldNotLoadBaseAddressMessage() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Could not load base address of program.\n" +
                "Exiting the program.");
        a.showAndWait();
    }

    public File getBaseFile() {
        return baseFile;
    }
}
