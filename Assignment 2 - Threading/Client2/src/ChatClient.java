import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.RandomAccess;
import java.util.Scanner;
import java.nio.channels.FileChannel;

public class ChatClient {

    public ChatClient() {}

    public void start() {
        SocketAddress address = new InetSocketAddress("127.0.0.1", 5000);
        try (SocketChannel socketChannel = SocketChannel.open(address)) {
            System.out.println("Connected to File Server");
            String message;
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // Receive message
                System.out.println("Waiting for file from the Client ...");
                System.out.print("> ");
                message = scanner.nextLine();
                HelperMethods.sendMessage(socketChannel, message);
                String receivedMessage = HelperMethods.receiveMessage(socketChannel);
                if (message.equalsIgnoreCase("quit")) {
                    HelperMethods.sendMessage(socketChannel, "Client terminating");
                    break;
                }
                else if(receivedMessage.contains ("Error")) {
                    HelperMethods.sendMessage(socketChannel, "File Not Found");
                    System.out.println("File Not Found");
                }else {
                    // Send message
                    RandomAccessFile my_File = new RandomAccessFile("C:\\Users\\James\\OneDrive - GMIT\\Year 5\\IDEAProjects\\Output files\\" + message, "rw");
                    FileChannel File_Channel = my_File.getChannel();
                    byte[] bytesCount = receivedMessage.getBytes();
                    ByteBuffer buff = ByteBuffer.allocate(128);
                    ByteBuffer.allocate(bytesCount.length);
                    buff.put(bytesCount);
                    buff.flip();
                    File_Channel.write(buff);
                    buff.clear();
                    my_File.close();
                    File_Channel.close();
                    System.out.println("File Content: " + receivedMessage);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }        
    }
    
    public static void main(String[] args) {
        new ChatClient().start();
    }
}