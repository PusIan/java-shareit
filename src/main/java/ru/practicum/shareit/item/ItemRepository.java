package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findItemsByOwnerId(long ownerId);

    @Query("select i from Item as i" +
            " where (lower(i.description) like lower(concat('%', ?1, '%')) " +
            " or lower(i.name) like lower(concat('%', ?1, '%')))" +
            " and i.available = true")
    Collection<Item> findItemsByName(String text);
}
