package Thread;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSendThread extends Thread {

   Socket s;
   String id,msg=null;
   public ClientSendThread(Socket s) {
      System.out.println("Ŭ���̾�Ʈ ���� ������");
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
         out.println(id);//id ����
         out.flush();
        
         while(true) 
         {
        	//�޼��� ��� �Է� �ޱ�
        	 
        	  System.out.println("<<�޼��� �Է�>>");
        	 @SuppressWarnings("resource")
			 Scanner sc = new Scanner(System.in);
             System.out.println(id+" :: ");
            
            msg = sc.nextLine();
            
            if (msg.equals("quit")) 
            {
               sc.close();
               out.close();
               System.out.println("�޽����� �����մϴ�\n");
               break;
            }
            
            else
            {//�޼��� ����
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