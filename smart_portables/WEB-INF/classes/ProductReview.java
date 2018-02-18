import java.util.*;

public class ProductReview implements java.io.Serializable {
        String prodModelName;
        String prodCategory;
        int prodPrice;
        String retailerName;
        String retailerZip;
        String retailerCity;
        String retailerState;
        String prodOnSale;
        String manuName;
        String manuRebate;
        String userID;
        int userAge;
        String userGender;
        String userOccupation;
        int reviewRating;
        String reviewDate;
        String reviewText;
        int count;

        public ProductReview(){

        }

        public String getProdModelName() {
		    return prodModelName;
        }
        public void setProdModelName(String prodModelName) {
            this.prodModelName = prodModelName;
        }
        public String getProdCategory() {
            return prodCategory;
        }
        public void setProdCategory(String prodCategory) {
            this.prodCategory = prodCategory;
        }
        public int getProdPrice() {
            return prodPrice;
        }
        public void setProdPrice(int prodPrice) {
            this.prodPrice = prodPrice;
        }
        public String getRetailerName() {
            return retailerName;
        }
        public void setRetailerName(String retailerName) {
            this.retailerName = retailerName;
        }
        public String getRetailerZip() {
            return retailerZip;
        }
        public void setRetailerZip(String retailerZip) {
            this.retailerZip = retailerZip;
        }
        public String getRetailerCity() {
            return retailerCity;
        }
        public void setRetailerCity(String retailerCity) {
            this.retailerCity = retailerCity;
        }
        public String getRetailerState() {
            return retailerState;
        }
        public void setRetailerState(String retailerState) {
            this.retailerState = retailerState;
        }
        public String getProdOnSale() {
            return prodOnSale;
        }
        public void setProdOnSale(String prodOnSale) {
            this.prodOnSale = prodOnSale;
        }
        public String getManuName() {
            return manuName;
        }
        public void setManuName(String manuName) {
            this.manuName = manuName;
        }
        public String getManuRebate() {
            return manuRebate;
        }
        public void setManuRebate(String manuRebate) {
            this.manuRebate = manuRebate;
        }
        public String getUserID() {
            return userID;
        }
        public void setUserID(String userID) {
            this.userID = userID;
        }
        public int getUserAge() {
            return userAge;
        }
        public void setUserAge(int userAge) {
            this.userAge = userAge;
        }
        public String getUserGender() {
            return userGender;
        }
        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }
        public String getUserOccupation() {
            return userOccupation;
        }
        public void setUserOccupation(String userOccupation) {
            this.userOccupation = userOccupation;
        }
        public int getReviewRating() {
            return reviewRating;
        }
        public void setReviewRating(int reviewRating) {
            this.reviewRating = reviewRating;
        }
        public String getReviewDate() {
            return reviewDate;
        }
        public void setReviewDate(String reviewDate) {
            this.reviewDate = reviewDate;
        }
        public String getReviewText() {
            return reviewText;
        }
        public void setReviewText(String reviewText) {
            this.reviewText = reviewText;
        }
        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }

}
