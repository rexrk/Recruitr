#!/bin/bash
# init-db.sh
# Make executable: chmod +x init-db-data.sh

BASE_URL="http://localhost:8080"

# Array of JSON payloads for /api/organizations
org_payloads=(
'{
  "companyName": "Microsoft Corporation",
  "address": "One Microsoft Way",
  "city": "Redmond",
  "email": "contact@microsoft.com",
  "website": "https://www.microsoft.com",
  "orgAdminEmail": "admin@microsoft.com",
  "orgAdminPassword": "microsoft@123"
}'
'{
  "companyName": "Blinkit Technologies Pvt. Ltd.",
  "address": "DLF Cyber City, Phase 2",
  "city": "Gurgaon",
  "email": "support@blinkit.in",
  "website": "https://www.blinkit.com",
  "orgAdminEmail": "admin@blinkit.com",
  "orgAdminPassword": "blinkit@123"
}'
'{
  "companyName": "TalentBridge Staffing Solutions",
  "address": "500 Business Park Road",
  "city": "Bangalore",
  "email": "info@talentbridge.com",
  "website": "https://www.talentbridge.com",
  "orgAdminEmail": "hr@talentbridge.com",
  "orgAdminPassword": "talentbridge@123"
}'
'{
  "companyName": "NextHire Consulting",
  "address": "22 Silicon Avenue",
  "city": "Hyderabad",
  "email": "contact@nexthire.com",
  "website": "https://www.nexthire.com",
  "orgAdminEmail": "admin@nexthire.com",
  "orgAdminPassword": "nexthire@123"
}'
'{
  "companyName": "Global Edge Staffing",
  "address": "45 Corporate Plaza",
  "city": "Pune",
  "email": "hello@globaledge.com",
  "website": "https://www.globaledge.com",
  "orgAdminEmail": "admin@globaledge.com",
  "orgAdminPassword": "globaledge@123"
}'
)

# Loop through org payloads
for payload in "${org_payloads[@]}"; do
  echo "Creating organization..."
  curl -X POST "$BASE_URL/api/organizations" \
       -u raman:admin \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$payload"
  echo -e "\n-----------------------------------\n"
done

echo "All organizations created!"

# ----------------------------
# Vendor & Client assignment payloads
# ----------------------------
assignment_payloads=(
'{
  "organizationId": 1,
  "vendorIds": [3,4,5]
}'
'{
  "organizationId": 2,
  "vendorIds": [3,4,5]
}'
'{
  "organizationId": 5,
  "clientIds": [4]
}'
)

# Loop through all assignments
for payload in "${assignment_payloads[@]}"; do
  echo "Assigning vendors/clients..."
  curl -X POST "$BASE_URL/api/organizations/vendors-and-clients/assign" \
       -u raman:admin \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$payload"
  echo -e "\n-----------------------------------\n"
done

echo "All vendor/client assignments completed!"

# ----------------------------
# Candidates for TalentBridge
# ----------------------------
talentbridge_candidates=(
'{
  "firstName": "Rohan",
  "lastName": "Sharma",
  "email": "rohan.sharma@example.com",
  "phone": "9876543210",
  "resumeReferenceUrl": "https://example.com/resume/rohan-sharma.pdf",
  "primarySkills": ["Java", "Spring Boot", "MySQL"],
  "totalExperience": 3.5,
  "currentLocation": "Bangalore",
  "preferredLocation": "Hyderabad"
}'
'{
  "firstName": "Sneha",
  "lastName": "Patel",
  "email": "sneha.patel@example.com",
  "phone": "9876501234",
  "resumeReferenceUrl": "https://example.com/resume/sneha-patel.pdf",
  "primarySkills": ["Angular", "TypeScript", "Node.js"],
  "totalExperience": 2.8,
  "currentLocation": "Bangalore",
  "preferredLocation": "Pune"
}'
'{
  "firstName": "Amit",
  "lastName": "Kumar",
  "email": "amit.kumar@example.com",
  "phone": "9123456789",
  "resumeReferenceUrl": "https://example.com/resume/amit-kumar.pdf",
  "primarySkills": ["Python", "Django", "AWS"],
  "totalExperience": 4.0,
  "currentLocation": "Hyderabad",
  "preferredLocation": "Bangalore"
}'
)

