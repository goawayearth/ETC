package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class MyFrame extends JPanel implements Runnable, ActionListener {
    private File file1=new File("D:\\STUDY\\JAVA\\Code\\IDEA\\ETC_6\\lib\\can.txt");//指向会的单词文件
    private File file2=new File("D:\\STUDY\\JAVA\\Code\\IDEA\\ETC_6\\lib\\cannot.txt");//指向不会的单词文件
    private FileReader fileReader1=new FileReader(file1);
    private FileReader fileReader2=new FileReader(file2);
    private BufferedReader fr1=new BufferedReader(fileReader1);
    private BufferedReader fr2=new BufferedReader(fileReader2);

    private Thread th=new Thread(this);
    private int correct=0;//记录哪个正确
    private int grade=10;//显示在屏幕上
    private boolean RUN=true;

    //五个静态文本
    private  JLabel jLabel1=new JLabel();
    private JLabel jLabel2=new JLabel();
    private JLabel jLabel3=new JLabel();
    private JLabel jLabel4=new JLabel();
    private JLabel jLabel5=new JLabel();
    private  JLabel GradeLabel =new JLabel();

    private JLabel flection=new JLabel();//反馈回答是否正确
    private JLabel notstart=new JLabel();//结束提醒

    //四个按钮
    private JButton jBut1=new JButton("A");
    private JButton jBut2=new JButton("B");
    private JButton jBut3=new JButton("C");
    private JButton jBut4=new JButton("D");
    private RandomOrder randomOrder=null;

    private int wordX=250;
    private int wordY=0;
    private PrintStream printStream1=null;//输入到学会的单词文件
    private PrintStream printStream2=null;//输入到没学会的文件
    private PrintStream ps=null;//向服务器传输
    private BufferedReader bfr=null;//从服务器获取
    private Socket s=null;//客户端接收


   private  ArrayList<String>list=new ArrayList<>();

   private ArrayList<String> canArray=new ArrayList<>();
   private ArrayList<String>cannotArray=new ArrayList<>();


   private void rewriteWord()throws Exception{
       while(true){
           String s=null;
           s=fr1.readLine();

           if(s==null)break;
            canArray.add(s);
       }
       while(true){
           String s=null;
           s=fr2.readLine();
           if(s==null)break;
           cannotArray.add(s);
       }
       fileReader1.close();
       fileReader2.close();
       printStream2=new PrintStream(file2);//输入到没学会的文件
       printStream1=new PrintStream(file1);//输入到学会的单词文件
       for(int i=0;i<canArray.size();i++){

           printStream1.println(canArray.get(i));

       }
       for(int i=0;i<cannotArray.size();i++){

           printStream2.println(cannotArray.get(i));
       }
   }
//界面初始化
    private void initPanel(){
        this.setLayout(null);
        this.add(jBut1);
        this.add(jBut2);
        this.add(jBut3);
        this.add(jBut4 );
        this.setBackground(new Color(194, 106, 209, 255));

        this.add(jLabel1);
        this.add(jLabel2);
        this.add(jLabel3);
        this.add(jLabel4);
        this.add(jLabel5);
        this.add(GradeLabel);
        this.add(flection);
        this.add(notstart);

        jBut1.setSize(60,40);
        jBut2.setSize(60,40);
        jBut3.setSize(60,40);
        jBut4.setSize(60,40);

        jBut1.setLocation(5,500);
        jBut2.setLocation(5,550);
        jBut3.setLocation(5,600);
        jBut4.setLocation(5,650);

        jBut1.addActionListener(this);
        jBut2.addActionListener(this);
        jBut3.addActionListener(this);
        jBut4.addActionListener(this);
        jBut1.setBackground(new Color(235, 95, 76));
        jBut2.setBackground(new Color(209, 203, 42));
        jBut3.setBackground(new Color(20, 80, 210));
        jBut4.setBackground(new Color(87, 209, 42));

        flection.setSize(300,30);
        flection.setLocation(20,300);

        GradeLabel.setLocation(380,10);
        GradeLabel.setSize(200,20);

        jLabel1.setSize(300,40);
        jLabel2.setSize(300,40);
        jLabel3.setSize(300,40);
        jLabel4.setSize(300,40);
        jLabel5.setSize(300,40);

        jLabel1.setLocation(250,0);
        jLabel2.setLocation(80,500);
        jLabel3.setLocation(80,550);
        jLabel4.setLocation(80,600);
        jLabel5.setLocation(80,650);

        jLabel1.setFont(new Font("楷体",Font.BOLD,20));
        jLabel1.setForeground(new Color(126, 227, 35));
        jLabel2.setFont(new Font("楷体",Font.BOLD,20));
        jLabel3.setFont(new Font("楷体",Font.BOLD,20));
        jLabel4.setFont(new Font("楷体",Font.BOLD,20));
        jLabel5.setFont(new Font("楷体",Font.BOLD,20));
        flection.setFont(new Font("楷体",Font.BOLD,20));
        GradeLabel.setFont(new Font("楷体",Font.BOLD,20));

    }
    public MyFrame()throws Exception {
        rewriteWord();//将已经学过的单词重新写入
        initPanel();//初始化面板
        s = new Socket("100.120.44.230", 9999);//连接上服务器
        bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));//从服务器读取数据
        ps=new PrintStream(s.getOutputStream());//向服务器输出#
        initTh();
    }
    //得到一个单词和四个翻译放在list容器里
    private void set_Text()throws Exception{
        ps.println("#");
        String word,t1,t2,t3,t4;
        list.clear();

        word=bfr.readLine();
        t1=bfr.readLine();
        t2=bfr.readLine();
        t3=bfr.readLine();
        t4=bfr.readLine();

        list.add(word);//单词
        list.add(t1);//单词对应的翻译
        list.add(t2);
        list.add(t3);
        list.add(t4);


        randomOrder=new RandomOrder();//里面0123时1234的乱排
        wordX=250;
        wordY=0;
        //为文本设置文字
        jLabel1.setText(list.get(0));
        jLabel1.setLocation(250,0);
        correct=randomOrder.choice[4];//标记下标为几的汉语是正确选项
        jLabel2.setText(list.get(randomOrder.choice[0]));
        jLabel3.setText(list.get(randomOrder.choice[1]));
        jLabel4.setText(list.get(randomOrder.choice[2]));
        jLabel5.setText(list.get(randomOrder.choice[3]));
        //得分设置
        GradeLabel.setText("目前得分是："+grade);
    }

