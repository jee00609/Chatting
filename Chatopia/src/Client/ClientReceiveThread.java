package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiveThread extends Thread  {
      
      Socket s;
      
      // 소켓 s 받는 생성자
      public ClientReceiveThread(Socket s) {
         System.out.println("클라이언트 리시브 생성자");
         this.s = s;
      }
      
      @Override
      public void run() {
         try 
         {
            BufferedReader reader = null;
            reader = new BufferedReader( new InputStreamReader(s.getInputStream()) );
            
            while( true ) 
            {
               String msg = reader.readLine();
               
               if(msg==null)
               {
                  System.out.println("메세지 없음");
                  break;
               }
               
               else
               {
                  //서버로 부터 다른 클라이언트로 부터 전달받은
                  //메세지를 출력한다.
                System.out.print("\n"+msg+"\n");
               }
            
            }
         
         }
         catch (Exception e) 
         {
            // TODO: handle exception
            e.printStackTrace();
         }
      }
   }