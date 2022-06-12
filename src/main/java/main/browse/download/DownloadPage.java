package main.browse.download;

import main.browse.BrowseFilePage;
import main.browse.Project;

import java.util.List;

public class DownloadPage extends BrowseFilePage {
    // TODO: Fix dependency on Main
    public DownloadPage() {
    }

    public List<Project> getDownloadableProjects() {
        return projects;
    }
}
