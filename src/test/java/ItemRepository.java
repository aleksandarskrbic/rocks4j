import com.github.aleksandarskrbic.rocks4j.core.KVStore;
import com.github.aleksandarskrbic.rocks4j.core.RocksDBConfiguration;

public class ItemRepository extends KVStore<Long, Item> {

    public ItemRepository(final RocksDBConfiguration configuration) {
        super(configuration);
    }
}