package com.hqyj.dev.farsighttoplogy.tools;

/**
 * Created by jiyangkang on 2016/6/2 0002.
 */
public class StringTools {
    /**
     * change int into String use Hex type
     * @param data the number which to be changed
     * @param haveBlank info if this is an space between two bytes
     * @return Hex String which means the same as data
     */
    public static String changeIntoHexString(int data, boolean haveBlank){
        String result;
        byte[] datas = new byte[4];
        for (int i = datas.length - 1; i >= 0; i--){
            datas[i] = (byte) (data >> (24 - i*8) & 0x00ff);
        }
        result = changeIntoHexString(datas,haveBlank);

        return result;
    }


    /**
     * change bytes into string type
     * @param datas num which to be changed
     * @param haveBlank info is their is a space between two bytes
     * @return the String which have the same means of datas
     */
    public static String changeIntoHexString(byte[] datas, boolean haveBlank){

        StringBuilder stringBuilder = new StringBuilder();
        for (byte data1 : datas) {
            byte b = (byte) (data1 & 0x0f);
            byte a = (byte) (data1 >> 4 & 0x0f);
            if (a > 9) {
                char c = (char) (a+ 55);
                stringBuilder.append(c);
            } else {
                stringBuilder.append(a);
            }
            if (b > 9) {
                char c = (char) (b+ 55);
                stringBuilder.append(c);
            } else {
                stringBuilder.append(b);
            }
            if (haveBlank) {
                stringBuilder.append(' ');
            }
        }

        return stringBuilder.toString();
    }

}
