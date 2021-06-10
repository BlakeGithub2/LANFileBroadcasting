package main.browse;

import java.nio.file.Path;

public class Project {
    private Path filepath;

    public Project(Path filepath) {
        this.filepath = filepath;
    }

    public String getName() {
        return filepath.getFileName().toString();
    }
    public Path getFilePath() {
        return filepath;
    }
}
