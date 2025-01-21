package ru.yandex.practicum.filmorate.enums;

public enum SortBy {
    YEAR,
    LIKES;

    public static SortBy fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Сортировка может быть только по параметрам: year или likes");
        }

        try {
            return SortBy.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Сортировка может быть только по параметрам: year или likes");
        }
    }
}