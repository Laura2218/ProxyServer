import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    // 代理服务器端口80
    private static int port = 80;
    // 线程池中保持5个核心线程
    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        try {
            // 声明欢迎套接字
            ServerSocket welcomeSocket=new ServerSocket(port);
            Socket socket;
            while(true){
                // 接收请求并为连接创建一个新的套接字
                socket=welcomeSocket.accept();
                // 将与该socket相关的处理交予线程池中的线程处理
                ForwardThread forwardThread=new ForwardThread(socket);
                executor.execute(forwardThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}