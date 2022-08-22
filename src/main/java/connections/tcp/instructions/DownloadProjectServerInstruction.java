package connections.tcp.instructions;

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
    public void onReceive(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        String projectName = instruction.substring(instruction.indexOf(' ') + 1);
        ObjectOutputStream oos = new ObjectOutputStream(out);

        if (!ProjectList.contains(projectName)) {
            oos.writeBoolean(false);
            return;
        }

        oos.writeBoolean(true);
        downloadProject(out, in, ProjectList.find(projectName));
    }

    private void downloadProject(ObjectOutputStream out, ObjectInputStream in, Project project) throws IOException {
        if (!ProjectList.contains(project.getName())) {
            throw new FileNotFoundException("Project " + project.getName() + " could not be found.");
        }

        InstructionSender.sendInstruction(out, in, "create-project " + project.getName());

        try {
            File projectDirectory = project.getFilePath().toFile();
            System.out.println("walking directory: " + projectDirectory);
            Files.walkFileTree(projectDirectory.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("Download file: " + file.toString());;
                    downloadFile(out, in, projectDirectory, file.toFile());
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

    private void downloadFile(ObjectOutputStream out, ObjectInputStream in, File projectDirectory, File file) throws IOException {
        InputStream fileInputStream = new FileInputStream(file);
        String relativeFilePath = file.toString().substring(projectDirectory.toString().length() + 1);

        boolean createdFile = (boolean) InstructionSender.sendInstruction(out, in, "create-file " + relativeFilePath);

        if (!createdFile) {
            throw new IOException("Could not create file " + file.getName() + ".");
        }

        int bytesRead = 0;
        while (bytesRead != -1) {
            byte[] bytes = new byte[Main.BYTE_BUFFER_SIZE];
            bytesRead = fileInputStream.read(bytes);
            out.write(bytes);
            InstructionSender.sendInstruction(out, in, "append " + relativeFilePath + " " + Base64.getEncoder().encodeToString(bytes));
        }
        InstructionSender.sendInstruction(out, in, "done");

        out.flush();
    }

    @Override
    public Object onResponse(ObjectOutputStream out, ObjectInputStream in, String instruction) throws IOException {
        // Ensure project was found

        // Read in instructions until done
        while (InstructionReceiver.readInstructionFromSocket(out, in)) {}

        return true;
    }

}
