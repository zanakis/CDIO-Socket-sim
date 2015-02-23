package sim;

import java.util.*;
import java.io.*;
import java.net.*;


public class TCP {
	static Scanner in = new Scanner(System.in);
	static Socket clientSocket;
	static DataOutputStream outToServer;
	static BufferedReader inFromServer;

	public static final String END_INPUT = "\r\n";
	static int menuItem;


	public static void main(String[] args) throws Exception {
		connect();
		ThreadDemo t1 = new ThreadDemo("Weight output", clientSocket);
		t1.start();
		do {
			choseMenuItem();
			//			System.out.println(inFromServer.readLine());
		} while(menuItem != 0);
		clientSocket.close();
	}

	public static void connect() throws Exception {
		String host;
		int port = 0;

		System.out.println("host name");
		host = in.next();
		System.out.println("port");
		try {
			port = Integer.parseInt(in.next());
		} catch(Exception e) {
			System.out.println("Invalid port");
			connect();
		}

		try{
			clientSocket = new Socket(host, port);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(Exception e){
			System.out.println("Could not connect to server");
			connect();
		}
	}

	public static void choseMenuItem() throws IOException {
		try {
			Thread.sleep(1000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		System.out.println("1. Vis vægt");
		System.out.println("2. Tara");
		System.out.println("3. Nulstil vægt");
		System.out.println("4. Indsæt tekst til displayet");
		System.out.println("5. Vis vægt på displayet");
		System.out.println("6. RM20 command");
		System.out.println("0. Stop program");
		try{
			menuItem = Integer.parseInt(in.next());
		} catch(Exception e) {
			System.out.println("Wrong input.");
			System.out.println();
			choseMenuItem();
		}
		switch(menuItem) {
		case 0: outToServer.writeBytes('Q' + END_INPUT); break;
		case 1: outToServer.writeBytes('S' + END_INPUT); break;
		case 2: outToServer.writeBytes('T' + END_INPUT); break;
		case 3: outToServer.writeBytes('Z' + END_INPUT); break;
		case 4: writeToDisplay(0); break;
		case 5: outToServer.writeBytes("DW" + END_INPUT); break;
		case 6: writeToDisplay(1); break;
		default: System.out.println("Invalid input.");
		}
	}

	public static void writeToDisplay(int x) throws IOException {
		System.out.println("Indsæt tekst");
		String input = in.next();
		switch(x) {
		case 0: outToServer.writeBytes("D \"" + input + "\"" + END_INPUT); break;
		case 1: outToServer.writeBytes("RM20 4 \"" + input + "\"" + END_INPUT); break;
		default: System.out.println("huh?");
		}
	}
}
