package Server;
import java.io.*;
import java.net.Socket;

import Server.ServerSendThread;

public class ServerSendThread extends Thread {
   Socket s;
   String msg;
   ServerSendThread SST=null;
   
   // ���� s �޴� ������
   public ServerSendThread(Socket s) {
      System.out.println("���� ���� ������");
      this.s = s;
   }

   @Override
   public void run() {
      
      try 
      {
    	 // PrintWriter pw=new PrintWriter(s.getOutputStream());
    	  
    	  //Send Message to Client from Server
    	  //pw.println(SST.msg);
    	 // pw.flush();
    	  
         
    	 
      }

      catch (Exception e) {
         System.out.println(e.getMessage());
      }

   }
}