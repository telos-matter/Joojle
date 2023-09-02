package com.joojle.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		int port = 8080; // Choose a port number
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server is running on port " + port);
		
		while (true) {
			Socket clientSocket = serverSocket.accept();
			System.out.println("New client: " +clientSocket.getInetAddress());
			new Thread(new ClientHandler(clientSocket)).start();
		}
	}
	
	static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		
		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}
		
		@Override
		public void run() {
			try (
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					) {
				// Read the HTTP request
				String request = in.readLine();
				System.out.println("Received request: " + request);
				
				// Send an HTTP response
				String response = "HTTP/1.1 200 OK\r\nContent-Length: 27\r\n\r\nHello from Java HTTP server!";
				out.println(response);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
