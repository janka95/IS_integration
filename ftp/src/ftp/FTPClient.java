package ftp;

import java.io.*;
import java.net.*;

public class FTPClient {
	public static void main(String[] args) throws IOException {
		try (Socket clientSocket = new Socket("localhost", 5000)) {
			System.out.println("Connected to server.");

			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter 'u' for upload or 'd' for download:");
			String command = keyboard.readLine();
			System.out.println("Enter the filename:");
			String filename = keyboard.readLine();

			if (command.equals("d")) {
				File file = new File("./files/" + filename);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int bytesRead;
				InputStream inputStream = clientSocket.getInputStream();
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, bytesRead);
				}
				fileOutputStream.close();
			} else if (command.equals("u")) {
				File file = new File("./files/" + filename);
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int bytesRead;
				OutputStream outputStream = clientSocket.getOutputStream();
				while ((bytesRead = fileInputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				fileInputStream.close();
			}
			clientSocket.close();
		}
	}
}