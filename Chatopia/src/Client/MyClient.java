package Client;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class MyClient {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Socket s = new Socket("117.17.142.133", 3001);
	
		
		String id=null;
        
		
        //id �Է��ϱ�
        System.out.println("id �Է�:");
       
        @SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
        id=sc.nextLine();//id �о����
        
		//Ŭ���̾�Ʈ ���� ������ ����
		ClientSendThread cst = new ClientSendThread(s);
		cst.setId(id);
		//������ ����
		cst.start();
	
		//Ŭ���̾�Ʈ ���ú� ������ ����
		ClientReceiveThread crt = new ClientReceiveThread(s);
		//������ ����
		crt.start();
	}
}
