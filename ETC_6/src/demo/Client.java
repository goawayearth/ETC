package demo;

import javax.swing.*;
public class Client extends JFrame{
    public Client()throws Exception {

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);//❌关闭
        this.setSize(600, 750);//界面大小
        this.setLocation(700, 50);//界面位置
        this.setVisible(true);//可见
        this.setTitle("客户端");//界面名称
        this.setResizable(false);
        this.add( new MyFrame());
    }
    public static void main(String[] args)throws Exception {
        new Client();

    }
}
