package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IOnSuccessInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import main.Main;
import main.browse.Project;
import main.browse.ProjectList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DownloadProjectInstruction implements IOnSuccessInstruction {
    private static long nextAvailableDownloadId = 0;
    private static final Map<Long, Boolean> downloadsHealth = new HashMap<>();

    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        String projectName = InstructionUtils.parseNameWithSpaces(instruction, 2);
        ProjectList projectList = new ProjectList("projects");

        if (!projectList.contains(projectName)) {
            String errorMessage = "Project " + projectName + " not found on server. Was it deleted after" +
                    " downloadable projects were refreshed?";
            sender.sendError(InstructionUtils.parseInstructionId(instruction), errorMessage);
            return;
        }

        long downloadId = nextAvailableDownloadId++;
        downloadsHealth.put(downloadId, true);

        downloadProject(sender, downloadId, Objects.requireNonNull(projectList.find(projectName)));

        downloadsHealth.remove(downloadId);

        sender.sendSuccess(InstructionUtils.parseInstructionId(instruction));
    }

    private void downloadProject(InstructionSender sender, long downloadId, Project project) throws IOException {
        sender.send("create-project " + downloadId + " " + project.getName());

        try {
            File projectDirectory = project.getFilePath().toFile();

            Files.walkFileTree(projectDirectory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    downloadFile(sender, downloadId, file.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // skip folders that can't be traversed
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // Ignore errors traversing a folder
                    if (!dir.toFile().equals(projectDirectory)) {
                        sender.send("drop-directory " + downloadId);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    downloadDirectory(sender, downloadId, projectDirectory, dir.toFile());
                    return super.preVisitDirectory(dir, attrs);
                }
            });
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void downloadDirectory(InstructionSender sender, long downloadId, File projectDirectory, File directory) throws IOException {
        if (downloadsHealth.get(downloadId)) {
            if (!directory.equals(projectDirectory)) {
                String relativeFilePath = directory.toString().substring(projectDirectory.toString().length() + 1);
                sender.send("create-directory " + downloadId + " " + relativeFilePath);
            }
        }
    }

    private void downloadFile(InstructionSender sender, long downloadId, File file) throws IOException {
        if (downloadsHealth.get(downloadId)) {
            InputStream fileInputStream = new FileInputStream(file);
            sender.send("create-file " + downloadId + " " + file.getName());

            // TODO: Check file has been successfully created on client over network
            while (true) {
                byte[] bytes = fileInputStream.readNBytes(Main.BYTE_BUFFER_SIZE);

                if (bytes.length == 0) {
                    break;
                }

                sender.send("append " + downloadId + " " + Base64.getEncoder().encodeToString(bytes));
            }

            fileInputStream.close();
        }
    }

    @Override
    public void onSuccess(InstructionSender sender) {
        showSuccessMessage();
    }

    public static void recordError(String error, long downloadId) {
        downloadsHealth.put(downloadId, false);
        showTransferFileError(error);
    }

    private static void showTransferFileError(String reason) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Transfer file error");
                a.setContentText(reason + " To avoid (potentially dangerous) corrupted data, " +
                        "please delete the partially downloaded file.");
                a.show();
            }
        });
    }

    private static void showSuccessMessage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setHeaderText("Transfer file success");
                a.setContentText("File successfully transferred.");
                a.show();
            }
        });
    }
}
