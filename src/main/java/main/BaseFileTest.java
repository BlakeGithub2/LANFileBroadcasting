package main;

import java.io.File;
import java.io.IOException;

public class BaseFileTest extends BaseFile {
    public void setFile(File file) throws IOException {
        file.mkdir();
        this.file = file;
    }
}
