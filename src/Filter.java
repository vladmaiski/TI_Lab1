import constants.Language;

public class Filter {

    private Language lang;

    Filter(Language lang) {
        setLang(lang);
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        if (lang == Language.RUS || lang == Language.ENG || lang == Language.NUM) {
            this.lang = lang;
        } else {
            throw new IllegalArgumentException("only \"rus\", \"eng\" and \"num\" supported.");
        }
    }

    public String filterMsg(String msg, boolean isSpaceIncluded) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            if (isCorrectChar(msg.charAt(i), isSpaceIncluded)) {
                result.append(msg.charAt(i));
            }
        }
        return result.toString();
    }

    private boolean isCorrectChar(char c, boolean isSpaceIncluded) {
        boolean result = switch (lang) {
            case ENG -> ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
            case RUS -> (((c >= 'А' && c <= 'Я') || c == 'Ё') || ((c >= 'а' && c <= 'я') || c == 'ё'));
            case NUM -> (c >= '0' && c <= '9');
            default -> false;
        };
        if (isSpaceIncluded && !result)
            result = (c == ' ');
        return result;
    }
}