for payload in "${talentbridge_candidates[@]}"; do
  curl -X POST "$BASE_URL/api/organization/candidates" \
       -u hr@talentbridge.com:talentbridge@123 \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$payload"
  echo -e "\n-----------------------------------\n"
done

# ----------------------------
# Candidates for NextHire
# ----------------------------
nexthire_candidates=(
'{
  "firstName": "Karthik",
  "lastName": "Reddy",
  "email": "karthik.reddy@example.com",
  "phone": "9988776655",
  "resumeReferenceUrl": "https://example.com/resume/karthik-reddy.pdf",
  "primarySkills": ["Python", "Django", "AWS"],
  "totalExperience": 5.0,
  "currentLocation": "Hyderabad",
  "preferredLocation": "Chennai"
}'
'{
  "firstName": "Priya",
  "lastName": "Singh",
  "email": "priya.singh@example.com",
  "phone": "9876512345",
  "resumeReferenceUrl": "https://example.com/resume/priya-singh.pdf",
  "primarySkills": ["React", "JavaScript", "Node.js"],
  "totalExperience": 3.7,
  "currentLocation": "Chennai",
  "preferredLocation": "Hyderabad"
}'
'{
  "firstName": "Vikram",
  "lastName": "Gupta",
  "email": "vikram.gupta@example.com",
  "phone": "9123456701",
  "resumeReferenceUrl": "https://example.com/resume/vikram-gupta.pdf",
  "primarySkills": ["Java", "Spring Boot", "MySQL"],
  "totalExperience": 4.5,
  "currentLocation": "Bangalore",
  "preferredLocation": "Hyderabad"
}'
)

for payload in "${nexthire_candidates[@]}"; do
  curl -X POST "$BASE_URL/api/organization/candidates" \
       -u admin@nexthire.com:nexthire@123 \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$payload"
  echo -e "\n-----------------------------------\n"
done

# ----------------------------
# Candidates for Global Edge
# ----------------------------
globaledge_candidates=(
'{
  "firstName": "Anita",
  "lastName": "Verma",
  "email": "anita.verma@example.com",
  "phone": "9123456780",
  "resumeReferenceUrl": "https://example.com/resume/anita-verma.pdf",
  "primarySkills": ["React", "TypeScript", "Node.js"],
  "totalExperience": 4.2,
  "currentLocation": "Pune",
  "preferredLocation": "Bangalore"
}'
'{
  "firstName": "Siddharth",
  "lastName": "Mehta",
  "email": "siddharth.mehta@example.com",
  "phone": "9988771122",
  "resumeReferenceUrl": "https://example.com/resume/siddharth-mehta.pdf",
  "primarySkills": ["Angular", "JavaScript", "Node.js"],
  "totalExperience": 3.8,
  "currentLocation": "Pune",
  "preferredLocation": "Mumbai"
}'
'{
  "firstName": "Neha",
  "lastName": "Shah",
  "email": "neha.shah@example.com",
  "phone": "9876543211",
  "resumeReferenceUrl": "https://example.com/resume/neha-shah.pdf",
  "primarySkills": ["Python", "Flask", "AWS"],
  "totalExperience": 4.1,
  "currentLocation": "Mumbai",
  "preferredLocation": "Pune"
}'
'{
  "firstName": "Bhargavi",
  "lastName": "Naik",
  "email": "bhargavi.baka@gmail.com",
  "phone": "9876512340",
  "resumeReferenceUrl": "https://example.com/resume/bhargavi-nn.pdf",
  "primarySkills": ["JavaScript", "React", "Node.js"],
  "totalExperience": 3.8,
  "currentLocation": "Bangalore",
  "preferredLocation": "Mumbai"
}'
)

