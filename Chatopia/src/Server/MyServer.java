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
            System.out.println("���� ����");

            ServerReceiveThread srt = new ServerReceiveThread(s);
          
            //������ 
            srt.start();
           
         }
      } 
      catch (IOException e) 
      {
         System.out.println("���� ���� ���� �߻�!\n");
         // TODO: handle exception
      }
   }
}