//重新设置文本
    void initTh()throws Exception{
        set_Text();
        if(grade<=0)
        {
            printStream1.close();
            printStream2.close();
            notstart.setText("游戏结束!");
            notstart.setLocation(200,400);
            notstart.setSize(200,50);
            notstart.setFont(new Font("宋体", Font.BOLD,30));
        }
        else
       th.start();
    }

    public void actionPerformed(ActionEvent e) {
        if(correct==0){
            if(e.getSource()==jBut1){
                //屏墓显示回答正确
            flection.setText("回答正确！");
            printStream1.println(list.get(0));
                printStream1.println(list.get(1));
            grade++;
            wordX=250;
            wordY=0;

            }
            else{
                //屏幕显示回答错误
                flection.setText("回答错误，正确答案是:A");
                printStream2.println(list.get(0));
                printStream2.println(list.get(1));
                grade-=2;
                wordX=250;
                wordY=0;

            }
        }
        if(correct==1){
            if(e.getSource()==jBut2){
                //屏墓显示回答正确
                flection.setText("回答正确！");
                printStream1.println(list.get(0));
                printStream1.println(list.get(1));
                grade++;
                wordX=250;
                wordY=0;

            }
            else{
                //屏幕显示回答错误
                flection.setText("回答错误，正确答案是:B");
                printStream2.println(list.get(0));
                printStream2.println(list.get(1));
                grade-=2;
                wordX=250;
                wordY=0;

            }
        }
        if(correct==2){
            if(e.getSource()==jBut3){
                //屏墓显示回答正确
                flection.setText("回答正确！");
                printStream1.println(list.get(0));
                printStream1.println(list.get(1));
                grade++;
                wordX=250;
                wordY=0;

            }
            else{
                //屏幕显示回答错误
                flection.setText("回答错误，正确答案是:C");
                printStream2.println(list.get(0));
                printStream2.println(list.get(1));
                grade-=2;
                wordX=250;
                wordY=0;

            }
        }
        if(correct==3){
            if(e.getSource()==jBut4){
                //屏墓显示回答正确
                flection.setText("回答正确！");
                printStream1.println(list.get(0));
                printStream1.println(list.get(1));
                grade++;
                wordX=250;
                wordY=0;

            }
            else{
                //屏幕显示回答错误
                flection.setText("回答错误，正确答案是:D");
                printStream2.println(list.get(0));
                printStream2.println(list.get(1));
                grade-=2;
                wordX=250;
                wordY=0;
            }
        }
       // RUN=false;
        try{initTh();}
        catch (Exception ex){}
    }
    public void run() {

    while (RUN) {
         try {
             jLabel1.setLocation(wordX, wordY);
             Thread.sleep(200);
            wordY = wordY + 10;
            if(wordY>600)
            {
                printStream2.println(list.get(0));
                printStream2.println(list.get(1));
                grade--;
                try{set_Text();}
                catch (Exception e){}
            }
            if(grade<=0){
                notstart.setText("游戏结束!");
//                notstart.setLocation(200,400);
//                notstart.setSize(200,50);
//                notstart.setFont(new Font("宋体", Font.BOLD,30));
                printStream2.close();
                printStream1.close();
                RUN=false;
            }
            }
         catch (InterruptedException e) {}
}

    }
}
