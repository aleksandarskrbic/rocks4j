package com.github.aleksandarskrbic.rocks4j;

import java.util.Collection;
import java.util.Optional;
import com.github.aleksandarskrbic.rocks4j.configuration.RocksDBConfiguration;
import com.github.aleksandarskrbic.rocks4j.kv.exception.DeleteAllFailedException;
import com.github.aleksandarskrbic.rocks4j.kv.exception.FindFailedException;
import com.github.aleksandarskrbic.rocks4j.kv.exception.SaveFailedException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerDeException;
import com.github.aleksandarskrbic.rocks4j.repository.RocksDBKeyValueRepository;

public class Test {

    public static void main(final String[] args) throws SerDeException, SaveFailedException, DeleteAllFailedException, FindFailedException {
        final RocksDBConfiguration rocksDBConfiguration = new RocksDBConfiguration("/src/main/resources/data/repositories", "db");

        final ItemRepository itemRepository = new ItemRepository(rocksDBConfiguration);

        for (int i = 0; i < 100000; i++) {
            final Item item = new Item();
            item.setId((long) i);
            item.setDesc("Desc");
            itemRepository.save(item.getId(), item);
        }

        final Optional<Item> byKey = itemRepository.findByKey(1L);
        Collection<Item> all = itemRepository.findAll();
        System.out.println("all size " + all.size());
        itemRepository.deleteAll();
        all = itemRepository.findAll();
        System.out.println("all size after delete " + all.size());
    }

    public static class ItemRepository extends RocksDBKeyValueRepository<Long, Item> {

        public ItemRepository(final RocksDBConfiguration configuration) {
            super(configuration);
        }
    }

    public static class Item {
        private Long id;
        private String desc;

        public Long getId() {
            return id;
        }

        public void setId(final Long id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(final String desc) {
            this.desc = desc;
        }
    }
}
