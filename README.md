#Assignment
#BCA - Mobile Programming
##Overview
You are required to build an Android application using Java and follow the MVVM
architecture design pattern.
• Minimum SDK Version: 28
• Package Name Format:
com.<firstname><lastname>.<studentid>
For example, if the student's name is Sunil Prasai and the student ID is ID1001,
then the package name should be:
com.sunilprasai.id1001
###1. App Requirements
• Launch Screen: Login UI
Display a login form with input fields for:
o Email
o Password
• Validate these fields with proper error messages for empty or invalid entries.
###2. API Integration
• Use the following API to validate login:
POST https://sunilprasai.com.np/api/user/login
Headers:
Accept: application/json [Added Automatically by Retrofit]
Content-Type: application/json [Added Automatically by Retrofit]
Body (raw JSON):
{
"email": "<user_email>",
"password": "<user_password>"
}
• On successful login:
o Navigate to the Home screen.
o Save token received from API in Shared Preference Storage.
o Display user details received from the API.
• On failed login:
o Show the error message returned by the API.
3. Architecture: MVVM
• You may use Activities or Fragments to manage navigation and UI.
