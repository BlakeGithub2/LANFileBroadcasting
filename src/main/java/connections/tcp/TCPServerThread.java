package connections.tcp;

import main.browse.Project;
import main.browse.ProjectLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServerThread extends Thread {
    // SEE: https://www.youtube.com/watch?v=dg2V2-ob_NU, https://www.baeldung.com/a-guide-to-java-sockets
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean started;

    public TCPServerThread() throws IOException {
        this("TCPServerThread");
    }

    public TCPServerThread(String name) throws IOException {
        super(name);
        serverSocket = new ServerSocket(4447);
        started = false;
    }

    public void initialize() throws IOException {
        if (!started) {
            clientSocket = serverSocket.accept();
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
            }
        }
    }

    private void receiveInstruction() throws IOException {
        String instruction = in.readUTF();
        if (instruction.equals("get downloads")) {
            List<Project> projects = ProjectLoader.loadProjectList();
            List<String> projectNames = new ArrayList<>();
            for (Project project : projects) {
                projectNames.add(project.getName());
            }

            out.writeObject(projectNames);
            out.flush();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();

        // Socket close code only works here
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
