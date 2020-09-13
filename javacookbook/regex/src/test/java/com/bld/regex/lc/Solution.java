package com.bld.regex.lc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    public static void main(String[] args) {
//        String logEntryPattern =
//                "^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+-]\\d{4})\\] " +
//                        "\"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
        final String[] urls = {
                "https://git.nx-code.com/bu5/tpl/cicd-java-refer.git",
                "git@git.nx-code.com:bu5/tpl/cicd-java-refer.git",
                "http://git.nx-code.com/bu5/tpl/cicd-java-refer.git",
        };
        String logEntryPattern = "^(git@|https://|http://)([-\\w\\._]+)[:/]{1}([-\\w\\._]+)[/]([-\\w\\._]+).*";
        System.out.println("RE Pattern:");
        System.out.println(logEntryPattern);
        Pattern p = Pattern.compile(logEntryPattern);

        for (String line : urls) {
            System.out.println(line);
            Matcher m = p.matcher(line);

            System.out.println(m.matches());
            System.out.println(m.groupCount());
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            System.out.println(m.group(3));
            System.out.println(m.group(4));
            System.out.println("###\n");
        }
    }
}
