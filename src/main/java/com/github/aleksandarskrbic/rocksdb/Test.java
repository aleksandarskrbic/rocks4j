package com.github.aleksandarskrbic.rocksdb;

import java.util.Collection;
import java.util.Optional;

public class Test {

    public static void main(final String[] args) {
        final RocksDBConfiguration rocksDBConfiguration = new RocksDBConfiguration() {
            @Override
            public String name() {
                return "db";
            }

            @Override
            public String path() {
                return "/data/repositories";
            }
        };

        ItemRepository itemRepository = new ItemRepository(rocksDBConfiguration);

        for (int i = 0; i < 10000000; i++) {
            Item item = new Item();
            item.setId((long) i);
            item.setDesc("Desc");
            itemRepository.save(item.getId(), item);
        }

        Optional<Item> byKey = itemRepository.findByKey(1L);
        Collection<Item> all = itemRepository.findAll();
        System.out.println();
    }

    public static class ItemRepository extends RocksDBKeyValueRepository<Long, Item> {

        public ItemRepository(final RocksDBConfiguration configuration) {
            super(configuration, Long.class, Item.class);
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
