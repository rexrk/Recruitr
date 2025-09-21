@echo off
REM init-db.bat
REM Run this file using: init-db.bat

SET BASE_URL=http://localhost:8080

REM ----------------------------
REM Create Organizations
REM ----------------------------
curl -X POST "%BASE_URL%/api/organizations" -u raman:admin -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""companyName"": ""Microsoft Corporation"",
  ""address"": ""One Microsoft Way"",
  ""city"": ""Redmond"",
  ""email"": ""contact@microsoft.com"",
  ""website"": ""https://www.microsoft.com"",
  ""orgAdminEmail"": ""admin@microsoft.com"",
  ""orgAdminPassword"": ""microsoft@123""
}"
echo.
curl -X POST "%BASE_URL%/api/organizations" -u raman:admin -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""companyName"": ""Blinkit Technologies Pvt. Ltd."",
  ""address"": ""DLF Cyber City, Phase 2"",
  ""city"": ""Gurgaon"",
  ""email"": ""support@blinkit.in"",
  ""website"": ""https://www.blinkit.com"",
  ""orgAdminEmail"": ""admin@blinkit.com"",
  ""orgAdminPassword"": ""blinkit@123""
}"
echo.
curl -X POST "%BASE_URL%/api/organizations" -u raman:admin -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""companyName"": ""TalentBridge Staffing Solutions"",
  ""address"": ""500 Business Park Road"",
  ""city"": ""Bangalore"",
  ""email"": ""info@talentbridge.com"",
  ""website"": ""https://www.talentbridge.com"",
  ""orgAdminEmail"": ""hr@talentbridge.com"",
  ""orgAdminPassword"": ""talentbridge@123""
}"
echo.
curl -X POST "%BASE_URL%/api/organizations" -u raman:admin -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""companyName"": ""NextHire Consulting"",
  ""address"": ""22 Silicon Avenue"",
  ""city"": ""Hyderabad"",
  ""email"": ""contact@nexthire.com"",
  ""website"": ""https://www.nexthire.com"",
  ""orgAdminEmail"": ""admin@nexthire.com"",
  ""orgAdminPassword"": ""nexthire@123""
}"
echo.
curl -X POST "%BASE_URL%/api/organizations" -u raman:admin -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""companyName"": ""Global Edge Staffing"",
  ""address"": ""45 Corporate Plaza"",
  ""city"": ""Pune"",
  ""email"": ""hello@globaledge.com"",
  ""website"": ""https://www.globaledge.com"",
  ""orgAdminEmail"": ""admin@globaledge.com"",
  ""orgAdminPassword"": ""globaledge@123""
}"
echo.
echo All organizations created!

REM ----------------------------
REM Vendor/Client assignments
REM ----------------------------
curl -X POST "%BASE_URL%/api/organizations/vendors-and-clients/assign" -u raman:admin -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""organizationId"": 1,
  ""vendorIds"": [3,4,5]
}"
echo.
curl -X POST "%BASE_URL%/api/organizations/vendors-and-clients/assign" -u raman:admin -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""organizationId"": 2,
  ""vendorIds"": [3,4,5]
}"
echo.
curl -X POST "%BASE_URL%/api/organizations/vendors-and-clients/assign" -u raman:admin -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""organizationId"": 5,
  ""clientIds"": [4]
}"
echo.
echo All vendor/client assignments completed!

REM ----------------------------
REM Candidates for TalentBridge
REM ----------------------------
curl -X POST "%BASE_URL%/api/organization/candidates" -u hr@talentbridge.com:talentbridge@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""firstName"": ""Rohan"",
  ""lastName"": ""Sharma"",
  ""email"": ""rohan.sharma@example.com"",
  ""phone"": ""9876543210"",
  ""resumeReferenceUrl"": ""https://example.com/resume/rohan-sharma.pdf"",
  ""primarySkills"": [""Java"",""Spring Boot"",""MySQL""],
  ""totalExperience"": 3.5,
  ""currentLocation"": ""Bangalore"",
  ""preferredLocation"": ""Hyderabad""
}"
echo.
curl -X POST "%BASE_URL%/api/organization/candidates" -u hr@talentbridge.com:talentbridge@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""firstName"": ""Sneha"",
  ""lastName"": ""Patel"",
  ""email"": ""sneha.patel@example.com"",
  ""phone"": ""9876501234"",
  ""resumeReferenceUrl"": ""https://example.com/resume/sneha-patel.pdf"",
  ""primarySkills"": [""Angular"",""TypeScript"",""Node.js""],
  ""totalExperience"": 2.8,
  ""currentLocation"": ""Bangalore"",
  ""preferredLocation"": ""Pune""
}"
echo.
curl -X POST "%BASE_URL%/api/organization/candidates" -u hr@talentbridge.com:talentbridge@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""firstName"": ""Amit"",
  ""lastName"": ""Kumar"",
  ""email"": ""amit.kumar@example.com"",
  ""phone"": ""9123456789"",
  ""resumeReferenceUrl"": ""https://example.com/resume/amit-kumar.pdf"",
  ""primarySkills"": [""Python"",""Django"",""AWS""],
  ""totalExperience"": 4.0,
  ""currentLocation"": ""Hyderabad"",
  ""preferredLocation"": ""Bangalore""
}"
echo.
REM Repeat similar curl commands for NextHire and Global Edge candidates

REM ----------------------------
REM Jobs for Microsoft
REM ----------------------------
curl -X POST "%BASE_URL%/api/organization/jobs" -u admin@microsoft.com:microsoft@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""title"": ""Frontend Developer"",
  ""description"": ""Build and maintain user interfaces using React and JavaScript, ensure responsive design."",
  ""requiredSkills"": [""JavaScript"",""React"",""CSS""],
  ""experienceLevel"": ""JUNIOR""
}"
echo.
curl -X POST "%BASE_URL%/api/organization/jobs" -u admin@microsoft.com:microsoft@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""title"": ""Backend Developer"",
  ""description"": ""Develop REST APIs and microservices using Spring Boot and Java."",
  ""requiredSkills"": [""Java"",""Spring Boot"",""MySQL""],
  ""experienceLevel"": ""MID""
}"
echo.
curl -X POST "%BASE_URL%/api/organization/jobs" -u admin@microsoft.com:microsoft@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""title"": ""Full Stack Developer"",
  ""description"": ""Work on both frontend and backend, integrating services and UI."",
  ""requiredSkills"": [""JavaScript"",""React"",""Spring Boot"",""MySQL""],
  ""experienceLevel"": ""SENIOR""
}"
echo.
REM Repeat similar curl commands for Blinkit jobs

REM ----------------------------
REM Job assignments for Microsoft
REM ----------------------------
curl -X POST "%BASE_URL%/api/organization/jobs/assigns" -u admin@microsoft.com:microsoft@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""jobId"": 1,
  ""candidateIds"": [1,2,3]
}"
echo.
curl -X POST "%BASE_URL%/api/organization/jobs/assigns" -u admin@microsoft.com:microsoft@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""jobId"": 2,
  ""candidateIds"": [4,5,10]
}"
echo.
curl -X POST "%BASE_URL%/api/organization/jobs/assigns" -u admin@microsoft.com:microsoft@123 -H "accept: */*" -H "Content-Type: application/json" -d "{
  ""jobId"": 3,
  ""candidateIds"": [7,8,9]
}"
echo.
REM Repeat similar curl commands for Blinkit job assignments

echo All jobs and assignments completed!
pause
