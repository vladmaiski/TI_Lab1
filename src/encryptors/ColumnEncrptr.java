package encryptors;

import java.util.ArrayList;

public class ColumnEncrptr implements Encryptor {
    // only eng

    public String encrypt(String text, String key) {
        ArrayList<Pair<Character, StringBuilder>> table = new ArrayList<>();
        for (int i = 0; i < key.length(); i++) {
            table.add(new Pair<>(key.charAt(i), createColumn(text, i, key.length())));
        }
        table.sort(Pair::compareTo);
        StringBuilder result = new StringBuilder();
        for (Pair<Character, StringBuilder> p : table) {
            result.append(p.getSecVal());
        }

        return result.toString();
    }

    private StringBuilder createColumn(String text, int startEl, int keyLen) {
        StringBuilder resColumn = new StringBuilder();
        int curEl = startEl;
        while (curEl < text.length()) {
            resColumn.append(text.charAt(curEl));
            curEl += keyLen;
        }
        return resColumn;
    }

    public String decrypt(String text, String key) {
        ArrayList<Pair<Character, Integer>> amForSymbol= new ArrayList<>();
        int fullRows = text.length() / key.length();
        for (Character ch: key.toCharArray()) {
            amForSymbol.add(new Pair<>(ch, fullRows));
        }

        int rowsAmount = text.length() % key.length();
        for (int i = 0; i < rowsAmount; i++) {
            Pair<Character, Integer> currPair = amForSymbol.get(i);
            currPair.setSecVal(currPair.getSecVal() + 1);
        }

        amForSymbol.sort(Pair::compareTo);

        int curEl = 0;
        ArrayList<Pair<Character, String>> tmpArr = new ArrayList<>();
        for(Pair<Character, Integer> pair: amForSymbol) {
            int step = pair.getSecVal();
            tmpArr.add(new Pair<>(pair.getFirstVal(), text.substring(curEl, curEl + step)));
            curEl += step;
        }

        ArrayList<Pair<Character, StringBuilder>> resultList = new ArrayList<>();
        for(Character ch: key.toCharArray()) {
            for(Pair<Character, String> pair: tmpArr) {
                if (pair.getFirstVal().equals(ch)) {
                    resultList.add(new Pair<>(ch, new StringBuilder(pair.getSecVal())));
                    tmpArr.remove(pair);
                    break;
                }
            }
        }

        StringBuilder resText = new StringBuilder();

        for (int i = 0; i < fullRows; i++) {
            for (int j = 0; j < key.length(); j++) {
                resText.append(resultList.get(j).getSecVal().charAt(i));
            }
        }
        for (int i = 0; i < rowsAmount; i++) {
            resText.append(resultList.get(i).getSecVal().charAt(fullRows));
        }

        return resText.toString();
    }

}
