package PropertyFinder;

import javafx.scene.image.Image;

public class PropertyBean {
    String mobile, location, area, city, size, front, rear, lft, rght, facing, proptype, constype, approvedby, price, status;
    Image pic1;
    byte[] imageBytes;

    private String dimensions;

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getRear() {
        return rear;
    }

    public void setRear(String rear) {
        this.rear = rear;
    }

    public String getLft() {
        return lft;
    }

    public void setLft(String lft) {
        this.lft = lft;
    }

    public String getRght() {
        return rght;
    }

    public void setRght(String rght) {
        this.rght = rght;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public String getProptype() {
        return proptype;
    }

    public void setProptype(String proptype) {
        this.proptype = proptype;
    }

    public String getConstype() {
        return constype;
    }

    public void setConstype(String constype) {
        this.constype = constype;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Image getPic1() {
        return pic1;
    }

    public void setPic1(Image pic1) {
        this.pic1 = pic1;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public PropertyBean(String mobile, String location, String area, String city, String size, String front, String rear, String lft, String rght, String facing, String proptype, String constype, String approvedby, String price, String status, Image pic1, byte[] imageBytes, String dimensions) {
        this.mobile = mobile;
        this.location = location;
        this.area = area;
        this.city = city;
        this.size = size;
        this.front = front;
        this.rear = rear;
        this.lft = lft;
        this.rght = rght;
        this.facing = facing;
        this.proptype = proptype;
        this.constype = constype;
        this.approvedby = approvedby;
        this.price = price;
        this.status = status;
        this.pic1 = pic1;
        this.imageBytes = imageBytes;
        this.dimensions = dimensions;
    }
}
