package bean;

public class RawBinlog <T>{
    public String op;
    public T before;
    public T after;
    public String db;
    public String tableName;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Object getBefore() {
        return before;
    }

    public void setBefore(T before) {
        this.before = before;
    }

    public Object getAfter() {
        return after;
    }

    public void setAfter(T after) {
        this.after = after;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public RawBinlog() {
    }

    public RawBinlog(String op, T before, T after, String db, String tableName) {
        this.op = op;
        this.before = before;
        this.after = after;
        this.db = db;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "SourceBinlog{" +
                "op='" + op + '\'' +
                ", before=" + before +
                ", after=" + after +
                ", db='" + db + '\'' +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
