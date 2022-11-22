import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        // Server
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("Waiting for another player...");

                Socket socket1 = serverSocket.accept();
                DataOutputStream dataOutputStream1 = new DataOutputStream(socket1.getOutputStream());
                dataOutputStream1.writeUTF("Waiting for another player...");

                ClientHandler clientHandler = new ClientHandler(socket, socket1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ClientHandler extends Thread {
        Socket socket;
        Socket socket1;
        DataInputStream dataInputStream;
        DataInputStream dataInputStream1;
        DataOutputStream dataOutputStream;
        DataOutputStream dataOutputStream1;
        String str = "", str1 = "";

        public ClientHandler(Socket socket, Socket socket1) {
            this.socket = socket;
            this.socket1 = socket1;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataInputStream1 = new DataInputStream(socket1.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream1 = new DataOutputStream(socket1.getOutputStream());
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                dataOutputStream.writeUTF("Game start! You are player 1. Your turn");
                dataOutputStream1.writeUTF("Game start! You are player 2. Wait");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                str = dataInputStream.readUTF();
                                System.out.println("Client1: " + str);
                                dataOutputStream1.writeUTF(str);
                                dataOutputStream1.flush();
                            } catch (IOException e) {
                                System.out.println("Game Over!");
                                try {
                                    dataOutputStream.writeUTF("Another player disconnect");
                                } catch (IOException ex) {
                                    System.out.println("Client1 disconnect");
                                }
                                try {
                                    dataOutputStream1.writeUTF("Another player disconnect");
                                } catch (IOException ex) {
                                    System.out.println("Client2 disconnect");
                                }
                                break;
                            }
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                str1 = dataInputStream1.readUTF();
                                System.out.println("Client2: " + str1);
                                dataOutputStream.writeUTF(str1);
                                dataOutputStream.flush();
                            } catch (IOException e) {
                                System.out.println("Game Over!");
                                try {
                                    dataOutputStream.writeUTF("Another player disconnect");
                                } catch (IOException ex) {
                                    System.out.println("Client1 disconnect");
                                }
                                try {
                                    dataOutputStream1.writeUTF("Another player disconnect");
                                } catch (IOException ex) {
                                    System.out.println("Client2 disconnect");
                                }
                                break;
                            }
                        }
                    }
                };
                Thread thread1 = new Thread(runnable1);
                thread1.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
