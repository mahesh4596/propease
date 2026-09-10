package DealsFinder;

import com.itextpdf.layout.element.Cell;

public class DealsBean {
    int dealId;
    String seller, sellerContact, buyer, buyerContact, dealPrice, advanceAmt, balanceAmt, dealDate, registryDate, status;

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerContact() {
        return sellerContact;
    }

    public void setSellerContact(String sellerContact) {
        this.sellerContact = sellerContact;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyerContact() {
        return buyerContact;
    }

    public void setBuyerContact(String buyerContact) {
        this.buyerContact = buyerContact;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getAdvanceAmt() {
        return advanceAmt;
    }

    public void setAdvanceAmt(String advanceAmt) {
        this.advanceAmt = advanceAmt;
    }

    public String getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(String balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getRegistryDate() {
        return registryDate;
    }

    public void setRegistryDate(String registryDate) {
        this.registryDate = registryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DealsBean(int dealId, String seller, String sellerContact, String buyer, String buyerContact, String dealPrice, String advanceAmt, String balanceAmt, String dealDate, String registryDate, String status) {
        this.dealId = dealId;
        this.seller = seller;
        this.sellerContact = sellerContact;
        this.buyer = buyer;
        this.buyerContact = buyerContact;
        this.dealPrice = dealPrice;
        this.advanceAmt = advanceAmt;
        this.balanceAmt = balanceAmt;
        this.dealDate = dealDate;
        this.registryDate = registryDate;
        this.status = status;
    }
}
