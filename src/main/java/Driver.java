package hw01_jotto_VeronicaCortezD.src.main.java;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Make a branch and start! :)
 */
public class Driver {
    public static void main(String[] args) throws IOException {
        String filepath = "src/hw01_jotto_VeronicaCortezD/src/main/java/resources/debug.txt";
//        String filepath = "debug.txt";
//        String filepath = "wordList.txt";
        Jotto game = new Jotto(filepath);
        game.play();


        ArrayList<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("there");
        strings.add("general");

        System.out.println(strings);

        for( String s : strings){
            System.out.println("In the for loop");
            System.out.println("value is : " + s);
        }

        System.out.println(game.readWords());
        System.out.println(game.readWords());
        game.updateWordList();
    }
}
