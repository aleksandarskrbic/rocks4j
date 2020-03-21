package com.github.aleksandarskrbic.rocks4j;

import com.github.aleksandarskrbic.rocks4j.exception.DeserializationException;
import com.github.aleksandarskrbic.rocks4j.repository.RocksDBKeyValueRepository;

import java.util.Collection;
import java.util.Optional;

public class Test {

    public static void main(final String[] args) throws DeserializationException {
        final RocksDBConfiguration rocksDBConfiguration = new RocksDBConfiguration("/src/main/resources/data/repositories", "db");

        ItemRepository itemRepository = new ItemRepository(rocksDBConfiguration);

        for (int i = 0; i < 100000; i++) {
            Item item = new Item();
            item.setId((long) i);
            item.setDesc("Desc");
            itemRepository.save(item.getId(), item);
        }

        Optional<Item> byKey = itemRepository.findByKey(1L);
        Collection<Item> all = itemRepository.findAll();
        itemRepository.deleteAll();
        all = itemRepository.findAll();
        System.out.println();
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
