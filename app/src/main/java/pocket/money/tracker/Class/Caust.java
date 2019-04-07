package pocket.money.tracker.Class;

public class Caust {
    String id ;
    String category;
    String budget;
    String date;
    String time;
    String amount;

    public Caust(String id, String category, String budget, String date, String time, String amount) {
        this.id = id;
        this.category = category;
        this.budget = budget;
        this.date = date;
        this.time = time;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
