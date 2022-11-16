import java.util.List;

public class Item {
    private Long id;
    private String desc;
    private List<Integer> ints;
    private List<Account> accounts;

    public Item() {
    }

    public Item(final Long id, final String desc, final List<Integer> ints, final List<Account> accounts) {
        this.id = id;
        this.desc = desc;
        this.ints = ints;
        this.accounts = accounts;
    }

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
