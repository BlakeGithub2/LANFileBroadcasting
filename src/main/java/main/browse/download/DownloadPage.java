package main.browse.download;

import main.browse.BrowseFilePage;
import main.browse.Project;

import java.util.List;

public class DownloadPage extends BrowseFilePage {
    public List<Project> getDownloadableProjects() {
        return projects;
    }
}
