package main;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseFile {
    protected File file;

    public BaseFile() {
        if (addressFileExists()) {
            loadBasePath();
        }
    }
    public BaseFile(File file) {
        this.file = file;
    }

    public File getFileAt(String appendDir) throws IOException {
        File result = new File(getFile().getPath() + "/" + appendDir);

        if (!result.exists()) {
            result.createNewFile();
        }

        return result;
    }

    public File getDirectoryAt(String appendDir) throws IOException {
        File result = new File(getFile().getPath() + "/" + appendDir);

        if (!result.exists()) {
            if (!result.mkdir()) {
                throw new IOException("Could not create file at appended " +
                        "directory " + appendDir);
            }
        }

        return result;
    }

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
    public boolean exists() {
        return file.exists();
    }

    private void loadBasePath() {
        showMessageIfBaseAddressDoesNotExist();

        BufferedReader br = initializeBufferedReader();
        if (br == null) {
            showCouldNotLoadBaseAddressSoExitingMessage();
        }

        String filePath = loadString(br);
        if (filePath == null) {
            showCouldNotLoadBaseAddressSoExitingMessage();
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
            file = newFile;
        } else {
            showMessageIfBaseAddressDoesNotExist();
        }
    }
    private void showMessageIfBaseAddressDoesNotExist() {
        if (!addressFileExists()) {
            showCouldNotLoadBaseAddressSoExitingMessage();
            System.exit(1);
        }
    }
    private void showCouldNotLoadBaseAddressSoExitingMessage() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Could not load base address of program.\n" +
                "Exiting the program.");
        a.showAndWait();
    }

    public File getFile() throws FileNotFoundException {
        if (file == null || (file != null && !file.exists())) {
            showCouldNotLoadBaseAddressSoGoInstallMessage();
            Main.getSceneController().activate("install");
            throw new FileNotFoundException("Could not find base file.");
        }

        return file;
    }
    private void showCouldNotLoadBaseAddressSoGoInstallMessage() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Could not load base address of program.\n" +
                "Please locate it again.");
        a.showAndWait();
    }
}
