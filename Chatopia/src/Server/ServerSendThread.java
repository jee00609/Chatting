package Server;
import java.io.*;
import java.net.Socket;

import Server.ServerSendThread;

public class ServerSendThread extends Thread {
   Socket s;
   String msg;
   ServerSendThread SST=null;
   
   // 소켓 s 받는 생성자
   public ServerSendThread(Socket s) {
      System.out.println("서버 센더 생성자");
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