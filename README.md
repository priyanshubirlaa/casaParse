# **PDF Parsing with Gemini AI - README**

## **Overview**
This is a **Spring Boot** application that processes PDF files using **Apache PDFBox** and integrates with **Google Gemini AI** for extracting structured data. The application can handle **password-protected PDFs**, extract text, and use AI to retrieve key details such as **Name, Email, Opening Balance, and Closing Balance**.

---

## **Solution Details**
### **Tech Stack**
- **Backend**: Java (Spring Boot)
- **AI Service**: Google Gemini AI
- **PDF Processing**: Apache PDFBox
- **API Calls**: OkHttpClient
- **Build Tool**: Maven

### **Core Features**
- Upload PDF via API
- Handle password-protected PDFs (requires `firstname` and `dob` as password)
- Extract text from PDFs using Apache PDFBox
- Process extracted text with Gemini AI
- Return structured JSON output

---

## **Project Structure**
```
casaParse/
│── src/
│   ├── main/
│   │   ├── java/com/pdfParse/gemini/
│   │   │   ├── controller/PdfReaderController.java
│   │   │   ├── service/PdfProcessingService.java
│   │   │   ├── service/GeminiAIService.java
│   │   │   ├── utils/FileUtils.java
│   │   ├── resources/
│   │   │   ├── application.properties
│── pom.xml
│── README.md
```

---

## **Deployment Notes**
### **Prerequisites**
- Java 17+
- Maven
- Google Gemini AI API Key (Get from [Google AI Studio](https://aistudio.google.com/))

### **How to Get the API Key?**
1. Go to [Google AI Studio](https://aistudio.google.com/).
2. Sign in with your Google account.
3. Navigate to "API Keys" and generate a new key.
4. Add the key to `application.properties`:
   ```properties
   gemini.api.key=YOUR_API_KEY_HERE
   ```

---

## **How to Run the Application**
1. **Clone the Repository**
   ```bash
   git clone [<repository-url>](https://github.com/priyanshubirlaa/casaParse)
   cd casaParse
   ```
2. **Configure API Key in `application.properties`**
   ```properties
   spring.application.name=pdfParse
   gemini.api.key=YOUR_API_KEY_HERE
   gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=
   ```
3. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. **Test the API**
   - Use **Postman** or **cURL** to test the API.
   - Endpoint: `POST /api/pdf/parse`
   - Example Request:
     ```bash
     curl -X POST -F "file=@sample.pdf" "http://localhost:8080/api/pdf/parse"
     ```

---

## **API Endpoints**
### **1. Parse PDF**
**Request:**
```http
POST /api/pdf/parse
```
**Parameters:**
- `file` (MultipartFile, required) - The PDF to process.
- `firstname` (String, required only for password-protected files) - Used as part of the password.
- `dob` (String, required only for password-protected files) - Used as part of the password.

**Response:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "openingBalance": "1000",
  "closingBalance": "2000"
}
```

---

## **Implementation Strategy**
1. **Extract Text from PDFs**
   - Used **Apache PDFBox** to read PDFs.
   - If password-protected, use `firstname + dob` as the password.
2. **AI Integration**
   - Sent extracted text to **Google Gemini AI** for structured data processing.
   - Used **OkHttpClient** to send API requests.
3. **API Design**
   - **Spring Boot REST API** (`POST /api/pdf/parse`).
   - **Response Handling** with structured JSON.

---

## **Challenges Faced & Solutions**
### **1. Handling Password-Protected PDFs**
**Problem:** PDFBox throws an error if the file is locked.
**Solution:** Prompted users to input `firstname` and `dob` as password.

### **2. JSON Formatting in AI Response**
**Problem:** Gemini AI sometimes returned unstructured data.
**Solution:** Used a **specific prompt format** to enforce JSON responses.

### **3. AI Model Limitations**
**Problem:** AI sometimes extracted incorrect or ambiguous data.
**Solution:** Applied **post-processing rules** to validate extracted content.

---

## **Next Steps & Improvements**
- Support multiple document formats (**Word, Excel**).
- Add **frontend UI** for user-friendly file uploads.

---

## **Author & Contact**
- **Priyanshu Birla**
- Email: [priyanshubirla22@gmail.com](mailto:priyanshubirla22@gmail.com)
  


