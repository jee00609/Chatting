package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerReceiveThread extends Thread {

   Socket s;
   String msg;

   // 클라이언트 관리시 사용됨
   PrintWriter printwriter;

   // 서버와 연결되는 클라이언트 하나씩 개별 관리해야함
   static List<PrintWriter> ClientList = Collections.synchronizedList(new ArrayList<PrintWriter>());

   // 소켓 s 받는 생성자
   public ServerReceiveThread(Socket s) {
      System.out.println("서버 리시브 생성자");
      this.s = s;

      try {
         // 클라이언트 하나씩 ArrayList에 추가하여 관리한다.
         printwriter = new PrintWriter(s.getOutputStream());
         ClientList.add(printwriter);
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }

   }

   @Override
   public void run() {
      try {
         // Client로 부터 온 메세지 읽어서 Client에게 다시 보내기
         BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

         // 클라이언트 아이디 읽기
         String id = br.readLine();

         // 모든 연결된 클라이언트에게 입장한 새로운 클라이언트 아이디 전송
         // @@님이 입장하셨습니다==>Example
         // sendAll이라는 함수==>사용자 정의 함수
         sendtoAll("#" + id + "님이 입장하셨습니다\n");

         while (true) {

            // 클라이언트가 보낸 메세지 읽기
            msg = br.readLine();

            // If there's no message to send
            // End Chat
            if (msg == null) {
               break;
            }

            else if (msg == "quit" || msg == "Quit" || msg == "QUIT") {
               // 클라이언트 관리 ArrayList에서 삭제
               ClientList.remove(printwriter);
               // 나가기 메세지 출력
               System.out.println(id + "님이 퇴장하셨습니다\n");
            }

            // msg가 존재할때
            // 모든 클라이언트에게 메세지 전송
            sendtoAll(id+" >> "+msg);

            // 클라이언트로 부터 온 메세지 출력
            // System.out.println("클라이언트로부터 온 메세지");
             System.out.println(id + "::" + msg);
         }

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // 여기가 추가된 함수에요!!~~
   // 모든 클라이언트에게 메세지 전송 함수
   private void sendtoAll(String message) {
      // for 문 사용한다.
      // Arraylist에 저장된 클라인언트에게 모두 메세지 전송
      for (PrintWriter printwriter : ClientList) {
         printwriter.println(message);
         printwriter.flush();
      }

   }
}
