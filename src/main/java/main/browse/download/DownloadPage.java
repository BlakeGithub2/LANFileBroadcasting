package main.browse.download;

import main.Main;
import main.browse.BrowseFilePage;

public class DownloadPage extends BrowseFilePage {
    public void findDownloadableProjects() {
        System.out.println("finding downloadable projects...");
        Main.getTCPClient().sendInstruction(new GetDownloadableProjectsInstruction(projects));
    }
}
