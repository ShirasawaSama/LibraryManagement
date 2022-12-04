package cn.apisium.library;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

/**
 * 借书条目
 */
@JsonPropertyOrder({ "barcode", "userId", "issueDate", "dueDate", "numRenews" })
public final class Loan {
    @JsonProperty("Barcode")
    private String barcode;
    @JsonProperty("User_id")
    private String userId;
    @JsonProperty("Issue_Date")
    private String issueDate;
    @JsonProperty("Due_Date")
    private String dueDate;
    private int numRenews;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SuppressWarnings("unused")
    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    @SuppressWarnings("unused")
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getNumRenews() {
        return numRenews;
    }

    public void setNumRenews(int numRenews) {
        this.numRenews = numRenews;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "barcode='" + barcode + '\'' +
                ", userId='" + userId + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", numRenews='" + numRenews + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Loan loan = (Loan) o;

        if (!Objects.equals(barcode, loan.barcode)) return false;
        if (!Objects.equals(userId, loan.userId)) return false;
        if (!Objects.equals(issueDate, loan.issueDate)) return false;
        if (!Objects.equals(dueDate, loan.dueDate)) return false;
        return Objects.equals(numRenews, loan.numRenews);
    }

    @Override
    public int hashCode() {
        int result = barcode != null ? barcode.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (issueDate != null ? issueDate.hashCode() : 0);
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + (numRenews);
        return result;
    }
}
