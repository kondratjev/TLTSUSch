package ru.kondratj3v.tltsusch.managers;


import ru.kondratj3v.tltsusch.R;

public class ThemeManager {

    public static String[] getSupportThemes() {
        return new String[]{"Стандартная", "Чёрная", "Индиго",
                "Чирок", "Серо-голубая", "Красная", "Зелёная"};
    }

    public static int getThemeStyle(int theme) {
        switch (theme) {
            case 0:
                return R.style.AppTheme;
            case 1:
                return R.style.AppThemeBlack;
            case 2:
                return R.style.AppThemeIndigo;
            case 3:
                return R.style.AppThemeTeal;
            case 4:
                return R.style.AppThemeGrayBlue;
            case 5:
                return R.style.AppThemeRed;
            case 6:
                return R.style.AppThemeGreen;
        }
        return 0;
    }
}