for payload in "${globaledge_candidates[@]}"; do
  curl -X POST "$BASE_URL/api/organization/candidates" \
       -u admin@globaledge.com:globaledge@123 \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$payload"
  echo -e "\n-----------------------------------\n"
done

echo "All candidates created!"

# ----------------------------
# Jobs for Microsoft
# ----------------------------
microsoft_jobs=(
'{
  "title": "Frontend Developer",
  "description": "Build and maintain user interfaces using React and JavaScript, ensure responsive design.",
  "requiredSkills": ["JavaScript", "React", "CSS"],
  "experienceLevel": "JUNIOR"
}'
'{
  "title": "Backend Developer",
  "description": "Develop REST APIs and microservices using Spring Boot and Java.",
  "requiredSkills": ["Java", "Spring Boot", "MySQL"],
  "experienceLevel": "MID"
}'
'{
  "title": "Full Stack Developer",
  "description": "Work on both frontend and backend, integrating services and UI.",
  "requiredSkills": ["JavaScript", "React", "Spring Boot", "MySQL"],
  "experienceLevel": "SENIOR"
}'
)

for job in "${microsoft_jobs[@]}"; do
  curl -X POST "$BASE_URL/api/organization/jobs" \
       -u admin@microsoft.com:microsoft@123 \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$job"
  echo -e "\n-----------------------------------\n"
done

# ----------------------------
# Jobs for Blinkit
# ----------------------------
blinkit_jobs=(
'{
  "title": "DevOps Engineer",
  "description": "Manage CI/CD pipelines, deploy and monitor applications.",
  "requiredSkills": ["Docker", "Kubernetes", "AWS"],
  "experienceLevel": "MID"
}'
'{
  "title": "QA Engineer",
  "description": "Perform manual and automated testing for web applications.",
  "requiredSkills": ["Selenium", "JUnit", "Cypress"],
  "experienceLevel": "JUNIOR"
}'
'{
  "title": "UI/UX Designer",
  "description": "Design wireframes, prototypes and high-fidelity mockups.",
  "requiredSkills": ["Figma", "Adobe XD", "Sketch"],
  "experienceLevel": "MID"
}'
)

for job in "${blinkit_jobs[@]}"; do
  curl -X POST "$BASE_URL/api/organization/jobs" \
       -u admin@blinkit.com:blinkit@123 \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$job"
  echo -e "\n-----------------------------------\n"
done

echo "All jobs created!"

# ----------------------------
# Job assignments for Microsoft
# ----------------------------
microsoft_job_assignments=(
'{
  "jobId": 1,
  "candidateIds": [1,2,3]
}'
'{
  "jobId": 2,
  "candidateIds": [4,5,10]
}'
'{
  "jobId": 3,
  "candidateIds": [7,8,9]
}'
)

for assign in "${microsoft_job_assignments[@]}"; do
  curl -X POST "$BASE_URL/api/organization/jobs/assigns" \
       -u admin@microsoft.com:microsoft@123 \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$assign"
  echo -e "\n-----------------------------------\n"
done

# ----------------------------
# Job assignments for Blinkit
# ----------------------------
blinkit_job_assignments=(
'{
  "jobId": 4,
  "candidateIds": [2,4,6]
}'
'{
  "jobId": 5,
  "candidateIds": [1,3,5]
}'
'{
  "jobId": 6,
  "candidateIds": [7,8,10]
}'
)

for assign in "${blinkit_job_assignments[@]}"; do
  curl -X POST "$BASE_URL/api/organization/jobs/assigns" \
       -u admin@blinkit.com:blinkit@123 \
       -H "accept: */*" \
       -H "Content-Type: application/json" \
       -d "$assign"
  echo -e "\n-----------------------------------\n"
done

echo "All job assignments completed!"

