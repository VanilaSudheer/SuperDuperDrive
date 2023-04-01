package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 4);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		System.out.println(this.port+"and/"+driver.getCurrentUrl());
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */


	// Write a test that verifies that an unauthorized user can only access the login and
	// signup pages.
	@Test
	public void testUnauthorizedUserHomePageRestriction() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	// Write a test that signs up a new user, logs in, verifies that the home page is
	// accessible, logs out, and verifies that the home page is no longer accessible.
	@Test
	public void testAuthorizedUserHomePageAccess() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		doMockSignUp("Authorized User","Test","abc","321");
		doLogIn("abc", "321");

		// verifies that the home page is accessible
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		// log out
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();

		// verifies that the home page is no longer accessible.
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		System.out.println("here");

		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}

		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));



	}

	/*
		Test Notes Screen
	**/
	@Test
	public void createCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 4);
		// Create a test account
		doMockSignUp("Credential", "Test", "CT", "123");
		doLogIn("CT", "123");

// go to credentials tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		navCredentialsTab.click();

		// Create a new credential
//		WebElement credentialButton = driver.findElement(By.id("nav-credentials"));
//		credentialButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newCredential")));
		WebElement addCredentialButton = driver.findElement(By.id("newCredential"));
		addCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrlInput = driver.findElement(By.id("credential-url"));
		credentialUrlInput.click();
		credentialUrlInput.sendKeys("https://example.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsernameInput = driver.findElement(By.id("credential-username"));
		credentialUsernameInput.click();
		credentialUsernameInput.sendKeys("username");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPasswordInput = driver.findElement(By.id("credential-password"));
		credentialPasswordInput.click();
		credentialPasswordInput.sendKeys("testpassword");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveChangesCredential")));
		WebElement credentialSubmitButton = driver.findElement(By.id("saveChangesCredential"));
		credentialSubmitButton.click();

		// Result page displays Success
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();

		// go back to the credentials tab
		navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		navCredentialsTab.click();

		// verify the displayed password is encrypted
		WebElement encryptedPswd = driver.findElement(By.id("exCredentialPassword"));
		try {
			// `password` is the original password submitted
			Assertions.assertNotEquals(encryptedPswd.getText(), "testpassword");
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Credential password displayed is not encrypted.");
		}

	}
	@Test
	public void testEditCredential() {
		createCredential();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 4);
		// View the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class, 'view-credential-button')]")));
		WebElement credentialViewButton = driver.findElement(By.xpath("//button[contains(@class, 'view-credential-button')]"));
		credentialViewButton.click();


		// Edit the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrlElmnt = driver.findElement(By.id("credential-url"));
		credentialUrlElmnt.click();
		credentialUrlElmnt.clear();
		credentialUrlElmnt.sendKeys("https://edited-example.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsernameElmnt = driver.findElement(By.id("credential-username"));
		credentialUsernameElmnt.click();
		credentialUsernameElmnt.clear();
		credentialUsernameElmnt.sendKeys("username2");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPasswordElmnt = driver.findElement(By.id("credential-password"));
		credentialPasswordElmnt.click();
		credentialPasswordElmnt.clear();
		credentialPasswordElmnt.sendKeys("edited-testpassword");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveChangesCredential")));
		WebElement credentialSubmitBtn = driver.findElement(By.id("saveChangesCredential"));
		credentialSubmitBtn.click();

		// Result page displays Success
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();

		// go back to the credentials tab
		WebElement navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		navCredentialsTab.click();

		// Verify that the changes are displayed
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("exCredentialUrl")));
		WebElement exCredentialUrl = driver.findElement(By.id("exCredentialUrl"));
		Assertions.assertEquals("https://edited-example.com", exCredentialUrl.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("exCredentialUsername")));
		WebElement exCredentialUsername = driver.findElement(By.id("exCredentialUsername"));
		Assertions.assertEquals("username2", exCredentialUsername.getText());

		// verify the displayed password is encrypted
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("exCredentialPassword")));
		WebElement exCredentialPassword = driver.findElement(By.id("exCredentialPassword"));
		try {
			// `password` is the original password submitted
			Assertions.assertNotEquals(exCredentialPassword.getText(), "edited-testpassword");
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Credential password displayed is not encrypted.");
		}
	}
	@Test
	public void testDeleteCredential() {
		createCredential();

		WebDriverWait webDriverWait = new WebDriverWait(driver, 4);
		// Delete the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCredential")));
		WebElement credentialDeleteButton = driver.findElement(By.id("deleteCredential"));
		credentialDeleteButton.click();

		// Verify that the credential is no longer displayed
		List<WebElement> credentialUrlElements = driver.findElements(By.id("exCredentialUrl"));
		Assertions.assertEquals(0, credentialUrlElements.size());
		List<WebElement> credentialUsernameElements = driver.findElements(By.id("exCredentialUsername"));
		Assertions.assertEquals(0, credentialUsernameElements.size());

	}
	@Test
	public void createNote(){
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		// Create a test account
		doMockSignUp("Note", "Test", "CT", "123");
		doLogIn("CT", "123");

		// go to Note tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
		navNotesTab.click();


		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newNote")));
		WebElement addNewNote = driver.findElement(By.id("newNote"));
		addNewNote.click();

		// note modal
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("New note");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("This is the note description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-btn")));
		WebElement saveNoteBtn = driver.findElement(By.id("save-note-btn"));
		saveNoteBtn.click();

		// Result page displays Success
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();

		// go back to the notes tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		navNotesTab = driver.findElement(By.id("nav-notes-tab"));
		navNotesTab.click();

		String note_Title="New note";
		String note_Description="This is the note description";

		// Verify that the note is displayed on the home page
		WebElement noteTitleElement = driver.findElement(By.xpath("//th[contains(text(), '" + note_Title + "')]"));
		Assertions.assertNotNull(noteTitleElement);
		WebElement noteDescriptionElement = driver.findElement(By.xpath("//td[contains(text(), '" + note_Description + "')]"));
		Assertions.assertNotNull(noteDescriptionElement);

	}
	@Test
	public void testNoteEdit(){
		createNote();
		// Edit the note
		String newNoteTitle = "Updated Test Note";
		String newNoteDescription = "This is an updated test note.";
		var wait = new WebDriverWait(driver,20);
		WebElement editNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("editNote")));
		editNoteButton.click();
		WebElement editNoteTitleField = wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
		editNoteTitleField.clear();
		editNoteTitleField.sendKeys(newNoteTitle);
		WebElement editNoteDescriptionField = driver.findElement(By.id("note-description"));
		editNoteDescriptionField.clear();
		editNoteDescriptionField.sendKeys(newNoteDescription);
		WebElement updateNoteButton = driver.findElement(By.id("save-note-btn"));
		updateNoteButton.click();

		// Result page displays Success
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
		navNotesTab.click();

		// Verify that the changes are displayed
		WebElement noteTitleElement = driver.findElement(By.xpath("//th[contains(text(), '" + newNoteTitle + "')]"));
		Assertions.assertNotNull(noteTitleElement);
		WebElement noteDescriptionElement = driver.findElement(By.xpath("//td[contains(text(), '" + newNoteDescription + "')]"));
		Assertions.assertNotNull(noteDescriptionElement);

		/*WebElement noteTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("note-title")));
		String displayedNoteTitle = noteTitleElement.getText();
		WebElement noteDescriptionElement = driver.findElement(By.className("note-description"));
		String displayedNoteDescription = noteDescriptionElement.getText();
		Assertions.assertEquals(newNoteTitle, displayedNoteTitle);
		Assertions.assertEquals(newNoteDescription, displayedNoteDescription);*/
	}
	@Test
	public void testNoteDelete(){
		createNote();
		// Wait for the home page to load
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.urlToBe("http://localhost:" + port + "/home"));

		// Click the delete button for the first note
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNote")));
		WebElement deleteButton = driver.findElement(By.id("deleteNote"));
		deleteButton.click();

		// Wait for the page to refresh
		//wait.until(ExpectedConditions.urlToBe("http://localhost:" + port + "/home"));

		// Verify that the note is no longer displayed
		List<WebElement> noteTitleElements = driver.findElements(By.id("exNoteTitle"));
		Assertions.assertEquals(0, noteTitleElements.size());
		List<WebElement> noteDescriptionElements = driver.findElements(By.id("exNoteDescription"));
		Assertions.assertEquals(0, noteDescriptionElements.size());

//		List<WebElement> notesList = driver.findElements(By.className("note-title"));
//		Assertions.assertFalse(notesList.stream().anyMatch(note -> note.getText().equals("New note")));
	}

}
