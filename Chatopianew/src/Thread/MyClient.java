package Thread;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class MyClient {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Socket s = new Socket("192.168.238.1",3001);
	
		
		String id=null;
        
		
        //id 입력하기
        System.out.println("id 입력:");
       
        @SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
        id=sc.nextLine();//id 읽어오기
        
		//클라이언트 센드 스레드 생성
		ClientSendThread cst = new ClientSendThread(s);
		cst.setId(id);
		//스레드 시작
		cst.start();
	
		//클라이언트 리시브 스레드 생성
		
		ClientReceiveThread crt = new ClientReceiveThread(s);
		//스레드 시작
		crt.start();
	}
}
