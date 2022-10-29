package demo;//里面的choices里面装有随机的四个英语单词和翻译

import java.util.ArrayList;

public class MyRandom {
    int[] choice=new int[5];
    ArrayList<struct> choices=new ArrayList<>();
   public MyRandom(ArrayList<struct>list){
        choice[0]=(int)(Math.random()*2090);
        do{
            choice[1]=(int)(Math.random()*2090);
        }while (choice[1] == choice[0]);
       do{
           choice[2]=(int)(Math.random()*2090);
       }while (choice[2] == choice[0]||choice[2] == choice[1]);


       do{
           choice[3]=(int)(Math.random()*2090);
       }while (choice[3] == choice[1]||choice[2] == choice[3]||choice[3] == choice[0]);

        choices.add(list.get(choice[0]));
       choices.add(list.get(choice[1]));
       choices.add(list.get(choice[2]));
       choices.add(list.get(choice[3]));
    }
}
