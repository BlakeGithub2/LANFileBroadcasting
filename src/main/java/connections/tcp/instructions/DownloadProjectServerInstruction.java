package connections.tcp.instructions;

import connections.tcp.instructions.distribution.IInstruction;
import connections.tcp.instructions.distribution.InstructionSender;
import connections.tcp.instructions.distribution.InstructionUtils;
import main.Main;
import main.browse.Project;
import main.browse.ProjectList;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Base64;

public class DownloadProjectServerInstruction implements IInstruction {
    @Override
    public void onReceive(InstructionSender sender, String instruction) throws IOException {
        String projectName = InstructionUtils.parseNameWithSpaces(instruction, 2);

        if (!ProjectList.contains(projectName)) {
            sender.sendReturn(InstructionUtils.parseInstructionId(instruction), "false");
            return;
        }

        sender.sendReturn(InstructionUtils.parseInstructionId(instruction), "true");
        //downloadProject(sender, Objects.requireNonNull(ProjectList.find(projectName)));
    }

    private void downloadProject(InstructionSender sender, Project project) throws IOException {
        if (!ProjectList.contains(project.getName())) {
            throw new FileNotFoundException("Project " + project.getName() + " could not be found.");
        }

        sender.send("create-project " + project.getName());

        try {
            File projectDirectory = project.getFilePath().toFile();
            System.out.println("walking directory: " + projectDirectory);
            Files.walkFileTree(projectDirectory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("Download file: " + file.toString());
                    downloadFile(sender, projectDirectory, file.toFile());
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
            throw new IOException(e.getMessage());
        }
    }

    private void downloadFile(InstructionSender sender, File projectDirectory, File file) throws IOException {
        InputStream fileInputStream = new FileInputStream(file);
        String relativeFilePath = file.toString().substring(projectDirectory.toString().length() + 1);

        sender.send("create-file " + relativeFilePath);

        // TODO: Check file has been successfully created on client over network

        int bytesRead = 0;
        while (bytesRead != -1) {
            byte[] bytes = new byte[Main.BYTE_BUFFER_SIZE];
            bytesRead = fileInputStream.read(bytes);
            sender.send("append " + relativeFilePath + " " + Base64.getEncoder().encodeToString(bytes));
        }
        sender.send("done");
    }
}
