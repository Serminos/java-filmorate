package ru.yandex.practicum.filmorate.enums;

public enum SortBy {
    YEAR("year"),
    LIKES("likes");

    private final String value;

    SortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SortBy fromString(String value) {
        for (SortBy sortBy : SortBy.values()) {
            if (sortBy.getValue().equalsIgnoreCase(value)) {
                return sortBy;
            }
        }
        throw new IllegalArgumentException("Несуществующий тип сортировки: " + value);
    }
}
