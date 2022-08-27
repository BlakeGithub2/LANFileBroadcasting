package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IErrorableInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
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
import java.util.Map;
import java.util.Objects;

public class DownloadProjectInstruction implements IErrorableInstruction {
    private static long nextAvailableDownloadId = 0;

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

        downloadProject(sender, nextAvailableDownloadId++, Objects.requireNonNull(projectList.find(projectName)));
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
        if (!directory.equals(projectDirectory)) {
            String relativeFilePath = directory.toString().substring(projectDirectory.toString().length() + 1);
            sender.send("create-directory " + downloadId + " " + relativeFilePath);
        }
    }

    private void downloadFile(InstructionSender sender, long downloadId, File file) throws IOException {
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
    }

    @Override
    public void throwError(Map<String, Object> transferredData, String instruction) {

    }
}
