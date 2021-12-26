package com.yao.bio;

import java.io.*;

/**
 * @author yaojian
 * @date 2021/12/26 18:17
 */
public class BIO {

    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("e://1.txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String s= "";
            while((s=reader.readLine()) != null){
                System.out.println(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
