import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ForwardThread  implements Runnable{
    private Socket socket;
    public ForwardThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        String stateLine = "";
        String url = "";
        String host = "";
        try {
            // 创建一个新套接字用于与目的主机交互
            Socket s = new Socket();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 从报文中的第一行提取主机名与完整的路径名
            try{
                stateLine = br.readLine();
                url = stateLine.split(" ")[1];
                host = url.substring(url.indexOf("//")+2);
                host = host.substring(0, host.indexOf("/"));
            }
            catch(IOException e){
                e.printStackTrace();
            }
            if (host.length()!=0){
                // 建立TCP连接并cp客户端报文
                InetSocketAddress address=new InetSocketAddress(host, 80);
                s.connect(address , 3000);
                s.getOutputStream().write(stateLine.getBytes());
                httpParser.c2sMessage(socket.getInputStream(), s.getOutputStream(), url);
            }
            socket.shutdownInput();//关闭输入流
            // cp目标主机的响应报文
            httpParser.s2cMessage(s.getInputStream(), socket.getOutputStream(), url);
            s.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}