package com.github.aleksandarskrbic.rocks4j;

import java.util.*;

import com.github.aleksandarskrbic.rocks4j.core.AsyncKVStore;
import com.github.aleksandarskrbic.rocks4j.core.RocksDBConfiguration;
import com.github.aleksandarskrbic.rocks4j.core.exception.DeleteAllFailedException;
import com.github.aleksandarskrbic.rocks4j.core.exception.FindFailedException;
import com.github.aleksandarskrbic.rocks4j.core.exception.SaveFailedException;
import com.github.aleksandarskrbic.rocks4j.mapper.exception.SerDeException;

public class TestExample {

    public static void main(final String[] args) throws SerDeException, SaveFailedException, DeleteAllFailedException, FindFailedException {
        final RocksDBConfiguration rocksDBConfiguration = new RocksDBConfiguration("/src/main/resources/data/repositories", "db");

        final ItemRepository itemRepository = new ItemRepository(rocksDBConfiguration);

        for (int i = 0; i < 100000; i++) {
            final Item item = new Item();
            item.setId((long) i);
            item.setDesc("Desc");
            final ArrayList<Integer> ints = new ArrayList<>();
            ints.add(1);
            item.setInts(ints);
            final ArrayList<Account> accounts = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                final Account account = new Account();
                account.setId((long) j);
                account.setName("Name " + j);
                accounts.add(account);
            }
            final Optional<Item> join = itemRepository.save(item.getId(), item).thenCompose(__ -> itemRepository.findByKey(item.id)).join();
            System.out.println(join);
        }

    }

    public static class ItemRepository extends AsyncKVStore<Long, Item> {

        public ItemRepository(final RocksDBConfiguration configuration) {
            super(configuration);
        }
    }

    public static class Item {
        private Long id;
        private String desc;
        private List<Integer> ints;
        private List<Account> accounts;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<Integer> getInts() {
            return ints;
        }

        public void setInts(List<Integer> ints) {
            this.ints = ints;
        }

        public List<Account> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<Account> accounts) {
            this.accounts = accounts;
        }
    }

    public static class Account {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
