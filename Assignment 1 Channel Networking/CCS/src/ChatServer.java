import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ChatServer {

    public ChatServer() {}

    public void start() {
        System.out.println("Chat Server started");
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(5000));

            boolean running = true;
            while (running) {
                System.out.println("Waiting for request ...");
                SocketChannel socketChannel
                        = serverSocketChannel.accept();

                System.out.println("Connected to Client");

                String receivedMessage = HelperMethods.receiveMessage(socketChannel);

                    if (receivedMessage.equalsIgnoreCase("quit")) {
                        HelperMethods.sendMessage(
                                socketChannel, "Server terminating");
                        running = false;
                        break;
                    }
                    else {
                        System.out.println(receivedMessage);
                        try {
                            RandomAccessFile my_File = new RandomAccessFile("C:\\Users\\James\\OneDrive - GMIT\\Year 5\\IDEAProjects\\" + receivedMessage, "r");
                            // Open channel
                            FileChannel File_Channel = my_File.getChannel();
                            // creating and initializing ByteBuffer for reading/writing data
                            ByteBuffer buff = ByteBuffer.allocate(1000);
                            String content = "";
                            while (File_Channel.read(buff) != -1) {
                                buff.flip();
                                while (buff.hasRemaining()) {
                                    content += (char) buff.get();
                                }
                                buff.clear();
                            }
                            System.out.println("Waiting for message from client ...");
                            HelperMethods.sendMessage(socketChannel, content);
                            my_File.close();
                        } catch (Exception FileNotFoundException) {
                            HelperMethods.sendMessage(socketChannel, "Error");
                        }
                    }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
