import constants.Alphabet;
import constants.Language;

public class VigenerEncrpt implements Encryptor {
    // only eng or rus
    private final char[] alphabet;
    private final int alphabetLen;
    private final Language lang;

    VigenerEncrpt(Language lang) throws IllegalArgumentException {
        switch (lang) {
            case RUS -> {
                this.lang = lang;
                alphabet = Alphabet.rusAlphabet;
                alphabetLen = alphabet.length;
            }
            case ENG -> {
                this.lang = lang;
                alphabet = Alphabet.engAlphabet;
                alphabetLen = alphabet.length;
            }
            default -> throw new IllegalArgumentException("Only \"eng\" and \"rus\" supported.");
        }
    }

    public String encrypt(String msg, String key) {
        String circleKey = createKey(msg, key);
        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            boolean isLowerCase = Character.isLowerCase(msg.charAt(i));
            char toAppend = alphabet[((Alphabet.indexOf(lang, Character.toUpperCase(msg.charAt(i))) +
                    Alphabet.indexOf(lang, Character.toUpperCase(circleKey.charAt(i)))) % alphabetLen)];
            if (isLowerCase) toAppend = Character.toLowerCase(toAppend);
            cipherText.append(toAppend);
        }
        return cipherText.toString();
    }

    public String decrypt(String msg, String key) {
        String circleKey = createKey(msg, key);
        StringBuilder plainText = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            boolean isLowerCase = Character.isLowerCase(msg.charAt(i));
            char toAppend = alphabet[((alphabetLen + Alphabet.indexOf(lang, Character.toUpperCase(msg.charAt(i))) -
                    Alphabet.indexOf(lang, Character.toUpperCase(circleKey.charAt(i)))) % alphabetLen)];
            if (isLowerCase) toAppend = Character.toLowerCase(toAppend);
            plainText.append(toAppend);
        }

        return plainText.toString();
    }

    private String createKey(String msg, String key) {
        StringBuilder resKey = new StringBuilder(key);
        for (int i = 0; i < msg.length() - key.length(); i++) {
            boolean isLowerCase = Character.isLowerCase(resKey.charAt(i));
            char toAppend = resKey.charAt(i);
            if (isLowerCase) toAppend = Character.toLowerCase(toAppend);
            resKey.append(toAppend);
        }
        return resKey.toString();
    }

}
