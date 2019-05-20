package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReceiveThread extends Thread  {
      
      Socket s;
      
      // ���� s �޴� ������
      public ClientReceiveThread(Socket s) {
         System.out.println("Ŭ���̾�Ʈ ���ú� ������");
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
                  System.out.println("�޼��� ����");
                  break;
               }
               
               else
               {
                  //������ ���� �ٸ� Ŭ���̾�Ʈ�� ���� ���޹���
                  //�޼����� ����Ѵ�.
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