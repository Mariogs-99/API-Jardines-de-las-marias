package com.hotelJB.hotelJB_API.models.responses;

public class CategoryRoomResponse {
    private int categoryRoomId;
    private String nameCategory;
    private String description;
    private String roomSize;
    private String bedInfo;
    private String extraInfo;
    private Boolean hasTv;
    private Boolean hasAc;
    private Boolean hasPrivateBathroom;

    // Constructor completoServiceImpl
    public CategoryRoomResponse(int categoryRoomId, String nameCategory, String description,
                                String roomSize, String bedInfo, String extraInfo,
                                Boolean hasTv, Boolean hasAc, Boolean hasPrivateBathroom) {
        this.categoryRoomId = categoryRoomId;
        this.nameCategory = nameCategory;
        this.description = description;
        this.roomSize = roomSize;
        this.bedInfo = bedInfo;
        this.extraInfo = extraInfo;
        this.hasTv = hasTv;
        this.hasAc = hasAc;
        this.hasPrivateBathroom = hasPrivateBathroom;
    }

    // Getters y setters
    public int getCategoryRoomId() {
        return categoryRoomId;
    }

    public void setCategoryRoomId(int categoryRoomId) {
        this.categoryRoomId = categoryRoomId;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public String getBedInfo() {
        return bedInfo;
    }

    public void setBedInfo(String bedInfo) {
        this.bedInfo = bedInfo;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Boolean getHasTv() {
        return hasTv;
    }

    public void setHasTv(Boolean hasTv) {
        this.hasTv = hasTv;
    }

    public Boolean getHasAc() {
        return hasAc;
    }

    public void setHasAc(Boolean hasAc) {
        this.hasAc = hasAc;
    }

    public Boolean getHasPrivateBathroom() {
        return hasPrivateBathroom;
    }

    public void setHasPrivateBathroom(Boolean hasPrivateBathroom) {
        this.hasPrivateBathroom = hasPrivateBathroom;
    }
}
