package Thread;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSendThread extends Thread {

   Socket s;
   String id,msg=null;
   public ClientSendThread(Socket s) {
      System.out.println("클라이언트 센더 생성자");
      this.s = s;
   }
   
   public void setId(String id)
   {
	   this.id=id;
   }
   
   @Override
   public void run() 
   {
      try 
      { 
    	 PrintWriter out = new PrintWriter( s.getOutputStream() );
         out.println(id);//id 전송
         out.flush();
        
         while(true) 
         {
        	//메세지 계속 입력 받기
        	 
        	  System.out.println("<<메세지 입력>>");
        	 @SuppressWarnings("resource")
			 Scanner sc = new Scanner(System.in);
             System.out.println(id+" :: ");
            
            msg = sc.nextLine();
            
            if (msg.equals("quit")) 
            {
               sc.close();
               out.close();
               System.out.println("메신저를 종료합니다\n");
               break;
            }
            
            else
            {//메세지 전송
            out.println(msg);
            out.flush();
           
            }
            
         }// out.close();
            
      }  
      
      catch (IOException e) 
      {
         e.printStackTrace();
      } 
      
      finally 
      {
         try 
         {
            s.close();
         } 
         catch (IOException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      
      
   }
   
}