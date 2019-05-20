package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
   public static void main(String[] args) throws IOException {
      // TODO Auto-generated method stub
      
      ServerSocket ss;
      try
      {
         ss = new ServerSocket(3001);

         while (true) 
         {
            Socket s = ss.accept();
            System.out.println("서버 시작");

            ServerReceiveThread srt = new ServerReceiveThread(s);
          
            //스레드 
            srt.start();
           
         }
      } 
      catch (IOException e) 
      {
         System.out.println("서버 연결 오류 발생!\n");
         // TODO: handle exception
      }
   }
}