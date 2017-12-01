package com.ashoksm.exceltoxml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String test = "The provisions of this Code apply also to any offence committed by-\n" +
                "\n" +
                "10[(1) any citizen of India in any place without and beyond India;\n" +
                "\n" +
                "(2) any person on any ship or aircraft registered in India wherever it may be.]\n" +
                "\n" +
                "Explanation- In this section the word \"offence\" includes every act committed outside 2[India] which, if committed in 2[India], would be punishable under this Code.\n" +
                "\n" +
                "11[Illustration]\n" +
                "\n" +
                "12[***] A, 13[who is 14[a citizen of India]] , commits a murder in Uganda. He can be tried and convicted of murder in any place in 2[India] in which he may be found.\n" +
                "\n" +
                "15[***]";
        String regEx = "[0-9]+[\\[]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(test);

        while (matcher.find()) {
            String replacement =
                    "<font color=\"#1565C0\"><u><small>" + matcher.group().substring(0, matcher.group().length() - 1) +
                            "</small></u></font>[";
            test = matcher.replaceFirst(replacement);
            matcher = pattern.matcher(test);
        }
        System.out.println(test);
    }
}
