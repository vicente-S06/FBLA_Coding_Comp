
// Partner object class
// Used with the CTE_Business_Partner_Search.java class 
// For the 2024 FBLA coding competition
public class Partner {
	// Data Fields
	private String partnerName; // Name of the Partner
	private String orgType; // Type of organization the partner is
	private String resourcesAvailable; // Resources the partner provides to the CTE department
	private String phoneNumber; // Partner's phone number
	private String email; // Partner's email address

	// Default constructor for Partner object
	public Partner() {
		this.partnerName = "Example LLC";
		this.orgType = "N/A";
		this.resourcesAvailable = "N/A";
		this.phoneNumber = "(123) 456-7890";
		this.email = "example@mail.com";
	}

	// 5-arg constructor for Partner Object
	public Partner(String partnerName, String orgType, String resourcesAvailable, String phoneNumber, String email) {
		this.partnerName = partnerName;
		this.orgType = orgType;
		this.resourcesAvailable = resourcesAvailable;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	// Getters and Setters
	public String getPartnerName() {
		return this.partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getResourcesAvailable() {
		return this.resourcesAvailable;
	}

	public void setResourcesAvailable(String resourcesAvailable) {
		this.resourcesAvailable = resourcesAvailable;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Override Tostring
	@Override
	public String toString() {
		String resultantString = "";
		resultantString += "============================\n";
		resultantString += this.partnerName + "\n";
		resultantString += this.orgType + "\n";
		resultantString += this.resourcesAvailable + "\n";
		resultantString += this.phoneNumber + "\n";
		resultantString += this.email + "\n";
		resultantString += "============================";
		return resultantString;
	}
}
