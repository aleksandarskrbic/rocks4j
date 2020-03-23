# rocks4j

KV Store for Java backed by RocksDB.

## Getting Started

### Maven
```java
<dependency>
  <groupId>com.github.aleksandarskrbic</groupId>
  <artifactId>rocks4j</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Gradle
```java
compile "com.github.aleksandarskrbic:rocks4j:1.0.0"
```

## Introduction
The goal of this project is to provide KV store backed by RocksDB with simple Java API.
Allow developers to quickly integrate RocksDB into the existing Java/Scala project.
It can be used as a local caching mechanism in microservices or just to replace huge in-memory data structures.

## API

There are five available operations against KV repository:

* `save(K key, V value)`
* `findByKey(K key)`
* `findAll()`
* `deleteByKey(K key)`
* `deleteAll()`

## Examples

Suppose you have a Java class:

```java
public class Item {
    private Long id;
    private String desc;
    
    // constructors, getters and setters
}
```

To create KV repository for Item class, firstly you need to create a configuration class that accepts a path to file (where RocksDB files will be) and the name of the repository: 

```java
final RocksDBConfiguration configuration = new RocksDBConfiguration("/path/to/rocksdb", "item");
```
After configuration class is created, you are able to instantiate the Item Repository:

```java
public class ItemRepository extends RocksDBKeyValueRepository<Long, Item> {

  public ItemRepository(final RocksDBConfiguration configuration) {
    super(configuration);
  }
  
}
```

```java
final ItemRepository itemRepository = new ItemRepository(configuration);
```

```java
final Item item = new Item(1L, "Desc")
```

```java
itemRepository.save(item.getId(), item);
final Optional<Item> itemOptional = itemRepository.findByKey(item.getId());
final Collection<Item> all = itemRepository.findAll();
itemRepository.deleteByKey(item.getId());
itemRepository.deleteAll();
```

## Note

In order to maintain flexibility, when exception related to `RocksDB` or `Serialization/Deserialization` in `RocksDBKeyValueRepository`,
exceptions are just propagated, so it's a client's responsibility to deal with it.
Best practice would be to override methods from `RocksDBKeyValueRepository` and 
handle those exceptions and handle those exceptions into your repository class.
Another solution would be to handle
it every time you invoke methods provided by `RocksDBKeyValueRepository` which is not recommended.

### Example

```java
public static class ItemRepository extends RocksDBKeyValueRepository<Long, Item> {

    public ItemRepository(final RocksDBConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void save(final Long key, final Item value) {
        try {
            super.save(key, value);
        } catch (final SerializationException e) {
            // log or handle
        } catch (final SaveFailedException e) {
            // log or handle
        }
    }

    // other methods
}
```
