import com.github.aleksandarskrbic.rocks4j.core.exception.FindFailedException;
import com.github.aleksandarskrbic.rocks4j.core.exception.SaveFailedException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerDeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import com.github.aleksandarskrbic.rocks4j.core.RocksDBConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyValueStoreTest {

    final RocksDBConfiguration rocksDBConfiguration = new RocksDBConfiguration("/src/main/resources/data/repositories", "db");
    final ItemRepository itemRepository = new ItemRepository(rocksDBConfiguration);

    @Test
    @DisplayName("Save and Get Item from Repository")
    void saveAndGet() throws SerDeException, SaveFailedException, FindFailedException {
        final Item item = new Item(
            1L,
            "Item Description",
            IntStream.range(0, 100).boxed().collect(Collectors.toList()),
            LongStream.range(0, 10).boxed().map(id -> new Account(id, "name" + id)).collect(Collectors.toList())
            );

        itemRepository.save(item.getId(), item);
        Optional<Item> maybeItem = itemRepository.findByKey(item.getId());

        assertTrue(maybeItem.isPresent());
    }
}
