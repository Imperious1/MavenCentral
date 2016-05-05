package imperiumnet.gradleplease.utils;

import java.lang.reflect.Field;

import imperiumnet.gradleplease.R;

//This file was made by Cj Smith -- https://github.com/Mr-Smithy-x/
public class UtilReflection {
    public static int getTheme(String theme_value) {
        Class clazz = R.style.class;
        theme_value = theme_value.substring(theme_value.lastIndexOf(".") + 1);
        try {
            Field field = clazz.getDeclaredField(theme_value);
            try {
                return field.getInt(field.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return R.style.TealTheme;
    }
}
