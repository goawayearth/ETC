package demo;

import java.util.ArrayList;

public class RandomOrder {
     int[] choice=new int[5];//客户端，单词放下标为0的单词，四个选项的汉语随机排布，数组0--3是1--4的随机数，最后一个属放的是下标为0的汉语放在第几个
    public RandomOrder(){
    choice[0]=(int)(Math.random()*4+1);
        do{
        choice[1]=(int)(Math.random()*4+1);
    }while (choice[1] == choice[0]);
       do{
        choice[2]=(int)(Math.random()*4+1);
    }while (choice[2] == choice[0]||choice[2] == choice[1]);


       do{
        choice[3]=(int)(Math.random()*4+1);
    }while (choice[3] == choice[1]||choice[2] == choice[3]||choice[3] == choice[0]);
     for (int i = 0; i < 4;i++) {
         if(choice[i]==1) {
             choice[4] = i;
             break;
         }

}
}}
