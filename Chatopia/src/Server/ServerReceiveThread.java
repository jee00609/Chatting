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

   // Ŭ���̾�Ʈ ������ ����
   PrintWriter printwriter;

   // ������ ����Ǵ� Ŭ���̾�Ʈ �ϳ��� ���� �����ؾ���
   static List<PrintWriter> ClientList = Collections.synchronizedList(new ArrayList<PrintWriter>());

   // ���� s �޴� ������
   public ServerReceiveThread(Socket s) {
      System.out.println("���� ���ú� ������");
      this.s = s;

      try {
         // Ŭ���̾�Ʈ �ϳ��� ArrayList�� �߰��Ͽ� �����Ѵ�.
         printwriter = new PrintWriter(s.getOutputStream());
         ClientList.add(printwriter);
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }

   }

   @Override
   public void run() {
      try {
         // Client�� ���� �� �޼��� �о Client���� �ٽ� ������
         BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

         // Ŭ���̾�Ʈ ���̵� �б�
         String id = br.readLine();

         // ��� ����� Ŭ���̾�Ʈ���� ������ ���ο� Ŭ���̾�Ʈ ���̵� ����
         // @@���� �����ϼ̽��ϴ�==>Example
         // sendAll�̶�� �Լ�==>����� ���� �Լ�
         sendtoAll("#" + id + "���� �����ϼ̽��ϴ�\n");

         while (true) {

            // Ŭ���̾�Ʈ�� ���� �޼��� �б�
            msg = br.readLine();

            // If there's no message to send
            // End Chat
            if (msg == null) {
               break;
            }

            else if (msg == "quit" || msg == "Quit" || msg == "QUIT") {
               // Ŭ���̾�Ʈ ���� ArrayList���� ����
               ClientList.remove(printwriter);
               // ������ �޼��� ���
               System.out.println(id + "���� �����ϼ̽��ϴ�\n");
            }

            // msg�� �����Ҷ�
            // ��� Ŭ���̾�Ʈ���� �޼��� ����
            sendtoAll(id+" >> "+msg);

            // Ŭ���̾�Ʈ�� ���� �� �޼��� ���
            // System.out.println("Ŭ���̾�Ʈ�κ��� �� �޼���");
             System.out.println(id + "::" + msg);
         }

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // ���Ⱑ �߰��� �Լ�����!!~~
   // ��� Ŭ���̾�Ʈ���� �޼��� ���� �Լ�
   private void sendtoAll(String message) {
      // for �� ����Ѵ�.
      // Arraylist�� ����� Ŭ���ξ�Ʈ���� ��� �޼��� ����
      for (PrintWriter printwriter : ClientList) {
         printwriter.println(message);
         printwriter.flush();
      }

   }
}
