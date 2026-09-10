package BrowseCustomers;

import javafx.scene.image.Image;

public class CustomersBean {

    String mobile, cname, address, city, email, doe;
    Image pic, acard;
    byte[] picBytes, acardBytes;

    public void setPicBytes(byte[] picBytes) {
        this.picBytes = picBytes;
    }

    public void setAcardBytes(byte[] acardBytes) {
        this.acardBytes = acardBytes;
    }

    public byte[] getPicBytes() {
        return picBytes;
    }

    public byte[] getAcardBytes() {
        return acardBytes;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public Image getPic() {
        return pic;
    }

    public void setPic(Image pic) {
        this.pic = pic;
    }

    public Image getAcard() {
        return acard;
    }

    public void setAcard(Image acard) {
        this.acard = acard;
    }

    public CustomersBean() {
    }

    public CustomersBean(String mobile,String cname,String address,String city,String email,String doe,Image pic,Image acard,byte[] picBytes,byte[] acardBytes) {
        this.mobile = mobile;
        this.cname = cname;
        this.address = address;
        this.city = city;
        this.email = email;
        this.doe = doe;
        this.pic = pic;
        this.acard = acard;
        this.picBytes = picBytes;
        this.acardBytes = acardBytes;
    }

    @Override
    public String toString() {
        return "CustomersBean{" +
                "mobile='" + mobile + '\'' +
                ", name='" + cname + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                ", pic='" + pic + '\'' +
                ", adhaar='" + acard + '\'' +
                ", doe='" + doe + '\'' +
                '}';
    }
}
