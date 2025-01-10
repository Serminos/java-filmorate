package ru.yandex.practicum.filmorate.storage.impl.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.*;

@Component
@Qualifier("inMemoryRaitingMpaStorage")
public class InMemoryRaitingMpaStorage implements RatingMpaStorage {
    private final HashMap<Long, Map<String, String>> raitingMpaStorage = new HashMap<>();

    public InMemoryRaitingMpaStorage() {
        raitingMpaStorage.put(1L, Map.of("G", "Нет возрастных ограничений"));
        raitingMpaStorage.put(2L, Map.of("PG", "Детям рекомендуется смотреть фильм с родителями"));
        raitingMpaStorage.put(3L, Map.of("PG-13", "Детям до 13 лет просмотр не желателен"));
        raitingMpaStorage.put(4L, Map.of("R", "Лицам до 17 лет просматривать фильм можно " +
                "только в присутствии взрослого"));
        raitingMpaStorage.put(5L, Map.of("NC-17", ""));
        raitingMpaStorage.put(6L, Map.of("", "Лицам до 18 лет просмотр запрещён"));
    }

    @Override
    public List<RatingMpa> all() {
        List<RatingMpa> ratingMpaAll = new ArrayList<>();
        for (Map.Entry<Long, Map<String, String>> ratingMpaEntry : raitingMpaStorage.entrySet()) {
            RatingMpa ratingMpa = new RatingMpa();
            ratingMpa.setRatingMpaId(ratingMpaEntry.getKey());
            ratingMpa.setName(ratingMpaEntry.getValue().keySet().stream().findFirst().get());
            ratingMpa.setDescription(ratingMpaEntry.getValue().values().stream().findFirst().get());
            ratingMpaAll.add(ratingMpa);
        }
        return ratingMpaAll;
    }

    @Override
    public RatingMpa findRatingMpaById(long ratingMpaId) {
        RatingMpa findRatingMpaById = new RatingMpa();
        if (raitingMpaStorage.get(ratingMpaId) != null) {
            findRatingMpaById.setRatingMpaId(ratingMpaId);
            findRatingMpaById.setName(raitingMpaStorage.get(ratingMpaId).keySet().stream().findFirst().get());
            findRatingMpaById.setDescription(raitingMpaStorage.get(ratingMpaId).values().stream().findFirst().get());
        }
        return findRatingMpaById;
    }

    @Override
    public List<RatingMpa> findRatingMpaByIds(Set<Long> ratingMpaId) {
        List<RatingMpa> ratingMpaAll = new ArrayList<>();
        for (Map.Entry<Long, Map<String, String>> ratingMpaEntry : raitingMpaStorage.entrySet()) {
            if (ratingMpaId.contains(ratingMpaEntry.getKey())) {
                RatingMpa ratingMpa = new RatingMpa();
                ratingMpa.setRatingMpaId(ratingMpaEntry.getKey());
                ratingMpa.setName(ratingMpaEntry.getValue().keySet().stream().findFirst().get());
                ratingMpa.setDescription(ratingMpaEntry.getValue().values().stream().findFirst().get());
                ratingMpaAll.add(ratingMpa);
            }
        }
        return ratingMpaAll;
    }
}
