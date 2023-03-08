package ftp;
import java.io.*;
import java.net.*;

public class FTPServer {
	public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
			System.out.println("Server started.");

			while (true) {
			    Socket clientSocket = serverSocket.accept();
			    System.out.println("Client connected: " + clientSocket.getInetAddress());

			    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			    File directory = new File("./store");
			    if (directory.mkdir() == true) { 
					System.out.println("Directory has been created successfully"); 
				} 
				else { 
					System.out.println("Directory cannot be created"); 
				} 
			    
			    File[] fileList = directory.listFiles();
			    for (File file : fileList) {
			        out.println(file.getName());
			    }

			    String command = in.readLine();
			    String filename = in.readLine();
			    if (command.equals("d")) {
			        File file = new File(directory, filename);
			        FileInputStream fileInputStream = new FileInputStream(file);
			        byte[] buffer = new byte[1024];
			        int bytesRead;
			        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
			            clientSocket.getOutputStream().write(buffer, 0, bytesRead);
			        }
			        fileInputStream.close();
			    } else if (command.equals("u")) {
			        File file = new File(directory, filename);
			        FileOutputStream fileOutputStream = new FileOutputStream(file);
			        byte[] buffer = new byte[1024];
			        int bytesRead;
			        while ((bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
			            fileOutputStream.write(buffer, 0, bytesRead);
			        }
			        fileOutputStream.close();
			    }
			    clientSocket.close();
			}
		}
    }
}