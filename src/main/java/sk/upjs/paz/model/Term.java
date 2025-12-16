package sk.upjs.paz.model;

public class Term {
    private long idTerms;
    private String type;
    private String date;
    private String startTime;
    private String endTime;
    private String address;
    private int capacity;
    private long districtId;


    public long getIdTerms() {
        return idTerms;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public long getDistrictId() {
        return districtId;
    }
}
