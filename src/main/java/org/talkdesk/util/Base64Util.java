package org.talkdesk.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {

    public static String strConvertBase(String str) {
        if(null != str){
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(str.getBytes());
        }
        return null;
    }

    public static String baseConvertStr(String str) {
        if(null != str){
            Base64.Decoder decoder = Base64.getDecoder();
            try {
                return new String(decoder.decode(str.getBytes()), "GBK");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args){
        String c = "SSBhbSBpbiBXdWhhbixjYW4gY29tZSBmb3IgbWU/CgoKCkF0IDIwMjEtMDUtMDYgMDk6NDM6NTIs" +
                "ICJnbWFpbCIgPGNhaXhpbjExMTBAZ21haWwuY29tPiB3cm90ZToKCndoZXJlIGFyZSB5b3Ugbm93";
        String s = baseConvertStr(c);

        String c1 ="SSBhbSBpbiBXdWhhbixjYW4gY29tZSBmb3IgbWU/PGJyPjxicj48cD5BdCAyMDIxLTA1LTA2IDA5" +
                "OjQzOjUyLCAiZ21haWwiICZsdDtjYWl4aW4xMTEwQGdtYWlsLmNvbSZndDsgd3JvdGU6PC9wPjxi" +
                "bG9ja3F1b3RlIGlkPSJpc1JlcGx5Q29udGVudCIgc3R5bGU9IlBBRERJTkctTEVGVDogMWV4OyBN" +
                "QVJHSU46IDBweCAwcHggMHB4IDAuOGV4OyBCT1JERVItTEVGVDogI2NjYyAxcHggc29saWQiPndo" +
                "ZXJlIGFyZSB5b3Ugbm93PC9ibG9ja3F1b3RlPjxicj48YnI+PHNwYW4gdGl0bGU9Im5ldGVhc2Vm" +
                "b290ZXIiPjxwPiZuYnNwOzwvcD48L3NwYW4+";

        String s1 ="PGRpdiBzdHlsZT0iZm9udDoxNHB4LzEuNSAnTHVjaWRhIEdyYW5kZSc7IGNvbG9yOiMzMzM7" +
                "Ij48cCBzdHlsZT0iZm9udDoxNHB4LzEuNSAnTHVjaWRhIEdyYW5kZSc7bWFyZ2luOjA7Ij55" +
                "b3UgbGlhbmdnZSB6aGVnPC9wPjwvZGl2Pg==";

        String put = baseConvertStr(s1);
        System.out.println("-------------------");
       // System.out.println(HTMLUtil.removeHtmlTag(put));
        System.out.println("-----------------------");
    }
}
