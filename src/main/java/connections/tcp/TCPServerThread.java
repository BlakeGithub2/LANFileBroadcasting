package connections.tcp;

import main.browse.Project;
import main.browse.ProjectLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServerThread extends Thread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU, https://www.baeldung.com/a-guide-to-java-sockets
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean started;

    public TCPServerThread(Socket socket) throws IOException {
        this("TCPServerThread", socket);
    }

    public TCPServerThread(String name, Socket socket) throws IOException {
        super(name);
        clientSocket = socket;
        started = false;
    }

    public void initialize() throws IOException {
        if (!started) {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        }
    }

    @Override
    public void run() {
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                receiveInstruction();
            } catch (IOException e) {
                break;
            }
        }

        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveInstruction() throws IOException {
        String instruction = in.readUTF();
        String[] splitInstruction = instruction.split(" ");

        if (splitInstruction.length == 0) {
            throw new IOException("Invalid instruction.");
        }

        String command = instruction.split(" ")[0];

        if (command.equals("get") && instruction.equals("get downloads")) {
            List<Project> projects = ProjectLoader.loadProjectList();
            List<String> projectNames = new ArrayList<>();
            for (Project project : projects) {
                projectNames.add(project.getName());
            }

            out.writeObject(projectNames);
            out.flush();
        }
        if (command.equals("download")) {
            String projectName = instruction.substring(instruction.indexOf(' ') + 1);
            List<Project> projects = ProjectLoader.loadProjectList();

            if (!projectListContains(projects, projectName)) {
                //out.writeObject(new NoProjectExistsException("No project found on server for downloading with name " + projectName));
                //out.flush();
                //return;
            }

            out.writeObject("test");
            out.flush();
        }
    }
    private boolean projectListContains(List<Project> projectList, String projectName) {
        for (Project project : projectList) {
            if (project.getName().equals(projectName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void interrupt() {
        super.interrupt();

        // Socket close code only works here
    }
}
