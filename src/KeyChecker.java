import constants.Errors;
import constants.Language;

public class KeyChecker {

    private Language mode;

    public KeyChecker(Language mode) {
        setMode(mode);
    }

    public Language getMode() {
        return mode;
    }

    public void setMode(Language mode) {
        switch (mode) {
            case NUM, RUS, ENG -> this.mode = mode;
            default -> throw new IllegalArgumentException("Only \"num\", \"rus\" and \"eng\" supported.");
        }
    }

    public Errors checkKey(String key) {
        if (key.length() == 0) return Errors.ZERO_LEN;
        if (mode == Language.NUM) {
            try {
                if (Integer.parseInt(key) == 0) {
                    return Errors.ZERO;
                }
            } catch (NumberFormatException e) {
                return Errors.NUMBER_OVERFLOW;
            }
        }
        return null;
    }

}

