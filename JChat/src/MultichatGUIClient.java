import java.awt.BorderLayout; 
import java.awt.Button; 
import java.awt.Color; 
import java.awt.Frame; 
import java.awt.Panel; 
import java.awt.TextArea; 
import java.awt.TextField; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent; 
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent; 
import java.io.DataInputStream; 
import java.io.DataOutputStream; 
import java.io.IOException; 
import java.net.ConnectException; 
import java.net.Socket; 
import java.util.Scanner; 

/** 
 * // ���� ��� // java MultichatGUIClient ��ȭ�� 
 *  
 * @since 2012. 07. 11. 
 * @author yanggun7201 
 */ 
public class MultichatGUIClient implements ActionListener {
     Frame f; 
    Panel p; 
    Button b1; 
    TextField tf; 
    TextArea ta; 
    // =============================== 
    String name; 
    ClientSender sender; 
    // =============================== 

    public MultichatGUIClient(String name) { 
        // =============================== 
        this.name = name; 
        f = new Frame(name); 
        // =============================== 

        p = new Panel(); 
        b1 = new Button("����");
         tf = new TextField(); 
        ta = new TextArea(20, 50);
     } 

    public void launchTest() {
         f.addWindowListener(new WindowAdapter() {
             public void windowClosing(WindowEvent e) {
                 System.exit(0); 
            } 
        }); 

        // frame�� �⺻������ BorderLayout�̴�. 
        f.add(BorderLayout.SOUTH, p); 

        b1.addActionListener(this); 
        b1.setBackground(Color.yellow); 

        tf.setColumns(40); 
        tf.addKeyListener(new KeyAdapter() { 
            public void keyReleased(KeyEvent e) {
                 char keyCode = e.getKeyChar();
                 if (keyCode == KeyEvent.VK_ENTER) {
                     actionPerformed(null); 
                } 
            } 
        }); 

        p.setBackground(Color.green); 
        p.add(tf); 
        p.add(b1); 

        f.add(BorderLayout.CENTER, ta); 

        f.setVisible(true); 
        f.pack(); 

        // =================================================
         Socket socket = null; 
        try { 
            String serverIp = "127.0.0.1"; 
            socket = new Socket(serverIp, 7777); // ������ �����Ͽ� ������ ��û�Ѵ�.
             System.out.println("������ ����Ǿ����ϴ�.");
 
            // �޽��� ���ۿ� Thread ���� 
            sender = new ClientSender(socket);
 
            // �޽��� ���ſ� Thread ���� 
            Thread receiver = new Thread(new ClientReceiver(socket));
 
            receiver.start(); 
        } catch (ConnectException ce) { 
            ce.printStackTrace(); 
        } catch (Exception e) { 
        } 
        // =================================================
     } 

    @Override 
    public void actionPerformed(ActionEvent e) {
         // ========================= 
        sender.send(tf.getText()); 
        // ========================= 
        tf.setText(""); 
        tf.requestFocus(); 
    } 

    // =================================================
     public void setMessage(String message) {
         ta.append(message); 
        ta.append("\n"); 
    } 
    // =================================================
 
    // ===============================================================================
     // �޽��� ���ۿ� ��ü  
    // Thread �ƴ�!!! (GUI �����̹Ƿ�, Event�� �޾Ƽ� ó����)
     class ClientSender { 
        Socket socket; 
        DataOutputStream out; 

        ClientSender(Socket socket) { 
            this.socket = socket; 

            try { 
                this.out = new DataOutputStream(socket.getOutputStream());
 
                // �������� ����, �ڽ��� ��ȭ���� ������ ���� 
                if (out != null) {
                     out.writeUTF(name); 
                } 

            } catch (Exception e) { 
            } 
        } 

        public void send(String message) {
             if (out != null) {
                 try { 
                    // Ű����� �Է¹��� �����͸� ������ ����
                     out.writeUTF("[" + name + "] " + message);
                 } catch (IOException e) { 
                } 
            } 
        } 
    } 

    // �޽��� ���ſ� Thread 
    class ClientReceiver implements Runnable {
         Socket socket; 
        DataInputStream in; 

        // ������ 
        ClientReceiver(Socket socket) { 
            this.socket = socket; 

            try { 
                // ������ ���� �����͸� ���� �� �ֵ��� DataInputStream ����
                 this.in = new DataInputStream(socket.getInputStream());
             } catch (IOException e) { 
            } 
        } 

        public void run() {
             while (in != null) { 
                try { 
                    // ������ ���� ���۵Ǵ� �����͸� ��� 
                    MultichatGUIClient.this.setMessage(in.readUTF());
                 } catch (IOException e) { 
                } 
            } 
        } 
    } 

    // ===============================================================================
 
    // ���� ��� 
    // java MultichatGUIClient ��ȭ�� 
    public static void main(String[] args) {
 
        String name = null; 
        Scanner scanner = new Scanner(System.in);
 
        do { 
            System.out.println("��ȭ���� �Է��ϼ���.");
             System.out.print(">>> ");
             name = scanner.nextLine(); 
            if (name.isEmpty()) { 
                System.out.println("��ȭ���� �ѱ��� �̻� �Է��ؾ� �մϴ�.\n\n");
             } 
        } while (name.isEmpty()); 

        new MultichatGUIClient(name).launchTest();
     } 
} 
// 


