package demo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends JFrame {
    private ArrayList<struct> array=new ArrayList<>();//存放六级所有单词
    private ArrayList<struct> array1=new ArrayList<>();//存放已经传输过的单词

    private File file=new File("D:\\STUDY\\JAVA\\Code\\IDEA\\ETC_6\\word1.txt");//从总单词表里读取单词
    private FileReader fr=new FileReader(file);
    private BufferedReader buf=new BufferedReader(fr);//从总文件按行读取

    private File file1=new File("D:\\STUDY\\JAVA\\Code\\IDEA\\ETC_6\\lib\\can.txt");//指向会的单词文件
    private File file2=new File("D:\\STUDY\\JAVA\\Code\\IDEA\\ETC_6\\lib\\cannot.txt");//指向不会的单词文件
    private FileReader fileReader1=new FileReader(file1);
    private FileReader fileReader2=new FileReader(file2);
    private BufferedReader fr1=new BufferedReader(fileReader1);//从会的文件里面读取到已传输容器
    private BufferedReader fr2=new BufferedReader(fileReader2);//从不会的文件里面读取到已传输容器

    private PrintStream ps=null;//向客户端传输
    private BufferedReader br=null;
   private String word=null;
    private String transtant=null;
    private MyRandom myRandom=null;//从总单词容器读取单词
    private JPanel jpanel=new JPanel();
    private JLabel jd=new JLabel();
    private JLabel js=new JLabel();
    private int sum=0;

    //将已经传输过的单词读取到array1容器
    private void readup()throws IOException{
        while (true) {
            word=fr1.readLine();
            if(word==null) {
                break;
            }
            word=word.trim();
            transtant=fr1.readLine();
            transtant=transtant.trim();
            struct ad=new struct();
            ad.word=word;
            ad.transt=transtant;
            array1.add(ad);
        }//这时六级单词存放在array里面
        while (true) {
            word=fr2.readLine();
            if(word==null) {
                break;
            }
            word=word.trim();
            transtant=fr2.readLine();
            transtant=transtant.trim();
            struct ad=new struct();
            ad.word=word;
            ad.transt=transtant;
            array1.add(ad);
        }//这时六级单词存放在array里面
        fr1.close();
        fr2.close();
    }
    //判读该单词是否已经传输过，没传输过返回true
    private boolean equals(struct obj){
        boolean flag=true;//如果不重复，就是返回true
        int size=array1.size();
        for(int i=0;i<size;i++){
            if(array1.get(i)==obj){
                flag = false;
            }
        }
        return flag;
    }
    //对界面进行初始化设置
    private void init() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);//❌关闭
        this.setSize(300,500);//界面大小
        this.setLocation(300,60);//界面位置
        this.setVisible(true);//可见
        this.setTitle("服务器");//界面名称

        jpanel.setLayout(null);
        this.add(jpanel);
        jpanel.add(js);
        js.setSize(200,50);
        js.setLocation(10,30);
        js.setFont(new Font("宋体",Font.BOLD,20));
        jpanel.setBackground(new Color(235, 113, 96));
        jd.setFont(new Font("宋体",Font.BOLD,20));
        jpanel.add(jd);
        jd.setLocation(10,10);
        jd.setSize(200,50);
    }

    public Server() throws Exception{
        init();
        readup();
        ServerSocket ss=new ServerSocket(9999);
        Socket s=ss.accept();//客户端连接上服务器
        jd.setText("连接成功！");
        ps=new PrintStream(s.getOutputStream());
        br=new BufferedReader(new InputStreamReader(s.getInputStream()));
        while (true) {
            word=buf.readLine();
            if(word==null) {
                break;
            }
            word=word.trim();
            transtant=buf.readLine();
            transtant=transtant.trim();
            struct ad=new struct();
            ad.word=word;
            ad.transt=transtant;
            array.add(ad);
        }//这时六级单词存放在array里面
        fr.close();

        while (true) {
            //接受客户端的信息，每接收一次，发出一个单词和四个汉语
            String string=br.readLine();
            if(string.equals("#")){//接收到#开始传输信息
                do{
                    myRandom=new MyRandom(array);

                }while (!equals(myRandom.choices.get(0)));
                ps.println(myRandom.choices.get(0).word);
                ps.println(myRandom.choices.get(0).transt);
                ps.println(myRandom.choices.get(1).transt);
                ps.println(myRandom.choices.get(2).transt);
                ps.println(myRandom.choices.get(3).transt);
                array1.add(myRandom.choices.get(0));
                sum+=1;
                js.setText("已传输"+sum+"个单词！");
            }
        }
    }

    public static void main(String[] args)throws Exception {
        new Server();
    }
}
